package com.simple.dao.mapper;

import com.simple.domain.Stock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockMapper {
    int deleteByPrimaryKey(Long skuId);

    int insert(Stock record);

    int insertSelective(Stock record);

    Stock selectByPrimaryKey(Long skuId);

    int updateByPrimaryKeySelective(Stock record);

    int updateByPrimaryKey(Stock record);
}