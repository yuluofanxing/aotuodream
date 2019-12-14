package com.aotuo.pojo.trpojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Data
@Table(name = "customer")
public class User implements Serializable {
    @Id
    @KeySql(useGeneratedKeys =true)
    private Integer id;
    private String name;
    private String bea_id;
    private String sfzh;
    private String pwd;
    private String phone;
    private String nick;
    private Date birthday;
    //private String cityCode;
    private String description;
    private Date create_time;
    private int sex;
    private String head;
    private int status;
    private Date update_time;
}
