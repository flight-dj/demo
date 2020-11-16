package demo.controller;

import com.test.demo.service.SentinelTestService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class SentinelController {

//    @Value("${testStr}")
//    private String test;

    @Autowired
    private SentinelTestService sentinelTestService;

    @RequestMapping("/sentinel")
    @ResponseBody
    public String sentinel() {
        return sentinelTestService.sayHello("sentinel");
    }

}
