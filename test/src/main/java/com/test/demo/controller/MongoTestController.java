package com.test.demo.controller;//package com.test.demo;
//
//import com.test.demo.dao.MongoTest;
//import com.test.demo.dao.MongoTestDao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class MongoTestController {
//
//    @Autowired
//    private MongoTestDao mtdao;
//
//    @GetMapping(value="/test1")
//    public void saveTest() throws Exception {
//        for (int i=0; i < 100; i++) {
//            MongoTest mgtest=new MongoTest();
//            mgtest.setId(i);
//            mgtest.setAge(i);
//            mgtest.setName(i+"");
//            mtdao.saveTest(mgtest);
//        }
//
//    }
//
//    @GetMapping(value="/test2")
//    public MongoTest findTestByName(){
//        MongoTest mgtest= mtdao.findTestByName("ceshi");
//        System.out.println("mgtest is "+mgtest);
//        return mgtest;
//    }
//
//    @GetMapping(value="/test3")
//    public void updateTest(){
//        MongoTest mgtest=new MongoTest();
//        mgtest.setId(11);
//        mgtest.setAge(44);
//        mgtest.setName("ceshi2");
//        mtdao.updateTest(mgtest);
//    }
//
//    @GetMapping(value="/test4")
//    public void deleteTestById(){
//        mtdao.deleteTestById(11);
//    }
//
//}