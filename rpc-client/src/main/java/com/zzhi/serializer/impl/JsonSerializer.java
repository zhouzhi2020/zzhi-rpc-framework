package com.zzhi.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.zzhi.enums.MessageType;
import com.zzhi.enums.SerializerType;
import com.zzhi.serializer.Serializer;
import com.zzhi.vo.RpcRequest;
import com.zzhi.vo.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * json序列化器
 *
 * @author zzhi
 * @date 2022/08/02
 */
@Slf4j
public class JsonSerializer implements Serializer {
    /**
     * 序列化
     *
     * @param obj obj
     * @return {@link byte[]}
     */
    @Override
    public byte[] serialize(Object obj) {
        return JSONObject.toJSONBytes(obj);
    }

    /**
     * 反序列化
     *
     * @param bytes 字节
     * @param type  类型
     * @return {@link Object}
     */
    @Override
    public Object deserialize(byte[] bytes, int type) {
        Object obj;
        MessageType messageType = MessageType.valueOf(type);
        switch (messageType) {
            case REQUEST:
                RpcRequest request = JSON.parseObject(bytes, RpcRequest.class);
                // params中有些可能不是基本数据类型，要判断，手动转换
                Object[] objects = new Object[request.getParams().length];
                for (int i = 0; i < objects.length; i++) {
                    Class<?> paramType = request.getParamsType()[i];
                    if (!paramType.isAssignableFrom(request.getParams()[i].getClass())) {
                        log.info("类对象 ===> {} 需要转换", request.getParams()[i].getClass());
                        objects[i] = JSON.toJavaObject((JSON) request.getParams()[i], paramType);
                    } else {
                        objects[i] = request.getParams()[i];
                    }
                }
                request.setParams(objects);
                obj = request;
                break;
            case RESPONSE:
                RpcResponse response = JSON.parseObject(bytes, RpcResponse.class, Feature.SupportClassForName);
                Map<String, Object> data = response.getData();
                Map<String, Class<?>> dataType = response.getDataType();
                for (Map.Entry<String, Object> next : data.entrySet()) {
                    String key = next.getKey();
                    Object val = next.getValue();
                    Class<?> valType = dataType.get(key);
                    if (!valType.isAssignableFrom(val.getClass())) {
                        Object value = JSON.toJavaObject((JSON) val, valType);
                        data.put(key, value);
                    }
                }
                obj = response;
                break;
            default:
                log.info("暂时不支持此种消息");
                throw  new RuntimeException();
        }
        return obj;
    }

    @Override
    public int getType() {
        return SerializerType.JSON_SERIALIZER.getType();
    }
}
