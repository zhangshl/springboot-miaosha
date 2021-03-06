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
public class SkGoods {
    private Long skuId;

    private String goodsName;

    private String goodsTitle;

    private String goodsImg;

    private BigDecimal goodsPrice;

    private Integer goodsStock;

}