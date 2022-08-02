package com.zzhi.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzhi
 */
@Data
@NoArgsConstructor
public class RpcRequest {
    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] paramsType;
    /**
     * 参数
     */
    private Object[] params;
}
