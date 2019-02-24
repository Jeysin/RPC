package com.jeysin.rpc.provider;

import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Jeysin
 * @Date: 2019/2/24 11:13
 * @Desc: 服务的统一注册中心，每个端口由线程池里单独的线程负责监听
 */

public class ServiceRegisterCenter {

    private static ServiceRegisterCenter serviceRegisterCenter = new ServiceRegisterCenter();

    /**
     * 所有被发布的bean都注册在这里，serviceKey值为serviceInterface:serviceGroup:serviceVersion:port
     * 之所以这样设置serviceKey是为了支持不同的服务可以发布在不同的端口
     * 由于有多个线程并发访问这个map，所以用ConcurrentHashMap
     */
    private Map<String, Object> serviceMap = new ConcurrentHashMap<String, Object>();

    /**
     * 记录下所有被监听的端口
     */
    private Set<Integer> portSet = new HashSet<Integer>();

    /**
     * 线程池
     */
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public static ServiceRegisterCenter getInstance(){
        return serviceRegisterCenter;
    }

    public void registerAndPublishService(String serviceInterface, String serviceGroup, String serviceVersion, String host, Integer port, Object bean){
        //校验服务是否已经被发布
        String serviceKey = serviceInterface + ":" + serviceGroup + ":" + serviceVersion + ":" + port;
        if(serviceMap.get(serviceKey) != null){
            System.out.println("ServiceRegisterCenter: Error! service can't be published repeatedly! serviceKey is '" + serviceKey + "'");
            return;
        }
        //注册服务
        serviceMap.put(serviceKey, bean);

        //如果端口还没有被监听，开启监听
        if(!portSet.contains(port)){
            ServerSocket serverSocket = null;
            try{
                serverSocket = new ServerSocket(port);
            } catch (Exception e){
                e.printStackTrace();
            }
            executorService.execute(new ServiceListenCenter(serviceMap, serverSocket));
            portSet.add(port);
            System.out.println("ServiceRegisterCenter: port " + port + " is listening");
        }
    }
}
