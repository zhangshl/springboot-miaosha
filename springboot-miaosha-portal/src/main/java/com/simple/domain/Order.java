package com.simple.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private Long id;

    private Long userId;

    private Long skuId;

    private Integer buyNum;

    private Double amount;

    private Integer status;
}