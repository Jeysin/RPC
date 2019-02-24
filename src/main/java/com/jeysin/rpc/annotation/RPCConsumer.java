package com.jeysin.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Jeysin
 * @Date: 2019/2/24 11:14
 * @Desc:
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RPCConsumer {

    String serviceGroup() default "PRC";

    String serviceVersion() default "1.0.0.DAILY";

    /**
     * 由于没有独立的服务发现中心，不能从服务发现中心获取IP和端口，所以这里要指明主机和端口，然后才能发起RPC调用
     * @return
     */
    String host() default "localhost";

    int port() default 8090;

    int clientTimeout() default 3000;
}
