package com.zzhi.serializer;

import com.zzhi.enums.SerializerType;
import com.zzhi.serializer.impl.JsonSerializer;
import com.zzhi.serializer.impl.KryoSerializer;
import com.zzhi.serializer.impl.ObjectSerializer;

/**
 * 序列化器
 *
 * @author zzhi
 * @date 2022/08/02
 */
public interface Serializer {

    /**
     * 序列化
     *
     * @param obj obj
     * @return {@link byte[]}
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     *
     * @param bytes       字节流
     * @param messageType 消息类型
     * @return {@link Object}
     */
    Object deserialize(byte[] bytes, int messageType);

    /**
     * 得到类型
     *
     * @return int
     */
    int getType();

    /**
     * 通过code编号得到序列化器
     *
     * @param code 编号
     * @return {@link Serializer}
     */
    static Serializer getSerializerByCode(int code) {
        SerializerType serializerType = SerializerType.valueOf(code);

        switch (serializerType) {
            case OBJECT_SERIALIZER:
                return new ObjectSerializer();
            case JSON_SERIALIZER:
                return new JsonSerializer();
            case KRYO_SERIALIZER:
                return new KryoSerializer();
            default:
                return null;
        }
    }

}
