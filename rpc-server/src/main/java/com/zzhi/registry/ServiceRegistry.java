package com.zzhi.registry;

import java.net.InetSocketAddress;

/**
 * 服务注册中心
 *
 * @author zzhi
 * @date 2022/08/03
 */
public interface ServiceRegistry {
    /**
     * 注册服务
     * 注册服务到注册中心
     *
     * @param serviceName       服务名称
     * @param inetSocketAddress 远程服务地址
     */
    void registerService(String serviceName, InetSocketAddress inetSocketAddress);
}

