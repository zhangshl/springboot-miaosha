package com.simple.dao.mapper;

import com.simple.domain.SkOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SkOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SkOrder record);

    int insertSelective(SkOrder record);

    SkOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SkOrder record);

    int updateByPrimaryKey(SkOrder record);
}