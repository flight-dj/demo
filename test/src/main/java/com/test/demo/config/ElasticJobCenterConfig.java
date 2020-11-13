//package com.test.demo.config;
//
//import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
//import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
//  * @Description: ElasticJobCenterConfig
//  * @Author: dajun
//  * @Date: 2020/8/28 11:08 上午
//**/
//@Configuration
//@ConditionalOnExpression("'${elastic-job.server.list}'.length() > 0")
//public class ElasticJobCenterConfig {
//
//    @Bean(initMethod = "init")
//    public ZookeeperRegistryCenter regCenter(@Value("${elastic-job.server.list}") final String serverList, @Value("${elastic-job.server.namespace}") final String namespace) {
//        return new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList, namespace));
//    }
//
//}
