package com.zzhi.serializer.impl;

import com.zzhi.enums.SerializerType;
import com.zzhi.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * JDK序列化器
 * 利用 Java IO 对象 -> 字节数组
 * @author zzhi
 * @date 2022/08/02
 */
@Slf4j
public class ObjectSerializer implements Serializer {

    /**
     * 序列化
     *
     * @param obj obj
     * @return {@link byte[]}
     */
    @Override
    public byte[] serialize(Object obj) {
        // 字节数组流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            byte[] bytes = bos.toByteArray();
            // 关闭流
            oos.close();
            bos.close();
            return bytes;

        } catch (IOException e) {
            log.error("ObjectSerializer 中的 serialize 出错啦！ ...");
            throw new RuntimeException(e);
        }
    }

    /**
     * 反序列化
     *  基于java原生方法不需要用到消息类型参数
     * @param bytes       字节
     * @param messageType 消息类型
     * @return {@link Object}
     */
    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object object = ois.readObject();
            bis.close();
            ois.close();
            return object;

        } catch (IOException | ClassNotFoundException e) {
            log.error("ObjectSerializer 中的 deserialize 出错啦！ ...");
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getType() {
        return SerializerType.OBJECT_SERIALIZER.getType();
    }
}
