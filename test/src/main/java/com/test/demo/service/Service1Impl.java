package com.test.demo.service;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Service1Impl implements Service, DisposableBean {

    @Override
    public void print() {
        System.out.println("print1");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy");
    }
}
