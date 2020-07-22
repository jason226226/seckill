# 基于SpringBoot的菜品秒杀系统

**使用的技术栈**：
前端技术 ：Bootstrap + jQuery + Thymeleaf
后端技术 ：SpringBoot + MyBatis + MySQL + Redis + RabbitMQ

# 开发步骤
## 第一步：
  开发出一个简单操作数据库的秒杀系统，只用到SpringBoot+MyBaties+MySQL，基础框架搭建完成
  
## 第二步
  加入Redis缓存，主要使用本地内存标记秒杀结束的商品，再使用Redis判断是否重复秒杀（使用用户手机号+商品ID作为主键），从而减少对数据库的访问
  使用RateLimiter完成限流

## 第三步
  加入RabbitMQ消息队列，把每一个秒杀请求放入消息队列中，异步完成下单请求。
