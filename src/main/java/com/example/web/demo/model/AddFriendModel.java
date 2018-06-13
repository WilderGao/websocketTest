package com.example.web.demo.model;


import lombok.Data;

/**
 *
 * @author Administrator
 * @date 2017/11/6
 */

@Data
public class AddFriendModel {
    private int myId;
    private String myAccount ;
    private int friendId;
    private String friendAccount;

}
