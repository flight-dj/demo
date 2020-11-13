package db;

import org.apache.commons.lang3.RandomUtils;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/***
 * RedisUtil
 */
public class RedisClusterUtil {

    public static void main(String[] args) {

        // 第一步：使用JedisCluster对象。需要一个Set<HostAndPort>参数。Redis节点的列表。
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("192.168.31.110", 7001));
        nodes.add(new HostAndPort("192.168.31.110", 7002));
        nodes.add(new HostAndPort("192.168.31.110", 7003));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        try {
            // 第二步：直接使用JedisCluster对象操作redis。在系统中单例存在。
    //        jedisCluster.set("a", "1");
    //        String result = jedisCluster.get("a");
    //        // 第三步：打印结果
    //        System.out.println(result);
    //        // 第四步：系统关闭前，关闭JedisCluster对象。
    //        jedisCluster.close();

            ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 3, 1000,
                    TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1024), new ThreadPoolExecutor.DiscardPolicy());

            CountDownLatch countDownLatch = new CountDownLatch(1000);
            for (int i=0; i< 1000; i++) {

                int finalI = i;
                poolExecutor.execute(()-> {
                    jedisCluster.zadd("collections", Integer.valueOf(RandomUtils.nextInt(1,1000)), String.valueOf(finalI));
//                    jedisCluster.lpush("collections", String.valueOf(RandomUtils.nextInt(1,1000)));
                    countDownLatch.countDown();
                });

            }
            countDownLatch.await();

//            System.out.println("collections的内容是:"+jedisCluster.lrange("collections",0,-1));
            SortingParams sortingParams = new SortingParams();
//            System.out.println("升序后的结果:"+jedisCluster.sort("collections",sortingParams.asc()));
//            System.out.println("降序后的结果:"+jedisCluster.sort("collections",sortingParams.desc()));

            Set<String> set = jedisCluster.zrange("collections",0,-1); //递增
            Set<String> revSet = jedisCluster.zrevrange("sortedSet",0,-1); //递减

            set.forEach(item -> {
                System.out.println(item+":"+jedisCluster.zscore("collections",item));

            });

            poolExecutor.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                jedisCluster.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
