package com.simple.service.impl;

import com.simple.domain.SkOrder;
import com.simple.enums.ResultEnum;
import com.simple.service.SecKillService;
import com.simple.service.SkOrderService;
import com.simple.util.SecKillUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: zhangshaolong001
 * @Date: 2021/1/12 6:18 下午
 * @Description：
 */
@Service
public class SecKillServiceImpl implements SecKillService {
    /** 自增ID，分布式环境使用分布式ID */
    private AtomicLong count= new AtomicLong();

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SkOrderService skOrderService;


    /**
     * 秒杀实现
     * @param skuId
     * @return
     */
    @Override
    public int seckill(Long skuId) {
        //秒杀是否结束标记
        if (SecKillUtils.secKillFlag.get(skuId) != null){
            return ResultEnum.FAIL.getCode();
        }
        //生成订单，orderId在线上需要使用分布式ID
        SkOrder skOrder = SkOrder.builder().orderId(count.getAndIncrement()).skuId(skuId).userId(123L).build();
        long stock = redissonClient.getAtomicLong(String.valueOf(skOrder.getSkuId())).decrementAndGet();
        if (stock<0){
            //秒杀结束标志
            SecKillUtils.secKillFlag.put(skOrder.getSkuId(), false);
            redissonClient.getAtomicLong(String.valueOf(skOrder.getSkuId())).incrementAndGet();
            return ResultEnum.FAIL.getCode();
        }
        return ResultEnum.SUCCESS.getCode();
    }
}
