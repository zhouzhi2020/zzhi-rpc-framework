package com.zzhi.registry;

import java.net.InetSocketAddress;

/**
 * 服务发现
 *
 * @author zzhi
 * @date 2022/08/03
 */
public interface ServiceDiscovery {
    /**
     * 查找服务
     *
     * @param serviceName 服务名称
     * @return {@link InetSocketAddress}
     */
    InetSocketAddress lookupService(String serviceName);
}
