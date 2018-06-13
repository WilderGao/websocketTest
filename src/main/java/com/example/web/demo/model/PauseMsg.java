package com.example.web.demo.model;

import lombok.Data;

/**
 * @author Administrator
 * time：
 * @Discription：
 */
@Data
public class PauseMsg {
    private int id;
    private String content;
    private int type;
    private String fromUserAccount;
    private String toUserAccount;

}
