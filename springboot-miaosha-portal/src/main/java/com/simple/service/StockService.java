package com.simple.service;

import com.simple.domain.Stock;
import org.apache.ibatis.annotations.Mapper;

public interface StockService {

    int insert(Stock record);

    int insertSelective(Stock record);

    Stock selectByPrimaryKey(Long skuId);

    int updateByPrimaryKeySelective(Long skuId, int buyNum);

    int updateByPrimaryKey(Stock record);
}