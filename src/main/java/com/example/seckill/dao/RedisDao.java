package com.example.seckill.dao;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.example.seckill.pojo.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Repository
public class RedisDao implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JedisPool jedisPool;
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化连接池，Redis端口是6379，默认配置Redis最多有16个数据库
        jedisPool = new JedisPool("redis://localhost:6379");
    }

    public Seckill getSeckill(String prefix, long seckillId){
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = prefix + seckillId;
                // 采用自定义序列化protostuff,把字节数组反序列化为一个对象
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes!=null){
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    public String putSeckill(String prefix,Seckill seckill){
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = prefix + seckill.getSeckillId();
                //把对象序列化为一个字节数组
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill,schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                int timeout = 60*60;
                return jedis.setex(key.getBytes(),timeout,bytes);
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public long decrStock(String prefix, long seckillId){
        try{
            Jedis jedis = jedisPool.getResource();
            try{
                String key = prefix+seckillId;
                return jedis.decr(key.getBytes());
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return -1;
    }

}
