package com.simple.controller;

import com.simple.constanst.Constants;
import com.simple.domain.SkOrder;
import com.simple.service.SkOrderService;
import com.simple.service.limit.ApiLimit;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhangshl
 * 提交订单入口
 */
@Controller
@RequestMapping("/app")
public class OrderController {

    @Autowired
    private SkOrderService orderService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 提交订单：通用订单流程，不止给秒杀用，普通商品也走次流程
     * @return
     *
     * 此逻辑可能出现少卖问题，当redis中订单为0的情况下，间隔一段时间进行mysql和redis的数据同步
     */
    @ApiLimit
    @RequestMapping("/submitOrder")
    @ResponseBody
    public SkOrder submitOrder(SkOrder skOrder) {

        /** redis预减库存 */
        try {
            //提单预减库存
            long stock = redissonClient.getAtomicLong(Constants.ORDER_STOCK_PREFIX + skOrder.getSkuId()).addAndGet(-skOrder.getBuyNum());
            if (stock<0){
                //无库存
                redissonClient.getAtomicLong(Constants.ORDER_STOCK_PREFIX + skOrder.getSkuId()).addAndGet(skOrder.getBuyNum());
                return null;
            }
        }catch (Exception e){
            return null;
        }

        /** 插入订单进数据库 */
        try{
            orderService.insertSelective(skOrder);
        }catch (Exception e){
            redissonClient.getAtomicLong(Constants.ORDER_STOCK_PREFIX + skOrder.getSkuId()).addAndGet(skOrder.getBuyNum());
            return null;
        }

        //返回订单信息，跳转支付页面
        return skOrder;
    }
}
