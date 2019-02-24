package com.jeysin.rpc.provider;

import com.jeysin.rpc.dto.Metadata;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * @Author: Jeysin
 * @Date: 2019/2/24 11:12
 * @Desc:
 */

public class ServiceListenCenter implements Runnable{
    private Map<String, Object> serviceMap;

    private ServerSocket serverSocket;

    public ServiceListenCenter(Map<String, Object> serviceMap, ServerSocket serverSocket){
        this.serviceMap = serviceMap;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while(true) {
            Socket socket = null;
            ObjectInputStream objectInputStream = null;
            ObjectOutputStream objectOutputStream = null;
            try {
                socket = serverSocket.accept();
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                Metadata metadata = (Metadata) objectInputStream.readObject();
                Object object = invoke(metadata);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                try {
                    if(socket != null){
                        socket.close();
                    }
                    if(objectInputStream != null){
                        objectInputStream.close();
                    }
                    if(objectOutputStream != null){
                        objectOutputStream.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private Object invoke(Metadata metadata) throws Exception{
        String serviceKey = metadata.getInterfaceName()+":"+metadata.getServiceGroup()+":"+metadata.getServiceVersion()+":"+serverSocket.getLocalPort();
        Object target = serviceMap.get(serviceKey);
        if(target == null){
            System.out.println("ServiceListenCenter: Error! service haven't register, serviceKey is '"+serviceKey+"'");
            return null;
        }

        Object[] args = metadata.getArgs();
        Class<?>[] types = new Class<?>[args.length];
        for(int i = 0; i < args.length; ++i){
            types[i] = args[i].getClass();
        }
        Method method = target.getClass().getMethod(metadata.getMethodName(), types);
        if(method == null){
            System.out.println("ServiceListenCenter: Error! cannot get Method, serviceKey is '"+serviceKey+"', method is "+method.getName());
            return null;
        }
        Object result = method.invoke(target, args);
        System.out.println("ServiceListenCenter: Successful! service:"+metadata.getInterfaceName()+"#"+metadata.getMethodName());
        return result;
    }
}
