package factory;

import db.RedisUtil;
import message.delay.TaskRecycleBinList;
import redis.clients.jedis.JedisPool;

/**
 * @Description:
 * @Author: dajun
 * @Date: 2020/10/26 3:58 下午
 **/
public class TimeRoundFactory {

    public static final TaskRecycleBinList taskRecycleBinList = null;

    public static TaskRecycleBinList getInstance() {
        synchronized (TaskRecycleBinListFactory.class) {
            if (taskRecycleBinList == null) {
                RedisUtil redisUtil = RedisUtil.getRedisUtil();
                JedisPool jedisPool = redisUtil.getJedisPool();
                return new TaskRecycleBinList(jedisPool);
            }
        };
        return null;
    }

}
