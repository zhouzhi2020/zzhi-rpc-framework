package com.zzhi.vo;

import com.zzhi.enums.ResponseType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zzhi
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class RpcResponse {
    private Integer code;
    private String message;

    private Map<String, Class<?>> dataType = new HashMap<>();
    private Map<String, Object> data = new HashMap<>();

    public RpcResponse ok() {
        this.code = ResponseType.SUCCESS.getType();
        this.message = "SUCCESS";
        return this;
    }

    public RpcResponse error() {
        this.code = ResponseType.ERROR.getType();
        this.message = "ERROR";
        return this;
    }
    public RpcResponse data(String key, Object val) {
        this.data.put(key, val);
        this.dataType.put(key, val.getClass());
        return this;
    }
}
