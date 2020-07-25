package com.example.seckill.rabbitmq;

import com.alibaba.druid.support.json.JSONUtils;
import com.example.seckill.dao.RedisDao;
import com.example.seckill.pojo.Seckill;
import com.example.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {
    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);
    @Autowired
    SeckillService seckillService;
    @Autowired
    RedisDao redisDao;
    public void receive(String message, String md5) throws Exception{
        logger.info("receive message:"+message);
        SeckillMessage m = (SeckillMessage) JSONUtils.parse(message);
        long phone = m.getPhone();
        long seckillId = m.getSeckillId();
        Seckill seckill = seckillService.findById(seckillId);
        int stock = seckill.getStockCount();
        if(stock<=0){
            return;
        }
        try {
            seckillService.executeSeckill(seckillId, phone, md5);
        } catch (Exception e) {
            logger.info("秒杀失败");
        }
    }
}
