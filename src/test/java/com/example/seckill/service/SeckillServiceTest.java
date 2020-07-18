package com.example.seckill.service;

import com.example.seckill.dto.Exposer;
import com.example.seckill.dto.SeckillExecution;
import com.example.seckill.pojo.Seckill;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;
    @Test
    void findAll() {
        List<Seckill> list = seckillService.findAll();
        logger.info("list={}",list);
    }

    @Test
    void findById() {
        Seckill seckill = seckillService.findById(1L);
        logger.info("seckill={}",seckill);
    }

    @Test
    void exposeSeckillUrl() {
        Exposer exposer = seckillService.exposeSeckillUrl(1L);
        logger.info("exposer={}",exposer);
    }

    @Test
    void executeSeckill() {
        SeckillExecution seckillExecution = seckillService.executeSeckill(1L,13800001111L,"dfb7c524947b7fd30d1a65dec8ff97a5");
        logger.info("seckillExection={}",seckillExecution);
    }
}