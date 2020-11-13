package com.test.demo.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryNTimes;

public class TestWatch {

    private static final String ZK_ADDRESS = "localhost:2181";

    public static void main(String[] args) throws Exception {
        // 1.Connect to zk
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, new RetryNTimes(10, 5000));
        client.start();

        System.out.println(client.getState());
        System.out.println("zk client start successfully!");

        NodeCache nodeCache = new NodeCache(client, "/app2");

//        nodeCache.getListenable().addListener(new NodeCacheListener() {
//            @Override
//            public void nodeChanged() throws Exception {
//                byte[] data = nodeCache.getCurrentData().getData();
//                System.out.println("nodeCache change: " + new String(data));
//            }
//        });

        //开启监听，如果为true则开启监听器
        nodeCache.start(true);

        System.out.println("监听器已开启！");
        //让线程休眠30s(为了方便测试)
        Thread.sleep(1000000 * 30);
    }
}
