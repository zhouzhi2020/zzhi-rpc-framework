package com.zzhi.client.proxy;

import com.zzhi.client.Client;
import lombok.AllArgsConstructor;
import com.zzhi.vo.RpcRequest;
import com.zzhi.vo.RpcResponse;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 客户端代理
 *
 * @author zzhi
 * @date 2022/08/02
 */
@AllArgsConstructor
@Slf4j
public class ClientProxy implements InvocationHandler {
    private Client client;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("代理类增强方法：包装成 request 发出");
        RpcRequest request = new RpcRequest();
        request.setInterfaceName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParamsType(method.getParameterTypes());
        request.setParams(args);
        RpcResponse response = client.sendRequest(request);

        return response.getData().get("result");
    }

    public <T> T getProxyInstance(Class<T> clazz) {
        Object obj = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T) obj;
    }
}
