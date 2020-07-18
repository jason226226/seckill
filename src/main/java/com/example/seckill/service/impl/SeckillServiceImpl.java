package com.example.seckill.service.impl;

import com.example.seckill.dao.SeckillDao;
import com.example.seckill.dao.SuccessKilledDao;
import com.example.seckill.dao.RedisDao;
import com.example.seckill.dto.Exposer;
import com.example.seckill.dto.SeckillExecution;
import com.example.seckill.enums.SeckillStatEnum;
import com.example.seckill.exception.RepeatKillException;
import com.example.seckill.exception.SeckillCloseException;
import com.example.seckill.exception.SeckillException;
import com.example.seckill.pojo.Seckill;
import com.example.seckill.pojo.SeckillSuccess;
import com.example.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;

    @Override
    public List<Seckill> findAll() {
        return seckillDao.queryAll(0,4);
    }

    @Override
    public Seckill findById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exposeSeckillUrl(long seckillId) {
        // 优化点：缓存优化：超时的基础上维护一致性，使用redis缓存热点商品
        Seckill seckill = redisDao.getSeckill("seckill:", seckillId);
        if(seckill==null) {
            seckill = seckillDao.queryById(seckillId);
            if(seckill==null){
                return new Exposer(false,seckillId);
            }else{
                redisDao.putSeckill("seckill:", seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5=getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5(long seckillId){
        String slat = "adfnadjifngdg";
        String base = seckillId+"/"+ slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    @Override
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException {

        if(md5==null||!md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        Date nowTime = new Date();
        try {
            // 先根据是否插入判断是否重复秒杀，再update，减少行级锁持有时间
            int insertCount = successKilledDao.insertSuccesskilled(seckillId, findById(seckillId).getCostPrice(), userPhone);
            if(insertCount<=0){
                throw new RepeatKillException("seckill is repeated");
            }else{
                // 减库存更新商品，拿到行级锁
                int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
                if(updateCount<=0){
                    throw new SeckillCloseException("seckill is closed");
                }else{
                    SeckillSuccess seckillSuccess = successKilledDao.querySeckillSuccessById(seckillId, userPhone);
                    return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS,seckillSuccess);
                    }
                }
        } catch (SeckillCloseException | RepeatKillException e1) {
            throw e1;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }

    }
}
