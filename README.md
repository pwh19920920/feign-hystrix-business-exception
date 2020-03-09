# feign-hystrix-business-exception
SpringCloud项目中，业务异常不被计入熔断度量、不被执行fallback流程处理实战

##一、实战原理
主要实现依赖hystrix对于HystrixBadRequestException异常的处理，以下是官方原文：

---

All exceptions thrown from the run() method except for HystrixBadRequestException count as failures and trigger getFallback() and circuit-breaker logic.

You can wrap the exception that you would like to throw in HystrixBadRequestException and retrieve it via getCause(). The HystrixBadRequestException is intended for use cases such as reporting illegal arguments or non-system failures that should not count against the failure metrics and should not trigger fallback logic.

| Failure Type | Exception class | Exception.cause | subject to fallback |
| --- | --- | --- | --- |
| FAILURE | `HystrixRuntimeException` | underlying exception (user-controlled) | YES |
| TIMEOUT | `HystrixRuntimeException` | `j.u.c.TimeoutException` | YES |
| SHORT_CIRCUITED | `HystrixRuntimeException` | `j.l.RuntimeException` | YES |
| THREAD\_POOL\_REJECTED | `HystrixRuntimeException` | `j.u.c.RejectedExecutionException` | YES |
| SEMAPHORE_REJECTED | `HystrixRuntimeException` | `j.l.RuntimeException` | YES |
| BAD_REQUEST | `HystrixBadRequestException` | underlying exception (user-controlled) | NO |

---

简单翻译下就是，发生非HystrixBadRequestException异常时将会执行失败并触发服务降级处理逻辑，参数异常以及非系统异常不应计入故障度量以及服务降级处理，你可以包装你的异常cause到HystrixBadRequestException中， 并附上5中会被fallback的失败类型。
> 1. FAILURE：执行失败，抛出异常。
> 2. TIMEOUT：执行超时。
> 3. SHORT_CIRCUITED：断路器打开。
> 4. THREAD_POOL_REJECTED：线程池拒绝。
> 5. SEMAPHORE_REJECTED：信号量拒绝。

##二、核心要点
1. BaseException需要继承HystrixBadRequestException
2. Feign的ErrorDecoder需要对业务异常的HttpStatus进行定义
```java
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
```