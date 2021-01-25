package com.simple.service.impl;

import org.springframework.stereotype.Service;

/**
 * @Author: haoran
 * @Date: 2020/9/11 3:21 下午
 * @Description: RocketMQ消费者服务
 */
@Service
public class RocketMQConsumerService {
	public void handleMessage(String message) {
		System.out.println(message);
	}
}
