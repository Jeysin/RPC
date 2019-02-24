package com.jeysin.rpc.consumer;


import java.lang.reflect.Proxy;

/**
 * @Author: Jeysin
 * @Date: 2019/2/24 11:11
 * @Desc: 在这里生成jdk动态代理类
 */

public class ServiceProxy {

    public static <T> T getProxy(Class<T> interfaceCls, String host, Integer port, String serviceGroup, String serviceVersion, Integer clientTimeout){
        ServiceProxyInvocationHandler serviceProxyInvocationHandler = new ServiceProxyInvocationHandler(host, port, interfaceCls.getName(), serviceGroup, serviceVersion, clientTimeout);
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class<?>[]{interfaceCls}, serviceProxyInvocationHandler);
    }
}
