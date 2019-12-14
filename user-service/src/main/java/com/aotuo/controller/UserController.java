package com.aotuo.controller;

import com.aotuo.client.UserClient;
import com.aotuo.pojo.repojo.Result;
import com.aotuo.pojo.trpojo.User;
import com.aotuo.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;
    //使用redis
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserClient userClient;
    /**
     * 发送短信验证码
     * @return
     */
    @RequestMapping("sendCode")
    public Result sendCode(String phone){
        //做健壮性判断
        //判断手机号是否为空
        if (StringUtils.isEmpty(phone)){
            return new Result(false,"手机号不能为空","输出错误哦");
        }
        //生成短信验证码 //生成6位短信验证码
        String code = RandomStringUtils.randomNumeric(6);
        //将生成的验证码保存到redis中
        redisTemplate.boundValueOps(phone).set(code);
        //设置验证码超时时间(5分钟超时)
        redisTemplate.boundValueOps(phone).expire(5L, TimeUnit.MINUTES);

        //发送短信验证码
        //将短信发送出去,这里是发送get请求,用这个,如果发送post请求,那么用post
        //String url="http://localhost:9011/sms/sendCode?phone="+phone+"&code="+code;
        //System.out.println(url);
        //Result forObject = restTemplate.getForObject(url, Result.class);
        UserClient.Params params = new UserClient.Params();
        params.setPhone(phone);
        params.setCode(code);
        Result result = userClient.sendCode(params);
        System.out.println(code);
        return new Result(true,"短信验证码发送成功",result);
    }

    /**
     * 用户登录
     * @return
     */
    @RequestMapping("login")
    public Result login(User user,String varityCode){
        //健壮性判断,
        if(StringUtils.isEmpty(user.getPhone())||StringUtils.isEmpty(user.getPwd())||StringUtils.isEmpty(varityCode)){
            return new Result(false,"用户名密码不能为空","请输入正确的验证码");
        }
        //校验验证码输入是否正确
        //先获取存入redis中的验证码
        String code_redis = (String) redisTemplate.boundValueOps(user.getPhone()).get();
        //这里比对用户输入的验证码和存入redis中的验证码
        //校验验证码
        if(!StringUtils.equalsIgnoreCase(varityCode,code_redis)){
            return new Result(false,"验证码输入错误","请重新输入验证码");
        }
        //调用Service完成用户登录
        User loginUser = userService.login(user);
        if (loginUser==null){
            return new Result(false,"客户名或密码错误","请重新检查后登陆");
        }
        //登陆成功,把相应的用户信息保存在redis中
        //返回信息
        redisTemplate.boundValueOps(user.getPhone()).set(loginUser);
        return new Result(true,"顾客登录成功",loginUser);

    }

    /**
     * 查询所有信息
     * @return
     */
    @RequestMapping("findAll")
    public Result findAll(){
        List<User> users = userService.findAll();
        return new Result(true,"查找所有顾客信息成功",users);
    }
}
