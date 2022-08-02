package com.zzhi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zzhi
 */

@AllArgsConstructor
@Getter
public enum ResponseType {

    /**
     * 成功
     */
    SUCCESS(200),
    /**
     * 错误
     */
    ERROR(500);

    private final Integer type;
}
