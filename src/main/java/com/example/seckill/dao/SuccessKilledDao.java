package com.example.seckill.dao;

import com.example.seckill.pojo.SeckillSuccess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@Mapper
public interface SuccessKilledDao {

    /**
     * 根据秒杀商品Id和用户电话判断是否为重复秒杀，userPhone唯一
     * 重复秒杀返回0，用户电话唯一则插入秒杀订单
     */
    int insertSuccesskilled(@Param("seckillId") long seckillId,
                            @Param("money") BigDecimal money,
                            @Param("userPhone") long userPhone);

    /**
     * 根据秒杀商品Id和用户电话查询成功秒杀的商品对象
     */
    SeckillSuccess querySeckillSuccessById(@Param("seckillId") long seckillId,
                                           @Param("userPhone") long userPhone);
}
