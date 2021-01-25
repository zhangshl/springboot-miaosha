package com.simple.service;

import com.simple.domain.Order;

public interface OrderService {

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Order record);
}