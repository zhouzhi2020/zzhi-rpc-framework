package com.zzhi.client.impl;

import com.zzhi.enums.MessageType;
import com.zzhi.serializer.Serializer;
import com.zzhi.vo.RpcRequest;
import com.zzhi.vo.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * Netty编码处理器
 *
 * @author zzhi
 * @date 2022/08/02
 */
@AllArgsConstructor
@Slf4j
public class NettyEncodeHandler extends MessageToByteEncoder {
    private Serializer serializer;

    /**
     * 编码
     *
     * @param ctx     上下文
     * @param msg       数据对象
     * @param byteBuf 字节缓冲区
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf byteBuf) {
        if (!byteBuf.isWritable()) {
            return;
        }
        log.info("===============客户端开始编码===============");
        log.info("要处理的消息类是 ===> {}", msg);
        // 写入魔数， 这里定义为消息类型
        if (msg instanceof RpcRequest) {
            byteBuf.writeInt(MessageType.REQUEST.getType());
        } else if (msg instanceof RpcResponse) {
            byteBuf.writeInt(MessageType.RESPONSE.getType());
        }
        // 写入序列化方式编号
        byteBuf.writeShort(serializer.getType());
        // 用序列化器编码
        byte[] bytes = serializer.serialize(msg);
        // 先写长度
        log.info("客户端写入该数据长度为 ===> {}", bytes.length);
        byteBuf.writeInt(bytes.length);
        // 再写数据
        byteBuf.writeBytes(bytes);
        log.info("编码后为 ===> {}", byteBuf.toString(Charset.defaultCharset()));
    }
}
