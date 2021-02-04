package com.simple.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.simple.domain.SkOrderPayResult;
import com.simple.domain.Stock;
import com.simple.service.StockService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 消费事务消息，执行实际扣库存步骤
 */
@Service
@RocketMQMessageListener(
	topic = "${rocketmq.producer.transaction.topic}",
	consumerGroup = "transactionGroup"
)
public class ConsumerTransactionListener implements RocketMQListener<String> {

	@Value("${times:10}")
	private int LIMIT_TIMES;

	@Autowired
	private StockService stockService;

	@Override
	public void onMessage(String s) {
		SkOrderPayResult payResult = JSONObject.parseObject(s, SkOrderPayResult.class);
		//去扣库存，乐观锁自旋扣库存
		int result = 0;
		int times = 0;
		while (result==0){
			result = stockService.updateByPrimaryKeySelective(payResult.getSkuId(), payResult.getBuyNum());
			if (++times > LIMIT_TIMES){
				break;
			}
		}

		if (result != 1){
			//TODO 失败处理流程，修改订单状态，退款
		}
	}
}
