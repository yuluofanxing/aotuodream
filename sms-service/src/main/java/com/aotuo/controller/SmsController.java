package com.aotuo.controller;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;
import com.aotuo.pojo.repojo.Result;
import com.aotuo.utils.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sms")
public class SmsController {
    @Autowired
    private SmsUtil smsUtil;
    //从前端接受手机号和验证码
    @RequestMapping("/sendCode")
    public Result sendCode(String phone, String code){
        //首先我们这里要做健壮性判断
        //如果用户名和密码有一个为空的话
        if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(code)){
            return new Result(true,"手机号或验证码错误",null);
        }
        //发送短信验证码
        Map<String,String>params=new HashMap<>();
        params.put("code",code);
        String jsonCode = JSON.toJSONString(params);
        try {
            smsUtil.sendSms(phone,jsonCode);
            return new Result(true,"发送验证码成功","恭喜发送成功");
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage(),null);
        }

    }
}
