package com.example.web.demo.model;

import lombok.Data;

/**
 * @author Administrator
 * @time：
 * @Discription：
 */
@Data
public class FeedBack<T> {
    private int state;
    private T data;

}
