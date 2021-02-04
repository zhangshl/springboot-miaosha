package com.simple.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.simple.constanst.Constants;
import com.simple.domain.SkGoods;
import com.simple.dao.mapper.SkGoodsMapper;
import com.simple.service.ProductService;
import com.simple.util.SecKillUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author: zhangshaolong001
 * @Date: 2021/1/12 6:18 下午
 * @Description：
 */
@Service
public class ProductServiceImpl implements ProductService, InitializingBean {
    @Autowired
    private SkGoodsMapper skGoodsMapper;

    /**秒杀商品本地缓存*/
    private LoadingCache<Long, SkGoods> localCache;
    private static final int TIME_OUT = 1;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public int insert(SkGoods record) {
        return skGoodsMapper.insert(record);
    }

    /**
     * 查询商品
     * @param skuId
     * @return
     */
    @Override
    public SkGoods selectByPrimaryKey(Long skuId) {
        if (localCache.get(skuId)!=null){
            return localCache.get(skuId);
        }

        SkGoods skGoods = skGoodsMapper.selectByPrimaryKey(skuId);

        //放入缓存
        localCache.put(skuId, skGoods);
        //将商品库存放入redis缓存
        redissonClient.getAtomicLong(Constants.KILL_STOCK_PREFIX + skuId).set(skGoods.getGoodsStock());
        redissonClient.getAtomicLong(Constants.KILL_STOCK_PREFIX + skuId).expire(TIME_OUT, TimeUnit.DAYS);

        //将商品库存放入redis缓存
        redissonClient.getAtomicLong(Constants.ORDER_STOCK_PREFIX + skuId).set(skGoods.getGoodsStock());
        redissonClient.getAtomicLong(Constants.ORDER_STOCK_PREFIX + skuId).expire(TIME_OUT, TimeUnit.DAYS);

        return skGoods;
    }

    /**
     * 重置库存
     * @param skuId
     * @return
     */
    @Override
    public SkGoods reset(Long skuId) {
        SkGoods skGoods = skGoodsMapper.selectByPrimaryKey(skuId);

        //放入缓存
        localCache.put(skuId, skGoods);
        //将商品库存放入redis缓存
        redissonClient.getAtomicLong(Constants.KILL_STOCK_PREFIX + skuId).set(skGoods.getGoodsStock());
        redissonClient.getAtomicLong(Constants.KILL_STOCK_PREFIX + skuId).expire(TIME_OUT, TimeUnit.DAYS);

        //将商品库存放入redis缓存
        redissonClient.getAtomicLong(Constants.ORDER_STOCK_PREFIX + skuId).set(skGoods.getGoodsStock());
        redissonClient.getAtomicLong(Constants.ORDER_STOCK_PREFIX + skuId).expire(TIME_OUT, TimeUnit.DAYS);

        // 重置售罄标志
        SecKillUtils.secKillFlag.remove(skuId);
        return skGoods;
    }


    /**
     * 初始化，将商品加载进缓存中
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        localCache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(TIME_OUT, TimeUnit.HOURS)
                .build(k -> getValue(k));

        /** 初始化将秒杀商品加载进缓存，按业务逻辑需求加载需要的数据*/
        Long skuId = 3L;
        SkGoods skGoods = skGoodsMapper.selectByPrimaryKey(skuId);
        localCache.put(skuId, skGoods);
        //将商品库存放入redis缓存
        redissonClient.getAtomicLong(Constants.KILL_STOCK_PREFIX + skuId).set(skGoods.getGoodsStock());
        redissonClient.getAtomicLong(Constants.KILL_STOCK_PREFIX + skuId).expire(TIME_OUT, TimeUnit.DAYS);

        //将商品库存放入redis缓存
        redissonClient.getAtomicLong(Constants.ORDER_STOCK_PREFIX + skuId).set(skGoods.getGoodsStock());
        redissonClient.getAtomicLong(Constants.ORDER_STOCK_PREFIX + skuId).expire(TIME_OUT, TimeUnit.DAYS);
    }

    public SkGoods getValue(Long key){
        return null;
    }
}
