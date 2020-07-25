package com.example.seckill.rabbitmq;

import lombok.Data;

@Data
public class SeckillMessage {

    private long phone;
    private long seckillId;
}
