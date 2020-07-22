package com.example.seckill.service;

import com.example.seckill.dto.Exposer;
import com.example.seckill.dto.SeckillExecution;
import com.example.seckill.dto.SeckillResult;
import com.example.seckill.exception.RepeatKillException;
import com.example.seckill.exception.SeckillCloseException;
import com.example.seckill.exception.SeckillException;
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
        SeckillExecution seckillExecution = null;
        try {
            seckillExecution = seckillService.executeSeckill(1L,13800002111L,"dfb7c524947b7fd30d1a65dec8ff97a5");
        } catch (SeckillException e) {
            if(e.getClass().equals(RepeatKillException.class)){
                System.out.println("重复秒杀");
            }else if(e.getClass().equals(SeckillCloseException.class)){
                System.out.println("秒杀结束");
            }else{
                System.out.println("内部异常");
            }
        }
        logger.info("seckillExection={}",seckillExecution);
    }
}