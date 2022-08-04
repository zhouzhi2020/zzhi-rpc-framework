package com.zzhi.serializer.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import com.zzhi.enums.SerializerType;
import com.zzhi.serializer.Serializer;
import com.zzhi.vo.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

/**
 * kryo序列化器
 *
 * @author zzhi
 * @date 2022/08/04
 */
@Slf4j
public class KryoSerializer implements Serializer {
    private final Pool<Kryo> kryoPool = new Pool<Kryo>(true, false, 8) {
        @Override
        protected Kryo create() {
            Kryo kryo = new Kryo();
            kryo.setRegistrationRequired(false);
            kryo.setReferences(true);
            return kryo;
        }
    };
    @Override
    public byte[] serialize(Object obj) {
        Kryo kryo = kryoPool.obtain();
        log.info("进入了 kryo 的 serialize 方法");
        try (Output opt = new Output(1024, -1)) {
            kryo.writeClassAndObject(opt, obj);
            opt.flush();
            return opt.getBuffer();
        } finally {
            kryoPool.free(kryo);
        }
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Kryo kryo = kryoPool.obtain();
        log.info("进入了 kryo 的 deserialize 方法");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try (Input ipt = new Input(inputStream)) {
            return kryo.readClassAndObject(ipt);
        } finally {
            kryoPool.free(kryo);
        }
    }

    @Override
    public int getType() {
        return SerializerType.KRYO_SERIALIZER.getType();
    }
}
