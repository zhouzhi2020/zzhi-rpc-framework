package com.zzhi.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zzhi
 */
@Data
@NoArgsConstructor
public class RpcRequest implements Serializable {
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
