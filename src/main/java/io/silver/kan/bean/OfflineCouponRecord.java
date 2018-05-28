package io.silver.kan.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author Silver.Kan
 * @date 2018/5/28
 * 用户线下消费和优惠券领取行为
 */
@Data
public class OfflineCouponRecord {

    /**
     * 用户 ID
     * */
    private String userId;

    /**
     * 商户 ID
     * */
    private String merchantId;

    /**
     * 优惠券 ID：
     * null表示无优惠券消费，此时Discount_rate和Date_received字段无意义
     * */
    private String couponId;

    /**
     * 优惠率,
     * x in `[0,1]` 代表折扣率
     * x:y表示满x减y
     * */
    private String discountRate;

    /**
     * user经常活动的地点离该 merchant 的最近门店距离
     * */
    private Double closestMerchantDistance;

    /**
     * 领取优惠券日期
     * */
    private Date receivedDate;

    /**
     * 消费日期
     * */
    private Date consumptionDate;


}
