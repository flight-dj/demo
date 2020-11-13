//package com.test.demo.job;
//
//import com.dangdang.ddframe.job.api.ShardingContext;
//import com.dangdang.ddframe.job.api.simple.SimpleJob;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Component
//public class SpringBootSimpleJob implements SimpleJob {
//
//    private final Logger logger = LoggerFactory.getLogger(SpringBootSimpleJob.class);
//
//    @Override
//    public void execute(ShardingContext shardingContext) {
//
//        System.out.println("============test========" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//    }
//}
