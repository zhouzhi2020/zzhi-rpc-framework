package com.zzhi.client.impl;

import com.zzhi.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Netty解码处理器
 *
 * @author zzhi
 * @date 2022/08/02
 */
@Slf4j
public class NettyDecodeHandler extends ByteToMessageDecoder {
    /**
     * 解码
     *
     * @param ctx     上下文
     * @param byteBuf 字节缓冲区
     * @param list    解码出来的对象存放在列表中
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) {
        if (!byteBuf.isReadable()) {
            return;
        }
        // 读取数据类型
        int messageType = byteBuf.readInt();
        // 读取序列化器编号
        short serializerType = byteBuf.readShort();

        Serializer serializer = Serializer.getSerializerByCode(serializerType);
        if (serializer == null) {
            log.error("不存在对应的编码器");
            throw new RuntimeException();
        }

        // 读取数据长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];

        byteBuf.readBytes(bytes);

        Object obj = serializer.deserialize(bytes, messageType);
        list.add(obj);
    }
}
