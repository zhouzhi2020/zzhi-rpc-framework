package com.zzhi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型
 *
 * @author zzhi
 * @date 2022/08/02
 */
@AllArgsConstructor
@Getter
public enum MessageType {

    /**
     * 响应
     */
    RESPONSE(123456),
    /**
     * 请求
     */
    REQUEST(654321),

    UNKNOWN(-1);

    private final Integer type;

    public static MessageType valueOf(int value) {
        MessageType messageType;
        switch (value) {
            case 654321:
                messageType = REQUEST;
                break;
            case 123456:
                messageType = RESPONSE;
                break;
            default:
                messageType = UNKNOWN;
                break;
        }
        return messageType;
    }
}
