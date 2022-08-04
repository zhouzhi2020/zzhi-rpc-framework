package com.zzhi.registry.zk.impl;

import com.zzhi.registry.LoadBalance;
import com.zzhi.registry.ServiceDiscovery;
import com.zzhi.registry.zk.utils.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 服务发现的实现类
 *
 * @author zzhi
 * @date 2022/08/03
 */
public class ServiceDiscoveryImpl implements ServiceDiscovery {
    /**
     * 负载平衡策略
     */
    private final LoadBalance loadBalance = new LoadBalanceImpl();
    @Override
    public InetSocketAddress lookupService(String serviceName) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<InetSocketAddress> addressByServiceName = CuratorUtils.getAddressByServiceName(zkClient, serviceName);
        InetSocketAddress targetAddress = loadBalance.loadBalanceByList(addressByServiceName);
        return targetAddress;
    }
}
