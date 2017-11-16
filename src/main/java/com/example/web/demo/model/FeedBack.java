package com.example.web.demo.model;

/**
 * @Author:高键城
 * @time：
 * @Discription：
 */
public class FeedBack<T> {
    private int state;
    private T data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FeedBack{" +
                "state=" + state +
                ", data=" + data +
                '}';
    }
}
