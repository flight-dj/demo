package start;

import db.RedisUtil;
import factory.ConsumerCarriedOutTaskFactory;
import factory.ConsumerProducerTaskFactory;
import factory.TaskRecycleBinListFactory;
import http.NettyHttpServer;
import message.delay.ConsumerCarriedOutTask;
import message.delay.ConsumerProducerTask;
import message.delay.TaskRecycleBinList;
import org.apache.rocketmq.common.ThreadFactoryImpl;
import redis.clients.jedis.JedisPool;
import timing.ringlist.TimeRound;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerStart{

    public static void main(String[] args){
        RedisUtil redisUtil = RedisUtil.getRedisUtil();
        JedisPool jedisPool = redisUtil.getJedisPool();
        int count = 1;

        ConsumerCarriedOutTask[] consumerCarriedOutTasks = new ConsumerCarriedOutTask[count];
        for(int i = 0; i < count; i++){
            consumerCarriedOutTasks[i] = ConsumerCarriedOutTaskFactory.getInstance();
            consumerCarriedOutTasks[i].start();
        }

        ConsumerProducerTask consumerProducerTask = ConsumerProducerTaskFactory.getInstance();
        consumerProducerTask.start();

        TaskRecycleBinList taskRecycleBinList = TaskRecycleBinListFactory.getInstance();
        taskRecycleBinList.start();

        TimeRound timeRound = new TimeRound(jedisPool);
        timeRound.traverse();

        new NettyHttpServer().start();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl(
                "TimerStart"));
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    TimerStart.register();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }, 1000 * 10, 1000 * 10, TimeUnit.MILLISECONDS);
    }

    private static void register() {


    }

}
