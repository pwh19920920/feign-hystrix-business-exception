package com.xmutca.feign.client.demo;

import com.xmutca.feign.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020-03-06
 */
@RestController
@RequestMapping
public class RestTestController {

    @Autowired
    private FeignTest feignTest;

    /**
     * 业务异常调用
     * @return
     */
    @RequestMapping("/businessErr")
    public Result businessErr() {
        return feignTest.businessErr();
    }

    /**
     * fallback回调
     * @return
     */
    @RequestMapping("/fallbackErr")
    public Result fallbackErr() {
        return feignTest.fallbackErr();
    }
}
