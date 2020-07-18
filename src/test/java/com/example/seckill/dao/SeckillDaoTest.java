package com.example.seckill.dao;

import com.example.seckill.pojo.Seckill;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class SeckillDaoTest {

    @Autowired
    private SeckillDao seckillDao;
    @Test
    void reduceNumber() {

    }

    @Test
    void queryById() {
        Seckill seckill = seckillDao.queryById(1L);
        System.out.println(seckill);
    }

    @Test
    void queryAll() {
        List<Seckill> seckill = seckillDao.queryAll(0,3);
        System.out.println(seckill);
    }
}