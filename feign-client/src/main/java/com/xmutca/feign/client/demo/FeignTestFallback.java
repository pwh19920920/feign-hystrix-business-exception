package com.xmutca.feign.client.demo;

import com.xmutca.feign.client.Result;
import org.springframework.stereotype.Component;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020-03-06
 */
@Component
public class FeignTestFallback implements FeignTest {

    @Override
    public Result fallbackErr() {
        return new Result("返回fallback");
    }

    /**
     * 发生业务异常不调用，超时会调用
     * @return
     */
    @Override
    public Result businessErr() {
        return new Result("timeout-fallback");
    }
}
