package com.zzhi.service;

import com.zzhi.entity.User;

/**
 * @author zzhi
 */
public interface UserService {
    /**
     * 通过id得到用户信息
     *
     * @param userId 用户id
     * @return {@link User}
     */
    public User getUserById(Integer userId);

    /**
     * 插入用户
     *
     * @param user 用户
     */
    public void insertUser(User user);

    /**
     * 通过id删除用户
     *
     * @param userId 用户id
     */
    public void deleteUserById(Integer userId);
}
