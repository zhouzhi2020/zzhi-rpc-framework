package com.zzhi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 序列化器类型
 *
 * @author zzhi
 * @date 2022/08/02
 */
@AllArgsConstructor
@Getter
public enum SerializerType {

    /**
     * 对象序列化器
     */
    OBJECT_SERIALIZER(0),
    /**
     * json序列化器
     */
    JSON_SERIALIZER(1),

    UNKNOWN(-1);

    private final Integer type;

    public static SerializerType valueOf(int value) {
        SerializerType serializerType;
        switch (value) {
            case 0:
                serializerType = OBJECT_SERIALIZER;
                break;
            case 1:
                serializerType = JSON_SERIALIZER;
                break;
            default:
                serializerType = UNKNOWN;
                break;
        }
        return serializerType;
    }
}
