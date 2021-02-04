package com.simple.service.impl;

import com.simple.dao.mapper.StockMapper;
import com.simple.domain.Stock;
import com.simple.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zhangshaolong001
 * @Date: 2021/2/4 3:00 下午
 * @Description：库存服务
 */
@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockMapper stockMapper;

    @Override
    public int insert(Stock record) {
        return 0;
    }

    @Override
    public int insertSelective(Stock record) {
        return 0;
    }

    @Override
    public Stock selectByPrimaryKey(Long skuId) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(Long skuId, int buyNum) {
        Stock stock = stockMapper.selectByPrimaryKey(skuId);
        if (stock.getLeftStock() < buyNum){
            return -99;
        }

        stock.setLeftStock(stock.getLeftStock() - buyNum);
        //修改库存
        return stockMapper.updateByPrimaryKeySelective(stock);
    }

    @Override
    public int updateByPrimaryKey(Stock record) {
        return 0;
    }
}
