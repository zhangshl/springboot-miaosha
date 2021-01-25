package com.simple.dao.mapper;

import com.simple.domain.SkGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SkGoodsMapper {
    int insert(SkGoods record);

    SkGoods selectByPrimaryKey(Long skuId);

    int updateByPrimaryKey(@Param("skuId") Long skuId, @Param("number") int number);
}