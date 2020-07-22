package com.example.seckill.dao.cache;

import com.example.seckill.dao.RedisDao;
import com.example.seckill.dao.SeckillDao;
import com.example.seckill.pojo.Seckill;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class RedisDaoTest{

    @Autowired
    private RedisDao redisDao;
    @Autowired
    private SeckillDao seckillDao;
    @Test
    public void testSeckill() throws Exception{
        long id = 2L;
        Seckill seckill = redisDao.getSeckill("seckill:",id, Seckill.class);
        if(seckill==null){
            seckill = seckillDao.queryById(id);
            if(seckill!=null){
                String result = redisDao.putSeckill("seckill:",seckill.getSeckillId(),seckill);
                System.out.println(result);
                seckill = redisDao.getSeckill("seckill:",id, Seckill.class);
                System.out.println(seckill);
            }
        }
    }
    @Test
    public void testSeckill1() throws Exception{
        long id = 2L;
        redisDao.putSeckill("seckill:", id, 20);
        Integer stock = redisDao.getSeckill("seckill:",id, Integer.class);
        System.out.println(stock);
    }
}