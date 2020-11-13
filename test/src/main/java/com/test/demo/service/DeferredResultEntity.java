package com.test.demo.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Component
public class DeferredResultEntity {

    /**
     * 后续监听器中用于判断
     */
    private boolean flag = false;

    /**
     * 用于存储DeferredResult
     */
    private DeferredResult<Object> result;

    public DeferredResult<Object> getResult() {
        return result;
    }

    public void setResult(DeferredResult<Object> result) {
        this.result = result;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}