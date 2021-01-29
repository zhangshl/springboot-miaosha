package com.simple.controller;

import com.simple.constanst.Constants;
import com.simple.domain.Order;
import com.simple.enums.RocketMQDelayLevelEnum;
import com.simple.service.OrderService;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhangshl
 * 提交订单入口
 */
@Controller
@RequestMapping("/app")
public class OrderController {

    /** 自增ID，分布式环境使用分布式ID */
    private AtomicLong orderIdGenerate= new AtomicLong();

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedissonClient redissonClient;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.producer.delay.topic}")
    private String delayTopic;

    /**
     * 提交订单：通用订单流程，不止给秒杀用，普通商品也走次流程
     * @return
     *
     * 此逻辑可能出现少卖问题，当redis中订单为0的情况下，间隔一段时间进行mysql和redis的数据同步
     */
    @RequestMapping("/submitOrder")
    @ResponseBody
    public Order submitOrder(Order order) {

        /** redis预减库存 */
        try {
            //提单预减库存
            long stock = redissonClient.getAtomicLong(Constants.ORDER_STOCK_PREFIX + order.getSkuId()).addAndGet(-order.getBuyNum());
            if (stock<0){
                //无库存
                redissonClient.getAtomicLong(Constants.ORDER_STOCK_PREFIX + order.getSkuId()).addAndGet(order.getBuyNum());
                return null;
            }
        }catch (Exception e){
            return null;
        }

        order.setId(System.currentTimeMillis() + orderIdGenerate.incrementAndGet());
        /**发送延时消息，预扣了库存，如果提单失败，或者到期不支付，则回滚预扣的库存*/
        SendResult sendResult = rocketMQTemplate.syncSend(delayTopic, MessageBuilder.withPayload(order).build(), 1000, RocketMQDelayLevelEnum.DELAY_5M.getLevel());
        if (sendResult.getSendStatus() != SendStatus.SEND_OK){
            return null;
        }

        /** 插入订单进数据库 */
        try{
            orderService.insertSelective(order);
        }catch (Exception e){
            redissonClient.getAtomicLong(Constants.ORDER_STOCK_PREFIX + order.getSkuId()).addAndGet(order.getBuyNum());
            e.printStackTrace();
            return null;
        }

        //返回订单信息，跳转支付页面
        return order;
    }
}
