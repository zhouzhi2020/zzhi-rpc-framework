package com.zzhi.client.impl;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import com.zzhi.vo.RpcResponse;

/**
 * Netty客户端处理程序
 *
 * @author zzhi
 * @date 2022/08/02
 */
@Slf4j
public class NettyClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("接受到了响应");
        log.info("响应为 ===> {}", msg);
        AttributeKey<RpcResponse> attributeKey = AttributeKey.valueOf("RpcResponse");
        ctx.channel().attr(attributeKey).set((RpcResponse) msg);
        ctx.channel().close();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("发生异常");
        cause.printStackTrace();
        ctx.close();
    }
}
