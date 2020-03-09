package com.xmutca.feign.client.demo;

import com.xmutca.feign.client.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020-03-06
 */
@FeignClient(value = "feign-server", fallback = FeignTestFallback.class)
public interface FeignTest {

    /**
     * 业务错误
     * @return
     */
    @RequestMapping("/businessErr")
    Result businessErr();

    /**
     * fallback错误
     * @return
     */
    @RequestMapping("/fallbackErr")
    Result fallbackErr();
}
