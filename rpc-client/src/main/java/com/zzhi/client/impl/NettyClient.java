package com.zzhi.client.impl;

import com.zzhi.client.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import com.zzhi.vo.RpcRequest;
import com.zzhi.vo.RpcResponse;

/**
 * Netty客户端实现
 *
 * @author zzhi
 * @date 2022/08/01
 */
@Slf4j
public class NettyClient implements Client {
    private String host;
    private int port;
    private static final Bootstrap BOOTSTRAP;

    static {
        log.info("开始初始化bootstrap");
        BOOTSTRAP = new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup())
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(
                                "dec1",
                                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        pipeline.addLast("enc1", new LengthFieldPrepender(4));
                        pipeline.addLast("enc2", new ObjectEncoder());
                        pipeline.addLast("de2", new ObjectDecoder(new ClassResolver() {
                            @Override
                            public Class<?> resolve(String s) throws ClassNotFoundException {
                                return Class.forName(s);
                            }
                        }));

                        //添加客户端处理器
                        pipeline.addLast("clientHandler", new NettyClientHandler());


                    }
                });
    }

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        log.info("开始发送 request");
        try {
            ChannelFuture channelFuture = BOOTSTRAP.connect(host, port).sync();
            log.info("和服务器连接成功！");
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(request);
            ChannelFuture closeFuture = channel.closeFuture().sync();
            AttributeKey<RpcResponse> attributeKey = AttributeKey.valueOf("RpcResponse");
            RpcResponse response = channel.attr(attributeKey).get();
            log.info("sendRequest 方法获得响应 ===> {}", response);
            return response;

        } catch (InterruptedException e) {
            log.error("连接异常 ===> {}", e.toString());
            throw new RuntimeException(e);
        }
    }
}
