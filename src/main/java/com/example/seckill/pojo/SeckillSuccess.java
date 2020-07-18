package com.example.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillSuccess {

    private long seckillId;
    private BigDecimal money;
    private long userPhone;
    private short state;
    private Date createTime;

    private Seckill seckill;
}
