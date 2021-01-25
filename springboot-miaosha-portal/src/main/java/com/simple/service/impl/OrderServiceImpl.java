package com.simple.service.impl;


import com.simple.dao.mapper.OrderMapper;
import com.simple.domain.Order;
import com.simple.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zhangshaolong001
 * @Date: 2021/1/13 8:59 下午
 * @Description：
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public int insert(Order record) {
        //TODO 分库分表
        return orderMapper.insertSelective(record);
    }

    @Override
    public int insertSelective(Order record) {
        return orderMapper.insertSelective(record);
    }

    @Override
    public Order selectByPrimaryKey(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(Order record) {
        return orderMapper.updateByPrimaryKeySelective(record);
    }
}
