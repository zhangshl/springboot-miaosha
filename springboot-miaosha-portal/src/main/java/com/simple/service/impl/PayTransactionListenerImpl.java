package com.simple.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.simple.domain.Order;
import com.simple.domain.SkOrderPayResult;
import com.simple.enums.ResultEnum;
import com.simple.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

/**
 * @Author: zhangshaolong001
 * @Date: 2021/1/13 6:16 下午
 * @Description：
 */
@RocketMQTransactionListener()
public class PayTransactionListenerImpl implements RocketMQLocalTransactionListener {
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private OrderService orderService;

    /**
     * 本地事务执行，支付异步扣库存
     *
     * @param message
     * @param o
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {
            String orderMessage = new String((byte[]) message.getPayload());
            SkOrderPayResult payResult = JSONObject.parseObject(orderMessage, SkOrderPayResult.class);
            //支付成功，异步执行扣库存，消费此消息
            if (ResultEnum.SUCCESS.getCode() == payResult.getResult()){
                //TODO 修改订单状态，异步消息去扣mysql库存
                Order order = orderService.selectByPrimaryKey(payResult.getOrderId());
                order.setStatus(ResultEnum.SUCCESS.getCode());
                orderService.updateByPrimaryKey(order);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return RocketMQLocalTransactionState.UNKNOWN;
        }

        return RocketMQLocalTransactionState.COMMIT;
    }

    /**
     * 回调接口
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        try {
            String orderMessage = new String((byte[]) message.getPayload());
            SkOrderPayResult payResult = JSONObject.parseObject(orderMessage, SkOrderPayResult.class);

            //检查mysql订单状态，完成支付，再扣mysql库存
            Order order = orderService.selectByPrimaryKey(payResult.getOrderId());

            if (order != null && order.getStatus().equals(ResultEnum.SUCCESS.getCode())){
                return RocketMQLocalTransactionState.COMMIT;
            }

            //其他状态不扣库存，也不回滚，等待另一个延时队列结果
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return RocketMQLocalTransactionState.UNKNOWN;
        }

        return RocketMQLocalTransactionState.UNKNOWN;
    }
}