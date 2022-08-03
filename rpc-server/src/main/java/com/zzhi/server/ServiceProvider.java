package com.zzhi.server;

import com.zzhi.registry.ServiceRegistry;
import com.zzhi.registry.zk.impl.ServiceRegistryImpl;
import io.netty.channel.local.LocalAddress;

import java.net.InetSocketAddress;
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
    private ServiceRegistry serviceRegistry;
    private int port;

    public ServiceProvider(int port) {
        this.port = port;
        this.interfaceProvider = new HashMap<>();
        this.serviceRegistry = new ServiceRegistryImpl();
    }

    /**
     * 为实现类添加对应的服务接口
     *
     * @param service 服务
     */
    public void providerServiceInterface(Object service) {
        Class<?>[] interfaces = service.getClass().getInterfaces();

        for (Class<?> clazz : interfaces) {
            interfaceProvider.put(clazz.getName(), service);
            serviceRegistry.registerService(clazz.getName(), new InetSocketAddress("127.0.0.1", port));
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
