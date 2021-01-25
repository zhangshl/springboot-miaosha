package com.simple.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author zhangshl
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkOrderPayResult {
    private Long id;

    private Long userId;

    private Long orderId;

    private Long skuId;

    private int result;

}