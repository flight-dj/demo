package message.delay;

import com.alibaba.fastjson.JSONObject;
import config.TimerConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import timing.template.BusinessTask;
import utils.BaseUtil;
import utils.MessageUtil;
import utils.TaskUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * ConsumerCarriedOutTask
 */
public class ConsumerCarriedOutTask extends Thread{
    private Jedis jedis;

    /***
     * 时间轮配置文件
     */
    private final static TimerConfig timerConfig = TimerConfig.getTimerConfig();

    /***
     * 失败次数上限
     */
    private final int failuresNumberCapped = 5;

    /***
     * 垃圾回收队列对象
     */
    private TaskRecycleBinList taskRecycleBinList;

    /***
     * 频道
     */
    private String channel;

    /***
     * 备用频道
     */
    private String channelTmp;

    /***
     * 执行锁
     */
    private String channelTmpLock = "hashed-wheel-channelTmpLock";

    public ConsumerCarriedOutTask(JedisPool jedisPool, int name){
        super(name + "");
        this.channel = timerConfig.getChannel();
        this.channelTmp = timerConfig.getChannelTmp();
        this.jedis = jedisPool.getResource();
        this.taskRecycleBinList = new TaskRecycleBinList(jedisPool);
    }

    public void run(){
        System.out.println("执行任务消费者启动---");
        while(true){
            try{
                if("OK".equals(jedis.set(this.channelTmpLock, this.channelTmpLock, "NX", "PX", 5000))){
                    String task = jedis.brpoplpush(this.channel, this.channelTmp, 5);
                    if(BaseUtil.isBlank(task)){
                        continue;
                    }
                    BusinessTask businessTask = MessageUtil.getObjectFromString(task, BusinessTask.class);
                    boolean bool = TaskUtil.execTaskAnsy(businessTask);
                    System.out.println("任务执行结果" + bool + "--->任务详细:" + JSONObject.toJSONString(businessTask) + ", date: " + new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date()));
                    //将结果存入redis中
                    if(bool){//成功则删除此任务
                        jedis.rpop(this.channelTmp);
                    }else{//未成功则判断这个任务失败了多少次,如果超过上限则添加到任务回收站队列
                        String taskId = businessTask.getId();
                        long failuresNumber = jedis.incr(taskId);
                        if(failuresNumber > this.failuresNumberCapped){
                            //添加到任务回收站队列
                            this.taskRecycleBinList.setFailureTask();
                            System.out.println(String.format("失败次数超过%d进入失败任务回收队列--->%s", failuresNumberCapped, task));
                        }else{
                            jedis.rpoplpush(this.channelTmp, this.channel);
                        }
                    }
                    //弹回执行队列,tmp队列按逻辑上讲长度应该时刻保持0-1的长度
                    boolean reboundBool = true;
                    while(reboundBool){
                        String reboundTask = jedis.rpoplpush(this.channelTmp, this.channel);
                        if(BaseUtil.isBlank(reboundTask)){
                            reboundBool = false;
                        }
                    }
                    jedis.del(this.channelTmpLock);
                }else{
                    Thread.sleep(1000);
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /***
     * 添加一个消息任务
     * @param task
     */
    public void setTask(String task){
        jedis.rpush(this.channel, task);
    }
}
