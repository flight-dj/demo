package factory;

import db.RedisUtil;
import message.delay.ConsumerCarriedOutTask;
import redis.clients.jedis.JedisPool;

/**
 * @Description:
 * @Author: dajun
 * @Date: 2020/10/26 3:54 下午
 **/
public class ConsumerCarriedOutTaskFactory {

    public static final ConsumerCarriedOutTask consumerCarriedOutTask = null;

    public static ConsumerCarriedOutTask getInstance() {
        synchronized (ConsumerCarriedOutTaskFactory.class) {
            if (consumerCarriedOutTask == null) {
                RedisUtil redisUtil = RedisUtil.getRedisUtil();
                JedisPool jedisPool = redisUtil.getJedisPool();
                return new ConsumerCarriedOutTask(jedisPool, 1);
            }
        };
        return null;
    }


}
