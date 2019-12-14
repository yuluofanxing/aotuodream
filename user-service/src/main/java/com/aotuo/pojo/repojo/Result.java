package com.aotuo.pojo.repojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result implements Serializable {
    //设置一个状态码
    private boolean status;
    //设置提示消息
    private String msg;
    //设置返回的相关数据
    private Object data;
}
