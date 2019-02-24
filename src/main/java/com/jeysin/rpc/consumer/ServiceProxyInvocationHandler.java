package com.jeysin.rpc.consumer;

import com.jeysin.rpc.dto.Metadata;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @Author: Jeysin
 * @Date: 2019/2/24 11:11
 * @Desc:
 */

public class ServiceProxyInvocationHandler implements InvocationHandler {

    private String host;

    private Integer port;

    private String serviceInterface;

    private String serviceGroup;

    private String serviceVersion;

    private Integer clientTimeout;

    public ServiceProxyInvocationHandler(String host, Integer port, String serviceInterface, String serviceGroup, String serviceVersion, Integer clientTimeout){
        this.host = host;
        this.port = port;
        this.serviceInterface = serviceInterface;
        this.serviceGroup = serviceGroup;
        this.serviceVersion = serviceVersion;
        this.clientTimeout = clientTimeout;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建Metadata
        Metadata metadata = new Metadata();
        metadata.setInterfaceName(serviceInterface);
        metadata.setServiceGroup(serviceGroup);
        metadata.setServiceVersion(serviceVersion);
        metadata.setMethodName(method.getName());
        metadata.setArgs(args);

        /**
         * 发网络请求
         * TODO: 超时返回功能(clientTimeout)
         */
        Socket socket = null;
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try{
            socket = new Socket(host, port);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(metadata);

            objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object obj = objectInputStream.readObject();
            if(null == obj){
                System.out.println("RPC failure, serviceInterface:" + this.serviceInterface
                        + ", serviceGroup:" + this.serviceGroup
                        + ", serviceVersion" + this.serviceVersion);
            }
            return obj;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                if(socket != null){
                    socket.close();
                }
                if(objectInputStream != null){
                    objectInputStream.close();
                }
                if(objectOutputStream != null){
                    objectOutputStream.close();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
