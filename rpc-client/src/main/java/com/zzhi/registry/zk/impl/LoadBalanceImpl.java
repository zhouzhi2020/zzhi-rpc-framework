package com.zzhi.registry.zk.impl;

import com.zzhi.registry.LoadBalance;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

/**
 * 负载平衡的实现类
 *
 * @author zzhi
 * @date 2022/08/04
 */
@Slf4j
public class LoadBalanceImpl implements LoadBalance {
    @Override
    public InetSocketAddress loadBalanceByList(List<InetSocketAddress> selectList) {
        int size = selectList.size();
        int select = new Random().nextInt(size);
        log.info("选择了第 {} 个 服务器, 地址为 {}, 消费服务", select, selectList.get(select).toString());
        return selectList.get(select);
    }
}
