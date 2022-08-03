package com.zzhi.registry.zk.impl;

import com.zzhi.registry.ServiceRegistry;
import com.zzhi.registry.zk.utils.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * 服务注册具体实现
 *
 * @author zzhi
 * @date 2022/08/03
 */
public class ServiceRegistryImpl implements ServiceRegistry {
    @Override
    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient, serviceName, inetSocketAddress.toString());
    }
}
