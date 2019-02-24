package com.jeysin.rpc.dto;

import java.io.Serializable;

/**
 * @Author: Jeysin
 * @Date: 2019/2/24 11:12
 * @Desc: 网络中传输的包含调用信息的类
 */

public class Metadata implements Serializable{

    private static final long serialVersionUID = 4431289252498287529L;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数
     */
    private Object[] args;

    /**
     * 服务组
     */
    private String serviceGroup;

    /**
     * 服务版本
     */
    private String serviceVersion;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }
}
