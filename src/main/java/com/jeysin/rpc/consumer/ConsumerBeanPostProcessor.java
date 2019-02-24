package com.jeysin.rpc.consumer;

import com.jeysin.rpc.annotation.RPCConsumer;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @Author: Jeysin
 * @Date: 2019/2/24 11:10
 * @Desc:
 */

@Component
public class ConsumerBeanPostProcessor implements BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for(Field declaredField : declaredFields){
            declaredField.setAccessible(true);
            if(declaredField.isAnnotationPresent(RPCConsumer.class)){
                //先判断一下这个字段类型是否是个接口类型，如果不是直接抛异常，因为RPCConsumer必须注解在接口上
                if(!declaredField.getType().isInterface()){
                    throw new BeanCreationException("RPCConsumer must be used at interface");
                }
                RPCConsumer anno = (RPCConsumer) declaredField.getAnnotation(RPCConsumer.class);
                //给其注入一个代理对象
                try{
                    Object objectPorxy = ServiceProxy.getProxy(declaredField.getType(), anno.host(), anno.port(), anno.serviceGroup(), anno.serviceVersion(), anno.clientTimeout());
                    declaredField.set(bean, objectPorxy);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
