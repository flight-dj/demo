package com.test.demo.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.test.demo.service.SentinelTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
public class SentinelController {

//    @Value("${testStr}")
//    private String test;

    @Autowired
    private SentinelTestService sentinelTestService;

    @RequestMapping("/sentinel")
    @ResponseBody
    public String sentinel(){
        return sentinelTestService.sayHello("sentinel");
    }

}
