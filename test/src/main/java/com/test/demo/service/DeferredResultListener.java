package com.test.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DeferredResultListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DeferredResultEntity deferredResultEntity;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        new Thread(() -> {
            int count = 0;
            System.out.println("监听器");
            while (true) {
                if (deferredResultEntity.isFlag()) {
                    System.out.println("线程" + Thread.currentThread().getName() + "开始");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    deferredResultEntity.getResult().setResult("SUCCESS");
                    deferredResultEntity.setFlag(false);
                    break;
                } else {
                    System.out.println("第" + count + "检查");
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}