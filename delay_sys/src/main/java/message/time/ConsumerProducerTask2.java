package message.time;

import com.alibaba.fastjson.JSONObject;
import config.TimerConfig;
import org.apache.rocketmq.common.message.Message;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import timing.ringlist.TimeRound;
import timing.template.BusinessTask;
import timing.template.DateToX;
import timing.template.Ring;
import utils.BaseUtil;
import utils.MessageUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/***
 * ConsumerProducerTask
 */
public class ConsumerProducerTask2 extends Thread{
    private final Jedis jedis;
    private final TimerConfig timerConfig = TimerConfig.getTimerConfig();
    public ConsumerProducerTask2(JedisPool jedisPool){
        this.jedis = jedisPool.getResource();
    }

    @Override
    public void run(){
        System.out.println("发布任务启动");
        while(true){
            try{
                String task = jedis.rpop(timerConfig.getHashedWheelProducerTask());
                if(!BaseUtil.isBlank(task)){
                    BusinessTask businessTask = MessageUtil.getObjectFromString(task, BusinessTask.class);
                    this.schedule(businessTask.getUrl(), businessTask.getMessage(), businessTask.getInitialDelay());
                }else{
                    sleep(1000);
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void schedule(final String url, final Message params, DateToX initialDelay){
        BusinessTask businessTask = new BusinessTask();
        businessTask.setId(UUID.randomUUID().toString());
        businessTask.setUrl(url);
        businessTask.setMessage(params);
        businessTask.setCycle(false);
        businessTask.setInitialDelay(initialDelay);
        businessTask.setCarriedTurns(this.getCarriedTurns(initialDelay.getMilliSecond()));
        System.out.println(String.format("发布任务: %s%s", JSONObject.toJSONString(businessTask), new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date())));
        String pointerAddress = this.getPointerAddress(jedis);
        int pointer = this.getPointerAddressSubscript(pointerAddress);
        long dle = TimeUnit.MILLISECONDS.toSeconds(initialDelay.getMilliSecond());
        businessTask.setSlot(this.getSolt(pointerAddress, (int) dle));
        //如果应该放的槽位和当前指针地址一致 且 执行环数为0的时候则直接发布到执行队列
        if(businessTask.getSlot() == pointer && businessTask.getCarriedTurns() == 0){
            this.producerTask(businessTask, jedis);
            return;
        }
        pointerAddress = timerConfig.getKeyPrefix() + businessTask.getSlot();
        this.producerTask(pointerAddress, businessTask, jedis);
    }

    private Long producerTask(BusinessTask businessTask, Jedis jedis){
        return TimeRound.producerTask(jedis, businessTask);
    }

    private void producerTask(final String pointerAddress, final BusinessTask businessTask, final Jedis jedis){
        final String pointerAddressLock = pointerAddress + "lock";
        while(true){
            try{
                String isOk = jedis.set(
                        pointerAddressLock,
                        UUID.randomUUID().toString(),
                        "NX",
                        "PX",
                        new DateToX(TimeUnit.SECONDS, 5).getMilliSecond()
                );
                if("OK".equals(isOk)){
                    Ring ring = TimeRound.getCurrentRing(pointerAddress, jedis);
                    List<BusinessTask> businessTaskList = ring.getTaskList();
                    businessTaskList.add(businessTask);
                    ring.setTaskList(businessTaskList);
                    String isOk2 = jedis.set(pointerAddress, MessageUtil.object2String(ring));
                    if(!"OK".equals(isOk2)){
                        continue;
                    }
                    long isOkLong = jedis.del(pointerAddressLock);
                    if(isOkLong == 1){
                        break;
                    }
                }
                Thread.sleep(50);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /***
     * 获取当前指针的位置(没有锁的校验只有查询)
     * @return
     */
    private String getPointerAddress(Jedis jedis){
        return jedis.get(timerConfig.getPointer());
    }

    /***
     * 获取应该放在的槽格的下标
     * @param pointer 下标地址
     * @param delayDate 延迟时间
     * @return
     */
    private int getSolt(int pointer, int delayDate){
        pointer = pointer + 1;
        delayDate = delayDate / timerConfig.getLockOutTime();
        int slot = 0;
        slot = ((slot = pointer + delayDate % timerConfig.getGrooveNumber()) > timerConfig.getGrooveNumber() ? slot - timerConfig.getGrooveNumber() : slot < 0 ? slot * -1 : slot) - 1;
        return slot;
    }

    private int getSoltTest(int pointer, int delayDate){
        delayDate = delayDate / timerConfig.getLockOutTime();
        int slot = (slot = pointer + delayDate % timerConfig.getGrooveNumber()) > timerConfig.getGrooveNumber() ? slot - timerConfig.getGrooveNumber() : slot < 0 ? slot * -1 : slot;
        return slot;
    }

    /***
     * 从地址中获取下标key
     * @return
     */
    private int getPointerAddressSubscript(String pointerAddress){
        int point = Integer.parseInt(pointerAddress.substring(timerConfig.getKeyPrefix().length()));
        return point == 0 ? (timerConfig.getGrooveNumber() - 1) : point - 1;
    }

    /***
     * 获取应该放在的槽格的下标
     * @param pointer 下标地址
     * @param delayDate 延迟时间单位毫秒
     * @return
     */
    private int getSolt(String pointer, int delayDate){
        return this.getSolt(this.getPointerAddressSubscript(pointer), delayDate);
    }

    /***
     * 指针扫描几圈后执行(可以理解为该任务的执行触发点),当小于等于0的时候执行
     * @param milliSecond 毫秒
     * @return
     */
    private int getCarriedTurns(long milliSecond){
        return (int) (milliSecond / (new DateToX(TimerConfig.timeUnit, timerConfig.getGrooveNumber()).getMilliSecond() * timerConfig.getLockOutTime()));
    }

}
