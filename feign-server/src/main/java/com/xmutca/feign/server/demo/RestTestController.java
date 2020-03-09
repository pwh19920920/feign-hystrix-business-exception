package com.xmutca.feign.server.demo;

import com.xmutca.feign.client.Result;
import com.xmutca.feign.client.ServiceException;
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

    @RequestMapping("/businessErr")
    public Result businessErr() throws InterruptedException {
        System.out.println("请求了businessErr");
        throw new ServiceException("businessErr");
    }

    @RequestMapping("/fallbackErr")
    public Result fallbackErr() {
        System.out.println("请求了fallbackErr");
        throw new RuntimeException("fallbackErr");
    }
}
