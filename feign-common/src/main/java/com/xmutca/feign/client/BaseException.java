package com.xmutca.feign.client;

import com.netflix.hystrix.exception.HystrixBadRequestException;

/**
 * 基本的异常处理
 * @author Peter
 */
public abstract class BaseException extends HystrixBadRequestException {

    public BaseException(String message) {
        super(message);
    }

    /**
     * 获取回执
     * @return
     */
    public Receipt getExceptionResult() {
        return new Receipt(getStatus(), getMessage());
    }

    /**
     * 获取真正的状态
     * @return
     */
    public abstract Integer getStatus();

    /**
     * 获取回执
     * @return
     */
    public static BaseException getInstance(Result receipt) {
        return new BaseException(receipt.getMessage()) {
            @Override
            public Integer getStatus() {
                return receipt.getStatus();
            }
        };
    }
}
