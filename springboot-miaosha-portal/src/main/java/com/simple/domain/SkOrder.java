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
public class SkOrder {
    private Long id;

    private Long userId;

    private Long orderId;

    /**一个订单中可以包含多个商品，这里只简单一个单品*/
    private Long skuId;

    /**购买数量*/
    private int buyNum;

    /**购买数量*/
    private BigDecimal amount;

    private int status;


}