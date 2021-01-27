package com.simple.enums;

/**
 * @Author: haoran
 * @Date: 2020/9/10 11:16 上午
 * @Description: RocketMQ延迟消费等级
 */
public enum RocketMQDelayLevelEnum {
	DELAY_1S(1, "延迟1秒钟"),
	DELAY_5S(2, "延迟5秒钟"),
	DELAY_10S(3, "延迟10秒钟"),
	DELAY_30S(4, "延迟30秒钟"),
	DELAY_1M(5, "延迟1分钟"),
	DELAY_2M(6, "延迟2分钟"),
	DELAY_3M(7, "延迟3分钟"),
	DELAY_4M(8, "延迟4分钟"),
	DELAY_5M(9, "延迟5分钟"),
	DELAY_6M(10, "延迟6分钟"),
	DELAY_7M(11, "延迟7分钟"),
	DELAY_8M(12, "延迟8分钟"),
	DELAY_9M(13, "延迟9分钟"),
	DELAY_10M(14, "延迟10分钟"),
	DELAY_20M(15, "延迟20分钟"),
	DELAY_30M(16, "延迟30分钟"),
	DELAY_1H(17, "延迟1小时"),
	DELAY_2H(18, "延迟2小时"),
	;

	private Integer level;
	private String desc;

	RocketMQDelayLevelEnum(Integer level, String desc) {
		this.level = level;
		this.desc = desc;
	}

	public Integer getLevel() {
		return level;
	}
	public String getDesc() {
		return desc;
	}

	public static RocketMQDelayLevelEnum getEnumByLevel(Integer level) {
		for (RocketMQDelayLevelEnum rocketMQDelayLevelEnum : RocketMQDelayLevelEnum.values()) {
			if (rocketMQDelayLevelEnum.getLevel().equals(level)) {
				return rocketMQDelayLevelEnum;
			}
		}
		return null;
	}
}
