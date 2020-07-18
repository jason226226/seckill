package com.example.seckill.service;


import com.example.seckill.dto.Exposer;
import com.example.seckill.dto.SeckillExecution;
import com.example.seckill.exception.SeckillException;
import com.example.seckill.pojo.Seckill;

import java.util.List;

public interface SeckillService{

    List<Seckill> findAll();

    Seckill findById(long seckillId);

    /**
     * 根据秒杀商品Id判断商品是否存在，seckillDao.queryById
     * 若存在，再判断当前时间是否在秒该商品秒杀时间内
     * @param seckillId
     * @return 是否暴露接口，否的话传入秒杀时间，是的话传入商品Id生成的Md5值 new Exposer()
     */
    Exposer exposeSeckillUrl(long seckillId);

    /**
     * 首先根据商品Id生成的md5判断md5是否正确
     * 然后根据减库存判断当前时间是否为秒杀时间  seckillDao.reduceNumber
     * 最后根据插入成功秒杀订单判断是否为重复秒杀    seckillDao.insertSuccessKilled
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return 返回成功秒杀的结果，包括状态，秒杀成功的订单 new SeckillExecution()
     * @throws SeckillException
     */
    SeckillExecution executeSeckill(long seckillId,long userPhone,String md5) throws SeckillException;


}
