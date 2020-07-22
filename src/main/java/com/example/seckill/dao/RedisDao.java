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

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化连接池，Redis端口是6379，默认配置Redis最多有16个数据库
        jedisPool = new JedisPool("redis://localhost:6379");
    }

    public <T> T getSeckill(String prefix, long seckillId, Class<T> clazz){
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = prefix + seckillId;
                RuntimeSchema<T> schema = RuntimeSchema.createFrom(clazz);
                // 采用自定义序列化protostuff,把字节数组反序列化为一个对象
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes!=null){
                    T t = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,t,schema);
                    return t;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    public <T> String putSeckill(String prefix, long seckillId, T value){
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = prefix + seckillId;
                RuntimeSchema<T> schema = (RuntimeSchema<T>) RuntimeSchema.createFrom(value.getClass());
                //把对象序列化为一个字节数组
                byte[] bytes = ProtostuffIOUtil.toByteArray(value,schema,
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

    public Integer decrStock(String prefix, long seckillId){
        try{
            Jedis jedis = jedisPool.getResource();
            try{
                String key = prefix + seckillId;
                RuntimeSchema<Integer> schema = RuntimeSchema.createFrom(Integer.class);
                // 采用自定义序列化protostuff,把字节数组反序列化为一个对象
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes!=null){
                    Integer stock = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,stock,schema);
                    stock-=1;
                    putSeckill(prefix,seckillId,stock);
                    return stock;
                }

            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return -1;
    }

    public boolean exit(String prefix, long seckillId){
        try {
            Jedis jedis = jedisPool.getResource();
            try{
                String key = prefix + seckillId;
                return jedis.exists(key);
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return false;
    }
}
