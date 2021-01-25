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
public class MqConsumerListener implements RocketMQListener<String> {

	@Resource
	RocketMQConsumerService rocketMQConsumerService;

	@Override
	public void onMessage(String s) {
		rocketMQConsumerService.handleMessage(s);
	}
}
