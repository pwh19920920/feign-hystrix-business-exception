package com.xmutca.feign.client;

/**
 * 业务异常
 * @author Peter
 */
public class ServiceException extends BaseException {

    public static final Integer STATUS = 400;

    public ServiceException(String message) {
        super(message);
    }

    @Override
    public Integer getStatus() {
        return STATUS;
    }
}
