package com.simple.service.impl;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * mq消息消费
 */
@Service
@RocketMQMessageListener(
	topic = "${rocketmq.producer.topic}",
	consumerGroup = "group1"
)
public class ConsumerDelayListener implements RocketMQListener<String> {

	@Override
	public void onMessage(String s) {
		//TODO 延时消息处理，查看该订单是否支付
	}
}