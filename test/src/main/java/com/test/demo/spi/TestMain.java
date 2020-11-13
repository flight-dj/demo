package com.test.demo.spi;

import java.util.ServiceLoader;

/**
 * @Description:
 * @Author: dajun
 * @Date: 2020/9/21 2:18 下午
 **/
public class TestMain {
    public static void main(String[] args) {

        ServiceLoader<DogService> loads = ServiceLoader.load(DogService.class);
        loads.forEach(item -> {
            item.test();
        });
    }
}
