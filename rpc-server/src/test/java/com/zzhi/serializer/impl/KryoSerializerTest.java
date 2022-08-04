package com.zzhi.serializer.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import com.zzhi.service.UserService;
import com.zzhi.vo.RpcRequest;
import junit.framework.TestCase;
import org.checkerframework.checker.units.qual.K;

import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;


public class KryoSerializerTest extends TestCase {
    Pool<Kryo> kryoPool = new Pool<Kryo>(true, false, 4) {
        @Override
        protected Kryo create() {
            Kryo kryo = new Kryo();
            kryo.setRegistrationRequired(false);
            kryo.setReferences(true);
            return kryo;
        }
    };

    byte[] bytes;

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        System.out.println("执行后置操作");
    }

    public void testSerialize() {
        Kryo kryo = kryoPool.obtain();

        try (Output output = new Output(1024, -1)){
            kryo.writeClassAndObject(output, new RpcRequest());
            output.flush();
            bytes = output.getBuffer();
        } finally {
            kryoPool.free(kryo);
        }

    }

    public void testDeserialize() {
        testSerialize();
        Kryo kryo = kryoPool.obtain();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try (Input input = new Input(inputStream)) {
            RpcRequest request = (RpcRequest) kryo.readClassAndObject(input);
            System.out.println(request);
        } finally {
          kryoPool.free(kryo);
        }

    }
}