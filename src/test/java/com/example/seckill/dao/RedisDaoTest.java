package com.example.seckill.dao;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class RedisDaoTest {
    @Autowired
    RedisDao redisDao;
    @Test
    void decrStock() {
        Integer stock = redisDao.getSeckill("mark:", 1L, Integer.class);
        System.out.println(stock);
        Integer stock1 = redisDao.decrStock("mark:", 1L);
        System.out.println(stock1);
        Integer stock2 = redisDao.getSeckill("mark:", 1L, Integer.class);
        System.out.println(stock2);
    }
}