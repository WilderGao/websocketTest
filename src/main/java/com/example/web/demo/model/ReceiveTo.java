package com.example.web.demo.model;

import lombok.Data;

/**
 * @author Administrator
 * @time：
 * @Discription：
 */
@Data
public class ReceiveTo<T> {
    private int method;
    private T requestBody;

}
