package com.example.seckill.dao;

import com.example.seckill.pojo.Seckill;
import com.example.seckill.pojo.SeckillSuccess;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class SuccessKilledDaoTest {

    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private SeckillDao seckillDao;
    @Test
    void querySeckillSuccessById() {
        SeckillSuccess seckillSuccess = successKilledDao.querySeckillSuccessById(1L,13800000000L);
        System.out.println(seckillSuccess);
    }

    @Test
    void insertSuccesskilled() {
        Long seckillId = 1L;
        Seckill seckill = seckillDao.queryById(seckillId);

        int res = successKilledDao.insertSuccesskilled(1L, seckill.getPrice(), 13800000011L);
        System.out.println(res);

    }
}