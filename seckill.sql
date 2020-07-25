CREATE DATABASE seckill default CHARACTER SET utf8;

use seckill;
create table seckill(
                        seckill_id  bigint    NOT NULL AUTO_INCREMENT comment '商品Id',
                        title       varchar(1000)      DEFAULT NULL comment '商品标题',
                        price       decimal(10,2)      DEFAULT NULL comment '商品原价格',
                        cost_price  decimal(10,2)      DEFAULT Null comment '商品秒杀价格',
                        stock_count bigint             DEFAULT NULL comment '剩余库存数量',
                        start_time  timestamp NOT NULL DEFAULT '2020-01-01 00:00:01' comment '秒杀开始时间',
                        end_time    timestamp NOT NULL DEFAULT '2020-01-01 00:00:01' comment '秒杀结束时间',
                        create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                        primary key (seckill_id),
                        KEY idx_start_time (start_time),
                        KEY idx_end_time (end_time),
                        KEY idx_create_time (create_time)

)CHARSET=utf8 ENGINE=InnoDB comment '秒杀商品表';

create table seckill_order(
                                seckill_id  bigint    NOT NULL comment '秒杀商品Id',
                                money       decimal(10,2)      DEFAULT NULL comment '支付金额',
                                user_phone  bigint    NOT NULL comment '用户手机号',
                                create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '创建时间',
                                state       tinyint   NOT NULL DEFAULT -1 comment '状态：-1无效，0成功，1已付款',
                                PRIMARY KEY (seckill_id, user_phone)
)CHARSET=utf8 ENGINE=InnoDB comment '秒杀订单表';

-- 添加秒杀商品表数据
INSERT INTO seckill (title, price, cost_price, start_time, end_time, stock_count) VALUES('Apple/苹果 iPhone 6s Plus 国行原装苹果6sp 5.5寸全网通4G手机', 2600.00, 1100.00, '2020-05-30 16:30:00', '2021-10-017 16:30:00', 10);
INSERT INTO seckill (title, price, cost_price, start_time, end_time, stock_count) VALUES('ins新款连帽毛领棉袄宽松棉衣女冬外套学生棉服', 200.00, 150.00, '2020-07-06 16:30:00', '2020-07-15 16:30:00', 10);
INSERT INTO seckill (title, price, cost_price, start_time, end_time, stock_count) VALUES('可爱超萌兔子毛绒玩具垂耳兔公仔布娃娃睡觉抱女孩玩偶大号女生 ', 160.00, 130.00, '2020-01-06 16:30:00', '2020-02-17 16:30:00', 20);
