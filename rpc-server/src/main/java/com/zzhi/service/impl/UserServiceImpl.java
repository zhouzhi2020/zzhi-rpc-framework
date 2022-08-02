package com.zzhi.service.impl;

import com.zzhi.entity.User;
import com.zzhi.service.UserService;
import lombok.extern.slf4j.Slf4j;


/**
 * @author zzhi
 */
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public User getUserById(Integer userId) {
        log.info("调用getUserById");
        log.info("查询userId ===> {}", userId);

        User user = new User();
        user.setUserId(userId);
        user.setPassword("123456");
        user.setUserName("root");
        user.setAddress("深圳市南山区");
        user.setTel("12345678999");

        log.info("查询的user ===> {}", user);

        return user;
    }

    @Override
    public void insertUser(User user) {
        log.info("调用insertUser");
        log.info("要删除的用户为 ===> {}", user);

    }

    @Override
    public void deleteUserById(Integer userId) {
        log.info("调用deleteUserById");
        log.info("删除的用户id ===> {}", userId);
    }
}
