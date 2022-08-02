package com.zzhi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 用户
 *
 * @author zzhi
 * @date 2022/08/01
 */
@Data
@NoArgsConstructor
public class User {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 地址
     */
    private String address;

    /**
     * 电话
     */
    private String tel;

}
