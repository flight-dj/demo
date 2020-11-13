package com.test.demo.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

//@Primary
@Component
public class Service2Impl implements Service{
    @Override
    public void print() {
        System.out.println("print2");
    }
}
