package com.aotuo.service.impl;


import com.aotuo.mapper.UserMapper;
import com.aotuo.pojo.trpojo.User;
import com.aotuo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    //和数据库进行交互将userMapper注入进来
    @Autowired
    private UserMapper userMapper;
    @Override
    public User login(User user) {
        //查询客户的相关信息
        User loginUser = userMapper.selectOne(user);
        return loginUser;
    }
    /**
     * 查询所有客户信息
     * @return 返回所有客户信息
     */
    @Override
    public List<User> findAll() {
        List<User> users = userMapper.selectAll();
        return users;
    }
}
