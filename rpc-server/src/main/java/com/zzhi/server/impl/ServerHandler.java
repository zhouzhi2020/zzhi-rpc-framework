package com.zzhi.server.impl;

import com.zzhi.server.ServiceProvider;
import com.zzhi.vo.RpcRequest;
import com.zzhi.vo.RpcResponse;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 服务器处理器
 *
 * @author zzhi
 * @date 2022/08/02
 */
@Slf4j
@AllArgsConstructor
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private ServiceProvider serviceProvide;
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        log.info("接受到 message");
        log.info("===> {}", msg);
        RpcResponse response = getResponse(msg);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("服务器 handler 层出错!");
        cause.printStackTrace();
        ctx.close();
    }

    public RpcResponse getResponse(RpcRequest request) {
        log.info("根据 request 调用本地方法获得响应");
        log.info("接受到的 request ===> {}", request);
        String interfaceName = request.getInterfaceName();
        String methodName = request.getMethodName();
        Class<?>[] paramsType = request.getParamsType();
        Object[] params = request.getParams();
        Object service = serviceProvide.getService(interfaceName);

        RpcResponse rpcResponse = new RpcResponse();

        if (service == null) {
            log.error("服务器暂时不支持该接口");
            return rpcResponse.error();
        }
        try {
            Class<?> name = Class.forName(interfaceName);
            Method method = name.getMethod(methodName, paramsType);

            Object obj = method.invoke(service, params);
            return rpcResponse.ok().data("result", obj);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("服务器根据 request 获取响应出错了");
            e.printStackTrace();
            return rpcResponse.error();
        }
    }
}
