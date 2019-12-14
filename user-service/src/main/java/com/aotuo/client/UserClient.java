package com.aotuo.client;

import com.aotuo.pojo.repojo.Result;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("sms-service")
public interface UserClient {
    @RequestMapping("/sms/sendCode")
    public Result sendCode(@SpringQueryMap Params params);
    @Data
    class Params{
        private String phone;
        private String code;
    }
}
