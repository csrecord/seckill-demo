package com.chenhf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chenhf.mapper")
//MapperScan指定要扫描的Mapper类的包的路径
public class SeckillDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoApplication.class, args);
    }

/**
 *
 * 接口测试：
 *          商品接口：/goods/toList
 *          windows QPS优化前1400    优化后2700
 *          linux   QPS优化前395.6   优化后
 *
 *          秒杀接口：/seckill/doSeckill
 *          windows QPS优化前785  优化后1435
 *
 */
}
