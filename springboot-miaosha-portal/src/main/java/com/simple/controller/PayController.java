package com.simple.controller;

import com.simple.domain.SkOrderPayResult;
import com.simple.service.limit.ApiLimit;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author zhangshl
 * 支付结果通知
 */
@Controller
@RequestMapping("/app")
public class PayController {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 支付流程：客户单端支付，完成后通知后端，并且第三方支付方异步通知
     * 支付结果通知
     * @param skOrderPayResult
     */
    @ApiLimit
    @RequestMapping("/payResult")
    @ResponseBody
    public void payResult(SkOrderPayResult skOrderPayResult) {
        //订单支付结果
        Message<SkOrderPayResult> message = MessageBuilder.withPayload(skOrderPayResult).build();

        //订单支付成功的后续操作
        rocketMQTemplate.sendMessageInTransaction("tx-order", message, null);
    }
}
