package com.aotuo.controller;

import com.aotuo.client.RegisterClient;
import com.aotuo.pojo.repojo.Result;
import com.aotuo.pojo.trpojo.User;
import com.aotuo.service.RegisterService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("register")
public class RegisterController {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RegisterClient registerClient;
    //发送验证码
    @RequestMapping("sendCode")
    public Result sendCode(String phone){
        if (StringUtils.isEmpty(phone)){
            return new Result(false,"手机号不能为空","输出错误哦");
        }
        //生成短信验证码 //生成6位短信验证码
        String code = RandomStringUtils.randomNumeric(4);
        //将生成的验证码保存到redis中
        redisTemplate.boundValueOps(phone).set(code);
        //设置验证码超时时间(5分钟超时)
        redisTemplate.boundValueOps(phone).expire(5L, TimeUnit.MINUTES);

        RegisterClient.Params params = new RegisterClient.Params();
        params.setPhone(phone);
        params.setCode(code);
        Result result = registerClient.sendCode(params);
        System.out.println(code);
        return new Result(true,"短信验证码发送成功",result);
    }

    //完成注册
    @PostMapping("add/{varityCode}")
    public Result add(@RequestBody User user,@PathVariable String varityCode){
        System.out.println(user.getPhone());
        List<User> byPhone = registerService.findByPhone(user.getPhone());
        //如果长度=0,可以进行注册,如果长度>0,说明一件注册过了
        if(byPhone.size()>0){
            return new Result(false,"您的账号已经进行了注册","请不要重复进行注册");
        }

        String redis_code = (String) redisTemplate.boundValueOps(user.getPhone()).get();
        //检验验证码
        if (!StringUtils.equalsIgnoreCase(varityCode,redis_code)){
            return new Result(false,"验证码输入错误请重新输入",null);
        }
        registerService.add(user);
        return new Result(true,"注册成功","恭喜注册成功");
    }
    @PostMapping("findByPhone")
    public Result findByPhone(@RequestBody User user){
        //查询数据库,如果手机号在数据库中,也就是查询到了相应的数据,那么说明用户注册过,不能注册
        System.out.println(user.getPhone());
        List<User> byPhone = registerService.findByPhone(user.getPhone());
        //如果长度=0,可以进行注册,如果长度>0,说明一件注册过了
        if(byPhone.size()>0){
            return new Result(false,"您的账号已经进行了注册","请不要重复进行注册");
        }
        return new Result(true,"账号可以注册","success");
    }
}
