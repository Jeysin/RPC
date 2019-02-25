# RPC Framework
[![Author](https://img.shields.io/badge/author-@Jeysin-blue.svg?style=flat)](http://www.cnblogs.com/jeysin/) 
[![Platform](https://img.shields.io/badge/platform-Linux/Windows-green.svg?style=flat)](https://github.com/Jeysin/searchEngine)

## 简介
Java语言编写的一个轻量级的RPC框架，基于TCP协议，与SpringBoot集成，让远程调用像本地调用一样便捷。

## 特性
* 与SpringBoot无缝衔接，实现RPC调用过程对用户无感知。并基于Java的注解与反射机制，实现对业务代码无侵入性的RPC调用。
* 服务的发布端底层利用线程池，可实现将不同的服务发布在不同的端口以供调用，当然了，更常用的方式是将不同的服务发布在同一个端口。
* 服务的调用端底层采用JDK动态代理技术，对用户屏蔽发起远程调用的细节，让远程调用像本地调用一样简单。

## 用法示例
1. 下载编译，打成jar包，放入本地maven库中。
2. 在服务发布端和调用端的pom.xml文件中都添加如下依赖：
``` xml
<dependency>
    <groupId>com.jeysin</groupId>
    <artifactId>RPC</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
3. 定义一个需要发布的接口，例如：
``` java
package com.jeysin;

public interface HelloWorldService {
    String sayHello(String name);
}
```
4. 在服务的发布端实现这个接口，并使用@RPCProvider注解发布这个服务：
``` java
package com.jeysin.impl;

import com.jeysin.HelloWorldService;
import com.jeysin.rpc.annotation.RPCProvider;

/**
 * @Author: Jeysin
 * @Date: 2019/2/24 21:54
 * @Desc:
 */

@RPCProvider(serviceInterface = HelloWorldService.class, serviceGroup = "RPC", serviceVersion = "1.0.0.DAILY", clientTimeout = 3000)
public class HelloWorldServiceImpl implements HelloWorldService {
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
```
5. 在服务的调用端利用@RPCConsumer注解实现服务的调用：
``` java
package com.jeysin.controller;

import com.jeysin.HelloWorldService;
import com.jeysin.rpc.annotation.RPCConsumer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Jeysin
 * @Date: 2019/2/24 22:07
 * @Desc:
 */

@RestController
public class ConsumerController {

    @RPCConsumer(serviceGroup = "RPC", serviceVersion = "1.0.0.DAILY", host = "localhost", port = 8090, clientTimeout = 3000)
    private HelloWorldService helloWorldService;

    @RequestMapping(value = "/sayhello", method = RequestMethod.GET)
    String sayHello(@RequestParam String name){
        //发起远程调用
        return helloWorldService.sayHello(name);
    }
}
```

## 关于作者

+ Email: `jeysin@qq.com`
+ QQ: `2214744822`
+ WeChat: `jiaxian_jiang`
+ Blog: [http://www.cnblogs.com/jeysin/](http://www.cnblogs.com/jeysin/)
