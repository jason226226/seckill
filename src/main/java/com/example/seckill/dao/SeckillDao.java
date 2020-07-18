package com.example.seckill.dao;

import com.example.seckill.pojo.Seckill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface SeckillDao {
    /**
     * 根据秒杀商品号和秒杀时间判断是否在秒杀时间内然后更新商品数
     * 若在秒杀时间内则减库存
     * @param seckillId
     * @param killTime
     * @return 两个及以上参数需要加@Param标识
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据Id查询商品
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
}
