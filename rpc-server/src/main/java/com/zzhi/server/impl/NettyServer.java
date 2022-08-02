package com.zzhi.server.impl;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.zzhi.server.Server;
import com.zzhi.server.ServiceProvider;
import com.zzhi.service.UserService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Netty服务器
 *
 * @author zzhi
 * @date 2022/08/02
 */
@Slf4j
public class NettyServer implements Server {
    private UserService service;
    private ServiceProvider serviceProvide;
    public NettyServer(UserService userService) {
        this.service = userService;
    }

    public NettyServer(ServiceProvider serviceProvide) {
        this.serviceProvide = serviceProvide;
    }



    @Override
    public void start(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("dec1", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0 ,4));
                        pipeline.addLast("enc1", new LengthFieldPrepender(4));

                        pipeline.addLast("enc2", new ObjectEncoder());
                        pipeline.addLast("dec2", new ObjectDecoder(new ClassResolver() {
                            @Override
                            public Class<?> resolve(String s) throws ClassNotFoundException {
                                return Class.forName(s);
                            }
                        }));
                        pipeline.addLast("serverHandler", new ServerHandler(serviceProvide));
                    }
                });
        log.info("服务器启动成功 ...");
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            Channel channel = channelFuture.channel();
            ChannelFuture closeFuture = channel.closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {

    }
}
