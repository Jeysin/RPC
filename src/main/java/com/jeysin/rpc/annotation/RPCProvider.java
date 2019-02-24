package com.jeysin.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Jeysin
 * @Date: 2019/2/24 11:15
 * @Desc:
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RPCProvider {

    Class<?> serviceInterface() default Object.class;

    String serviceGroup() default "RPC";

    String serviceVersion() default "1.0.0.DAILY";

    String host() default "localhost";

    /**
     * 支持将不同的服务发布在同一个端口，也支持将不同的服务发布在不同的端口
     * @return
     */
    int port() default 8090;

    int clientTimeout() default 3000;
}
