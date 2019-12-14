package com.aotuo.service;

import com.aotuo.pojo.trpojo.User;

import java.util.List;

public interface RegisterService {
    //新增客户
    public void add(User user);
    //根据电话号码查询
    public List<User> findByPhone(String phone);
}
