package com.simple.dao.mapper;

import com.simple.domain.SkStock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SkStockMapper {
    int deleteByPrimaryKey(Long skuId);

    int insert(SkStock record);

    int insertSelective(SkStock record);

    SkStock selectByPrimaryKey(Long skuId);

    int updateByPrimaryKeySelective(SkStock record);

    int updateByPrimaryKey(SkStock record);
}