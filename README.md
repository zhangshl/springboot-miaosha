springboot-miaosha

**业务流转步骤：**

1、查询秒杀商品， /app/queryProduct

2、进行秒杀，获取秒杀资格，/app/secKill

3、提交订单，/app/submitOrder

4、支付，/app/payResult



**要解决哪些问题？**

1、性能问题，查询性能，插入性能、修改性能，预热内存

2、数据一致性问题，避免超卖和少卖

3、分库分表，如何分？时间、hash

4、分库分表后扩容

5、分库分表后数据查询问题，C端和B端用户怎么知道去哪个表查询同一个订单

6、开始按钮才有效，未开始置灰

7、限流，在哪限？

8、隔离



**OP保障：**

1、mysql高可用

2、redis高可用

3、rocketmq高可用