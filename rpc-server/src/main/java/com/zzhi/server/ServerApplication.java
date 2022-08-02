package com.zzhi.server;

import com.zzhi.server.impl.NettyServer;
import com.zzhi.service.UserService;
import com.zzhi.service.impl.BrandServiceImpl;
import com.zzhi.service.impl.UserServiceImpl;

/**
 * 服务器应用程序
 *
 * @author zzhi
 * @date 2022/08/02
 */
public class ServerApplication {

    public static void main(String[] args) {
        //保存服务
        ServiceProvider serviceProvider = new ServiceProvider();

        serviceProvider.providerServiceInterface(new UserServiceImpl());
        serviceProvider.providerServiceInterface(new BrandServiceImpl());

        Server server = new NettyServer(serviceProvider);

        server.start(8899);
    }
}
