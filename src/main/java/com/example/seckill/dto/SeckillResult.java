package com.example.seckill.dto;

import lombok.Data;

/**
 * 封装Ajax请求返回的结果，使用泛型可以拿到相应的数据data
 * @param <T>
 */
@Data
public class SeckillResult<T> {
    private boolean success;
    private T data;
    private String error;

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

}
