package com.example.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Seckill {

    private long seckillId;
    private String title;
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer stockCount;
    private Date startTime;
    private Date endTime;
    private Date createTime;
}
