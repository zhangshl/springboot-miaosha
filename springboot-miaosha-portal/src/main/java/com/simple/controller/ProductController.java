package com.simple.controller;

import com.simple.domain.SkGoods;
import com.simple.service.ProductService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * @author zhangshl
 * 查询秒杀商品接口
 */
@Controller
@RequestMapping("/app")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 查询秒杀商品
     *
     * @param skuId
     * @return
     * @ApiLimit 为本地限流
     */
    @RequestMapping("/queryProduct")
    @ResponseBody
    public SkGoods queryProduct(@Param("skuId") Long skuId) {
        return productService.selectByPrimaryKey(skuId);
    }

    /**
     * 增加商品
     *
     * @return
     */
    @RequestMapping("/insertProduct")
    @ResponseBody
    public int insertProduct() {
        SkGoods skGoods = SkGoods.builder().goodsTitle("苹果手机").goodsName("iPhone12").goodsPrice(new BigDecimal(1)).goodsStock(100).build();
        return productService.insert(skGoods);
    }

    /**
     * 重置商品库存
     *
     * @param skuId
     * @return
     */
    @RequestMapping("/reset")
    @ResponseBody
    public SkGoods reset(@Param("skuId") Long skuId) {
        return productService.selectByPrimaryKey(skuId);
    }

}
