//package com.test.demo.config;
//
//import com.dangdang.ddframe.job.api.simple.SimpleJob;
//import com.dangdang.ddframe.job.config.JobCoreConfiguration;
//import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
//import com.dangdang.ddframe.job.lite.api.JobScheduler;
//import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
//import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
//import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
//import com.test.demo.job.SpringBootSimpleJob;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.Resource;
//
///**
//  * @Description: ElasticJobConfig
//  * @Author: dajun
//  * @Date: 2020/8/28 11:08 上午
//**/
//@Configuration
//public class ElasticJobConfig {
//
//    @Resource
//    private ZookeeperRegistryCenter zookeeperRegistryCenter;
//
//    /**
//     * Title:
//     *
//     * @param: [jobClass, cron, shardingTotalCount, shardingItemParameters:默认值]
//     * @author: teninone
//     */
//    public LiteJobConfiguration createJobConfiguration(final Class<? extends SimpleJob> jobClass, final String cron, final int shardingTotalCount) {
//        // 定义作业核心配置
//        final JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount).build();
////                shardingItemParameters(shardingItemParameters).build();
//
//        // 定义SIMPLE类型配置
//        final SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, jobClass.getCanonicalName());
//
//        // 定义Lite作业根配置
//        return LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
//    }
//
//    /**
//      * 定时更新企业订单状态
//    **/
//    @Bean(initMethod = "init")
//    public JobScheduler updateOrderStatusJobScheduler(SpringBootSimpleJob springBootSimpleJob,
//                                                      @Value("${elastic-job.cron.simpleJob}") String cron,
//                                                      @Value("${elastic-job.shardingTotalCount}") int shardingTotalCount) {
//
//        final LiteJobConfiguration liteJobConfiguration = createJobConfiguration(springBootSimpleJob.getClass(), cron, shardingTotalCount);
//
//        return new SpringJobScheduler(springBootSimpleJob, zookeeperRegistryCenter, liteJobConfiguration, new ElasticJobSimpleListener());
//    }
//
//
//}
