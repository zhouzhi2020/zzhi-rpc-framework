package com.zzhi.server.impl;

import com.zzhi.enums.SerializerType;
import com.zzhi.serializer.Serializer;
import com.zzhi.server.Server;
import com.zzhi.server.ServiceProvider;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;


/**
 * Netty服务器
 *
 * @author zzhi
 * @date 2022/08/02
 */
@Slf4j
public class NettyServer implements Server {

    private final ServiceProvider serviceProvide;

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
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("lenBaseDec", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 6, 4, 0, 0));
                        pipeline.addLast(
                                "dec",
                                new NettyDecodeHandler());
                        pipeline.addLast(
                                "enc",
                                new NettyEncodeHandler(
                                        Serializer.getSerializerByCode(SerializerType.KRYO_SERIALIZER.getType())));
                        pipeline.addLast("serverHandler", new ServerHandler(serviceProvide));
                    }
                });
        log.info("服务器启动成功 ...");
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            Channel channel = channelFuture.channel();
            channel.closeFuture().sync();

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
