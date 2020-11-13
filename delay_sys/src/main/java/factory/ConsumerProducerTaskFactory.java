package factory;

import db.RedisUtil;
import message.delay.ConsumerProducerTask;
import redis.clients.jedis.JedisPool;

/**
 * @Description:
 * @Author: dajun
 * @Date: 2020/10/26 3:50 下午
 **/
public class ConsumerProducerTaskFactory {

    public static final ConsumerProducerTask consumerProducerTask = null;

    public static ConsumerProducerTask getInstance() {
       synchronized (ConsumerProducerTaskFactory.class) {
           if (consumerProducerTask == null) {
               RedisUtil redisUtil = RedisUtil.getRedisUtil();
               JedisPool jedisPool = redisUtil.getJedisPool();
                return new ConsumerProducerTask(jedisPool);
           }
       };
       return null;
    }

}
