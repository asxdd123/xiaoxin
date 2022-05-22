package com.heima.model.admin.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdUserDto implements Serializable {

    //用户名
    private String name;

    //密码
    private String password;
}

