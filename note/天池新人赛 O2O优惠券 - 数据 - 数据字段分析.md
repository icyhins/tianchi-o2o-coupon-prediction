# 天池新人赛 O2O优惠券 - 数据 - 数据字段分析

User_id,Merchant_id,Coupon_id,Discount_rate,Distance,Date_received,Date

## 离散值
User_id， Merchant_id，

StringIndexer



## 连续值


## 日期值
正反馈
Date -  Date_received  天数
普通反馈 0
负反馈 -1

## 特殊值
优惠率有满减券（满 X 减 Y）
或 以明确的折扣
应该是分成2种还是也把满减券当成折扣呢？
例如（x-y）/ x.

即 Coupon Tyoe 1 以及 Coupon Type 2
并有对应的 coupon rate 1 & coupon rate 2

## Spark SQL
样例
``` sql
select CASE WHEN y = 2 THEN 'A' ELSE 'B' END AS flag, x from test
```

## 标记值
``` sql
select flag from ccf_offline_stage1_train
-- 有收到 Coupon 并使用 Coupon 消费，正反馈
if (Date_received != null && Date != null) then '1'

-- 没有使用 Coupon 的普通消费，普通反馈
else if (Date_received == null && Date != null) then '0'

-- 有收到 Coupon 但没有消费，负反馈
else if (Date_received != null && Date == null) then '-1'

```

