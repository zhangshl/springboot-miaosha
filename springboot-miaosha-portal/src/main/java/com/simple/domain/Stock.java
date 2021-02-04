package com.simple.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stock {
    private Long skuId;

    private Integer goodsStock;

    private Integer leftStock;

    private Integer version;

}