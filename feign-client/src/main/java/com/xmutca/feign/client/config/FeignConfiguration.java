package com.xmutca.feign.client.config;

import com.alibaba.fastjson.JSON;
import com.xmutca.feign.client.BaseException;
import com.xmutca.feign.client.Result;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.InputStream;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-25
 */
@Configuration
public class FeignConfiguration {

    /**
     * 业务异常解码器
     * @return
     */
    @Bean
    public ErrorDecoder getErrorDecoder() {
        return new FeignErrorDecode();
    }


    public class FeignErrorDecode implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            // 这一部分的异常将会变成子系统的异常, 不会进入hystrix的fallback方法，将会进入ErrorFilter的过滤链路
            if (response.status() >= HttpStatus.BAD_REQUEST.value() && response.status() < HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                try {
                    InputStream is = response.body().asInputStream();
                    Result receipt = JSON.parseObject(IOUtils.toString(is), Result.class);
                    return BaseException.getInstance(receipt);
                } catch (Exception e) {
                }
            }

            // 这一部分会进入fallback
            return feign.FeignException.errorStatus(methodKey, response);
        }
    }
}
