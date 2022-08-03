package com.zzhi.client;

import com.zzhi.client.impl.NettyClient;
import com.zzhi.client.proxy.ClientProxy;
import com.zzhi.entity.Brand;
import com.zzhi.entity.User;
import com.zzhi.registry.ServiceDiscovery;
import com.zzhi.registry.zk.impl.ServiceDiscoveryImpl;
import com.zzhi.service.BrandService;
import com.zzhi.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端应用程序
 *
 * @author zzhi
 * @date 2022/08/02
 */
@Slf4j
public class ClientApplication {
    public static void main(String[] args) {
        ServiceDiscovery serviceDiscovery = new ServiceDiscoveryImpl();
        Client client = new NettyClient();
        ClientProxy clientProxy = new ClientProxy(client);

        UserService userService = clientProxy.getProxyInstance(UserService.class);
        BrandService brandService = clientProxy.getProxyInstance(BrandService.class);

        User user = userService.getUserById(10);

        log.info("客户端应用成功远程调用 ===> {}", user);

        Brand brand = brandService.getBrandById(20);

        log.info("客户端应用成功远程调用 ===> {}", brand);
    }
}
