package com.zzhi.client;

import com.zzhi.vo.RpcRequest;
import com.zzhi.vo.RpcResponse;

/**
 * @author zzhi
 */
public interface Client {
    /**
     * 发送请求
     *
     * @param request 请求
     * @return {@link RpcResponse}
     */
    public RpcResponse sendRequest(RpcRequest request);
}
