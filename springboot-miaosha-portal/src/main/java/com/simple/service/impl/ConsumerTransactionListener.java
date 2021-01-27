package com.simple.service.impl;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 消费事务消息，执行实际扣库存步骤
 */
@Service
@RocketMQMessageListener(
	topic = "${rocketmq.producer.topic}",
	consumerGroup = "group2"
)
public class ConsumerTransactionListener implements RocketMQListener<String> {

	@Override
	public void onMessage(String s) {
		//TODO 去实际扣库存，update数据库
	}
}
