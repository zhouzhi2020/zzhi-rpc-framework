package com.zzhi.client;

import com.zzhi.client.impl.NettyClient;
import com.zzhi.client.proxy.ClientProxy;
import com.zzhi.entity.Brand;
import com.zzhi.entity.User;
import com.zzhi.registry.ServiceDiscovery;
import com.zzhi.registry.zk.impl.ServiceDiscoveryImpl;
import com.zzhi.service.BrandService;
import com.zzhi.service.UserService;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 客户端应用程序
 *
 * @author zzhi
 * @date 2022/08/02
 */
@Slf4j
public class ClientApplication {
    @SuppressWarnings("checkstyle:WhitespaceAfter")
    public static void main(String[] args) throws InterruptedException {
        Client client = new NettyClient();
        ClientProxy clientProxy = new ClientProxy(client);

        UserService userService = clientProxy.getProxyInstance(UserService.class);
        BrandService brandService = clientProxy.getProxyInstance(BrandService.class);

        CountDownLatch countDownLatch = new CountDownLatch(2);
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(1);
        Random random = new Random();
        nioEventLoopGroup.next().scheduleAtFixedRate((Runnable) () -> {
            User user = userService.getUserById(random.nextInt(1000));
            countDownLatch.countDown();
            log.info("客户端应用成功远程调用 ===> {}", user);
        }, 0,2L, TimeUnit.SECONDS);

        nioEventLoopGroup.next().scheduleAtFixedRate((Runnable) () -> {
            Brand brand = brandService.getBrandById(random.nextInt(1000));
            countDownLatch.countDown();
            log.info("客户端应用成功远程调用 ===> {}", brand);
        }, 0, 2L, TimeUnit.SECONDS);

        countDownLatch.await();
        nioEventLoopGroup.shutdownGracefully();
    }
}
