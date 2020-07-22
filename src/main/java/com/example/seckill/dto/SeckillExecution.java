package com.example.seckill.dto;

import com.example.seckill.enums.SeckillStatEnum;
import com.example.seckill.exception.RepeatKillException;
import com.example.seckill.exception.SeckillCloseException;
import com.example.seckill.exception.SeckillException;
import com.example.seckill.pojo.SeckillSuccess;
import lombok.Data;


/**
 * 封装秒杀执行后的结果
 */
@Data
public class SeckillExecution {

    private long seckillId;
    //0代表秒杀成功，-1代表秒杀失败，1代表成功下单
    private int state;

    private String stateInfo;

    private SeckillSuccess seckillSuccess;

    private SeckillException exception;

    public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SeckillSuccess seckillSuccess) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.seckillSuccess = seckillSuccess;
    }

    public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }

}
