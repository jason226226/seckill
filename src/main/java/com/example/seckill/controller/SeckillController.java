package com.example.seckill.controller;

import com.example.seckill.dao.RedisDao;
import com.example.seckill.dto.Exposer;
import com.example.seckill.dto.SeckillExecution;
import com.example.seckill.dto.SeckillResult;
import com.example.seckill.enums.SeckillStatEnum;
import com.example.seckill.exception.RepeatKillException;
import com.example.seckill.exception.SeckillCloseException;
import com.example.seckill.exception.SeckillException;
import com.example.seckill.pojo.Seckill;
import com.example.seckill.service.SeckillService;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class SeckillController implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;
    @Autowired
    private RedisDao redisDao;
    // 令牌桶实现限流
    RateLimiter limiter = RateLimiter.create(10);
    // 本地变量做标记
    private HashMap<Long,Boolean> localMap = new HashMap<>();

    /**
     * 系统初始化,将商品信息加载到redis和本地内存
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<Seckill> seckills = seckillService.findAll();
        if(seckills==null){
            return;
        }
        for(Seckill seckill:seckills){
            redisDao.putSeckill("mark:",seckill);
            localMap.put(seckill.getSeckillId(),false);
        }
    }

    /**
     * 暴露秒杀接口
     * @param seckillId 商品Id
     * @return SeckillResult<Exposer>
     */
    @RequestMapping(value = "/seckill/{seckillId}/exposer",method = RequestMethod.POST)
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
            result = new SeckillResult<>(true,exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result = new SeckillResult<>(false,e.getMessage());
        }
        return result;
    }

    /**
     * 执行秒杀逻辑
     * @param seckillId
     * @param md5
     * @param phone
     * @return SeckillResult<SeckillExecution>
     */
    @RequestMapping(value = "/seckill/{seckillId}/{md5}/execution",
                    method = RequestMethod.POST,
                    produces = {"application/json;charset=UTF-8"})
    @ResponseBody // 返回Json数据
    public SeckillResult<SeckillExecution> execute(@PathVariable Long seckillId,
                                                   @PathVariable String md5,
                                                   @CookieValue(value = "killPhone",required = false) Long phone){
        try {
            // 执行秒杀逻辑
            SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
            return new SeckillResult<>(true,execution);
        } catch (RepeatKillException e){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<>(true,execution);
        }catch (SeckillCloseException e){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<>(true,execution);
        }catch (SeckillException e) {
            logger.error(e.getMessage(),e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<>(true,execution);
        }
    }
}
