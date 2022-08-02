package com.zzhi.server;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务提供者
 *
 * @author zzhi
 * @date 2022/08/02
 */
public class ServiceProvider {
    /**
     * 通过接口找实现类
     */
    private Map<String, Object> interfaceProvider;

    public ServiceProvider() {
        this.interfaceProvider = new HashMap<>();
    }

    /**
     * 为实现类添加对应的服务接口
     *
     * @param service 服务
     */
    public void providerServiceInterface(Object service){
        Class<?>[] interfaces = service.getClass().getInterfaces();

        for (Class<?> clazz : interfaces) {
            interfaceProvider.put(clazz.getName(), service);
        }
    }

    /**
     * 得到服务实现类
     *
     * @param interfaceName 接口名称
     * @return {@link Object}
     */
    public Object getService(String interfaceName) {
        return interfaceProvider.getOrDefault(interfaceName, null);
    }
}
