package com.springboot.sqlinjectionlab.entity;

import lombok.Data;

/**
 * 用户实体类
 */
@Data
public class User {
    private Integer id;
    private String username;
    private String password;
}
