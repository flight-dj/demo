//package com.test.demo.controller;
//
//import com.test.demo.service.DeferredResultEntity;
//import com.test.demo.service.DeferredResultResponse;
//import com.test.demo.service.DeferredResultService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.context.request.async.DeferredResult;
//
///**
// * @Description:
// * @Author: dajun
// * @Date: 2020/11/13 11:55 上午
// **/
//@RestController
//public class DeferredController {
//
//    @Autowired
//    private DeferredResultService deferredResultService;
//
//    /**
//     * 为了方便测试，简单模拟一个
//     * 多个请求用同一个requestId会出问题
//     */
//    private final String requestId = "haha";
//
//    @GetMapping(value = "/get")
//    public DeferredResult<DeferredResultResponse> get(@RequestParam(value = "timeout", required = false, defaultValue = "10000") Long timeout) {
//        DeferredResult<DeferredResultResponse> deferredResult = new DeferredResult<>(timeout);
//
//        deferredResultService.process(requestId, deferredResult);
//
//        return deferredResult;
//    }
//
//    /**
//     * 设置DeferredResult对象的result属性，模拟异步操作
//     * @param desired
//     * @return
//     */
//    @GetMapping(value = "/result")
//    public String settingResult(@RequestParam(value = "desired", required = false, defaultValue = "成功") String desired) {
//        DeferredResultResponse deferredResultResponse = new DeferredResultResponse();
//        if (DeferredResultResponse.Msg.SUCCESS.getDesc().equals(desired)){
//            deferredResultResponse.setCode(HttpStatus.OK.value());
//            deferredResultResponse.setMsg(desired);
//        }else{
//            deferredResultResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            deferredResultResponse.setMsg(DeferredResultResponse.Msg.FAILED.getDesc());
//        }
//        deferredResultService.settingResult(requestId, deferredResultResponse);
//
//        return "Done";
//    }
//
//    @Autowired
//    private DeferredResultEntity deferredResultEntity;
//
//    @GetMapping("/deferred")
//    public DeferredResult<Object> testDeferredResult() {
//        System.out.println("主线程开始！");
//        DeferredResult<Object> result = new DeferredResult<Object>();
//        deferredResultEntity.setResult(result);
//        deferredResultEntity.setFlag(true);
//        System.out.println("主线程结束！");
//        return result;
//    }
//
//}
