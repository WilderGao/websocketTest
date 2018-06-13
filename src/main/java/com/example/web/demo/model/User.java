package com.example.web.demo.model;


import lombok.Data;

/**
 *
 * @author Administrator
 * @date 2017/10/28
 */
@Data
public class User implements Comparable<User>{
    private int userId;
    private String userAccount;
    private String userName ;
    private String userPassword;
    private int userAge;
    private String userSex;

    @Override
    public int compareTo( User o) {
        return this.getUserName().compareTo(o.getUserName());
    }


}
