package com.zzhi.client.impl;

import com.zzhi.client.Client;
import com.zzhi.enums.SerializerType;
import com.zzhi.serializer.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
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
    private final String host;
    private final int port;
    private static final Bootstrap BOOTSTRAP;

    static {
        log.info("开始初始化bootstrap");
        BOOTSTRAP = new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup())
                .handler(new ChannelInitializer<NioSocketChannel>() {
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
                                        Serializer.getSerializerByCode(SerializerType.JSON_SERIALIZER.getType())));
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
            channel.closeFuture().sync();
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
