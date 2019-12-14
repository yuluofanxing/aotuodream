package com.aotuo.service;

import com.aotuo.pojo.trpojo.User;

import java.util.List;

public interface UserService {
    /**
     * 用户登录
     * @param user
     * @return 登录成功的用户对象
     */
    public User login(User user);

    /**
     * 查询所有用户信息
     * @return 所用用户信息
     */
    public List<User>findAll();
}
