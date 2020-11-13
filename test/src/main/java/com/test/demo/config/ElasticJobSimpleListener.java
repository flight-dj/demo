//package com.test.demo.config;
//
//import com.dangdang.ddframe.job.executor.ShardingContexts;
//import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
//import lombok.extern.slf4j.Slf4j;
//
///**
//  * @Description: 计算job执行时间
//  * @Author: dajun
//  * @Date: 2020/8/28 11:08 上午
//**/
//@Slf4j
//public class ElasticJobSimpleListener implements ElasticJobListener {
//
//    private long beginTime = 0;
//
//    @Override
//    public void beforeJobExecuted(ShardingContexts shardingContexts) {
//        beginTime = System.currentTimeMillis();
//        log.info("[ElasticJob] - ##Before Job## @{}", shardingContexts.getJobName());
//    }
//
//    @Override
//    public void afterJobExecuted(ShardingContexts shardingContexts) {
//        log.info("[ElasticJob] - ##End Job## @{}, cost : {} ms", shardingContexts.getJobName(), System.currentTimeMillis() - beginTime);
//    }
//
//}
