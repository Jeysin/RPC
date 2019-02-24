package com.jeysin.rpc.provider;

import com.jeysin.rpc.annotation.RPCProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @Author: Jeysin
 * @Date: 2019/2/24 11:15
 * @Desc:
 */

@Component
public class ProviderBeanPostProcessor implements BeanPostProcessor {

    private ServiceRegisterCenter serviceRegisterCenter = ServiceRegisterCenter.getInstance();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
        if(!clazz.isAnnotationPresent(RPCProvider.class)){
            return bean;
        }

        RPCProvider annotation = (RPCProvider) clazz.getAnnotation(RPCProvider.class);

        //校验这个bean是否真的实现了RPCProvider注解里面的接口
        if(!annotation.serviceInterface().isAssignableFrom(bean.getClass())){
            throw new BeanCreationException(bean.getClass().getName() + " haven't implement " + annotation.serviceInterface());
        }

        //注册并发布服务
        serviceRegisterCenter.registerAndPublishService(annotation.serviceInterface().getName(), annotation.serviceGroup(), annotation.serviceVersion(),
                annotation.host(), annotation.port(), bean);

        return bean;
    }
}
