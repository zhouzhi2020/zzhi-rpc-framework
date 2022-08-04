package com.zzhi.server;

import com.zzhi.registry.ServiceRegistry;
import com.zzhi.registry.zk.impl.ServiceRegistryImpl;
import com.zzhi.server.impl.NettyServer;
import com.zzhi.service.BrandService;
import com.zzhi.service.UserService;
import com.zzhi.service.impl.BrandServiceImpl;
import com.zzhi.service.impl.UserServiceImpl;

import java.net.InetSocketAddress;

/**
 * 服务器应用程序
 *
 * @author zzhi
 * @date 2022/08/02
 */
public class ServerApplication {

    public static void main(String[] args) {
        int port = 8899;
        UserService userService = new UserServiceImpl();
        BrandService brandService = new BrandServiceImpl();
        //保存服务
        ServiceProvider serviceProvider = new ServiceProvider(port);

        serviceProvider.providerServiceInterface(userService);
        serviceProvider.providerServiceInterface(brandService);

        Server server = new NettyServer(serviceProvider);

        server.start(port);
    }
}
