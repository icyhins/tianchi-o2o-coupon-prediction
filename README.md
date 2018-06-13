# 天池新人实战赛《生活大实惠：O2O优惠券使用预测》
```
本赛题的比赛背景：随着移动设备的完善和普及，移动互联网+各行各业进入了高速发展阶段，
这其中以O2O（Online to Offline）消费最为吸引眼球。

据不完全统计，O2O行业估值上亿的创业公司至少有10家，也不乏百亿巨头的身影。

O2O行业天然关联数亿消费者，各类APP每天记录了超过百亿条用户行为和位置记录，
因而成为大数据科研和商业化运营的最佳结合点之一。

以优惠券盘活老用户或吸引新客户进店消费是O2O的一种重要营销方式。然而随机投放的优惠券对多数用户造成无意义的干扰。
对商家而言，滥发的优惠券可能降低品牌声誉，同时难以估算营销成本。 

个性化投放是提高优惠券核销率的重要技术，它可以让具有一定偏好的消费者得到真正的实惠，同时赋予商家更强的营销能力。
本次大赛为参赛选手提供了O2O场景相关的丰富数据，希望参赛选手通过分析建模，精准预测用户是否会在规定时间内使用相应优惠券。
```

## 数据
```
本赛题提供用户在2016年1月1日至2016年6月30日之间真实线上线下消费行为，预测用户在2016年7月领取优惠券后15天以内的使用情况。 
```
注意： 为了保护用户和商家的隐私，所有数据均作匿名处理，同时采用了有偏采样和必要过滤。

## 评价方式
```
本赛题目标是预测投放的优惠券是否核销。
针对此任务及一些相关背景知识，使用优惠券核销预测的平均AUC（ROC曲线下面积）作为评价标准。 
即对每个优惠券coupon_id单独计算核销预测的AUC值，再对所有优惠券的AUC值求平均作为最终的评价标准。 
```
关于AUC的含义与具体计算方法，可参考[维基百科](https://zh.wikipedia.org/wiki/ROC曲线)。

## 字段表

### Table 1: 用户线下消费和优惠券领取行为

Field       |	    Description
------------|------------------
User_id     |   用户ID
Merchant_id |   商户ID
Coupon_id   |   优惠券ID：null表示无优惠券消费，此时Discount_rate和Date_received字段无意义
Discount_rate|   优惠率：x \in `[0,1]` 代表折扣率；
<br/>            |x:y表示满x减y。单位是元
Distance    |   user经常活动的地点离该merchant的最近门店距离是x*500米（如果是连锁店，则取最近的一家门店），x \in [0,10] ；null表示无此信息，0表示低于500米，10表示大于5公里；
Date_received|   领取优惠券日期
Date    |   消费日期：如果Date=null & Coupon_id != null，该记录表示领取优惠券但没有使用，即负样本；
<br/>         |   如果Date!=null & Coupon_id = null，则表示普通消费日期；
<br/>         |   如果Date!=null & Coupon_id != null，则表示用优惠券消费日期，即正样本；

### Table 2: 用户线上点击/消费和优惠券领取行为

Field       |	    Description
------------|------------------
User_id     |   用户ID
Merchant_id |   商户ID
Action      |   0 点击， 1购买，2领取优惠券
Coupon_id   |   优惠券ID：null表示无优惠券消费，此时Discount_rate和Date_received字段无意义。“fixed”表示该交易是限时低价活动。
Discount_rate   |   优惠率：x \in `[0,1]` 代表折扣率；x:y表示满x减y；“fixed”表示低价限时优惠；
Date_received   |   领取优惠券日期
Date    |   消费日期：如果Date=null & Coupon_id != null，该记录表示领取优惠券但没有使用；如果Date!=null & Coupon_id = null，则表示普通消费日期；如果Date!=null & Coupon_id != null，则表示用优惠券消费日期；

### Table 3：用户O2O线下优惠券使用预测样本

Field       |	    Description
------------|------------------
User_id     |   用户ID
Merchant_id |   商户ID
Coupon_id   |   优惠券ID
Discount_rate   |   优惠率：x \in `[0,1]` 代表折扣率；x:y表示满x减y.
Distance    |   user经常活动的地点离该merchant的最近门店距离是x*500米（如果是连锁店，则取最近的一家门店），x\in`[0,10]`；null表示无此信息，0表示低于500米，10表示大于5公里；
Date_received   |   领取优惠券日期

### Table 4：选手提交文件字段，其中user_id,coupon_id和date_received均来自Table 3,而Probability为预测值

Field       |	    Description
------------|------------------
User_id     |     用户ID
Coupon_id   |   优惠券ID
Date_received|   领取优惠券日期
Probability |   15天内用券概率，由参赛选手给出

