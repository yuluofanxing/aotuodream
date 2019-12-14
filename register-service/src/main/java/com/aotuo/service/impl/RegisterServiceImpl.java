package com.aotuo.service.impl;

import com.aotuo.mapper.RegisterMapper;
import com.aotuo.service.RegisterService;
import com.aotuo.pojo.trpojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService{
    @Autowired
    private RegisterMapper registerMapper;

    //完成对用户的添加
    @Override
    public void add(User user) {
        registerMapper.insert(user);
    }
    //通过手机号查询,如果数据库中有手机号,那么就注册过,不能进行注册
    @Override
    public List<User> findByPhone(String phone) {
        //创建新的example实例
        Example example=new Example(User.class);
        //创建查询条件
        Example.Criteria criteria = example.createCriteria();
        //用手机号创建实例
        criteria.andEqualTo("phone",phone);
        //创建实例根据手机号进行查询,默认为一个集合,但是我们知道这其中只会有一个元素
        List<User> users = registerMapper.selectByExample(example);
        //取出集合中的元素并且取出来并返回去
        return users;
    }
}
