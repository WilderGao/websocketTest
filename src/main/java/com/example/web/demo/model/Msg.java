package com.example.web.demo.model;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Administrator
 * @date 2017/10/29
 */
@Data
@NoArgsConstructor
public class Msg {
    public static final int TYPE_RECEIVED = 0 ;
    public static final int TYPE_SEND = 1 ;
    private int id;
    private String content ;
    private int type ;
    /**
     * 发送者
     */
    private User fromUser;
    /**
     * 接收者
     */
    private User toUser;
    private String fromUserAccount;
    private String toUserAccount;

}
