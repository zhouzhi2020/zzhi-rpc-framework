package com.zzhi.registry;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 负载平衡
 *
 * @author zzhi
 * @date 2022/08/04
 */
public interface LoadBalance {
    /**
     * 通过列表负载平衡
     *
     * @param selectList 选择列表
     * @return {@link InetSocketAddress}
     */
    InetSocketAddress loadBalanceByList(List<InetSocketAddress> selectList);

}
