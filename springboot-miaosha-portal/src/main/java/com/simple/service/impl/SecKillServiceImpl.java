package com.simple.service.impl;

import com.simple.constanst.Constants;
import com.simple.enums.ResultEnum;
import com.simple.service.SecKillService;
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

        //获取秒杀资格
        long stock = redissonClient.getAtomicLong(Constants.KILL_STOCK_PREFIX + skuId).decrementAndGet();
        if (stock<0){
            //秒杀结束标志
            SecKillUtils.secKillFlag.put(skuId, false);
            redissonClient.getAtomicLong(Constants.KILL_STOCK_PREFIX + skuId).incrementAndGet();
            return ResultEnum.FAIL.getCode();
        }
        return ResultEnum.SUCCESS.getCode();
    }
}
