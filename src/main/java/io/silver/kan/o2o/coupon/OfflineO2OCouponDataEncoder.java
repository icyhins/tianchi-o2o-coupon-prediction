package io.silver.kan.o2o.coupon;

import io.silver.kan.util.Constants;
import lombok.Data;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * @author Silver.Kan
 * @date 2018/6/24
 *
 * 数据特征处理
 */
public class OfflineO2OCouponDataEncoder {

    /**
     * 用户 ID 特征处理
     * */
    public Dataset<Row>  encodeUserId(Dataset<Row> rawDf){
        // TODO
        // 离散变量 string 转化为 int index
        StringIndexer indexer = new StringIndexer()
                .setInputCol(Constants.META_USER_ID)
                .setOutputCol(Constants.USER_ID_INDEX);

        Dataset<Row> resultDf = indexer.fit(rawDf).transform(rawDf);

        return resultDf;
    }

    /**
     *  门店 ID 特征处理
     *  目前认为门店 ID 不重要，暂不处理
     * */
    public Dataset<Row>  encodeMerchantId(Dataset<Row> rawDf){
        // TODO
        return null;
    }

    /**
     * 优惠券 ID 特征处理
     * 优惠券 ID 意义不大， 可转化为 有 -> 1 , 没有 -> 0
     * */
    public Dataset<Row>  encodeCouponId(Dataset<Row> rawDf){
        // TODO


        return null;
    }


    /**
     * 优惠率 特征处理
     * x:y表示满x减y
     *
     * */
    public Dataset<Row>  encodeCouponRate(Dataset<Row> rawDf){
        // TODO
        Dataset<Row> rateDf = rawDf.select(Constants.META_DISCOUNT_RATE);




        return null;
    }

    private Double convertRate(String str){

        if(str == null || str.equals("null")){
            return new Double(0);
        }

        if(str.contains(":")){
            String[] tmp = str.split(":");
            Double a = Double.parseDouble(tmp[0]);
            Double b = Double.parseDouble(tmp[1]);
            return b/a;
        }else{
            return Double.parseDouble(str);
        }
    }

    /**
     * 最近门店距离 特征处理
     * */
    public Dataset<Row>  encodeClosestMerchantDistance(Dataset<Row> rawDf){
        // TODO
        rawDf.select(Constants.META_DISTANCE);
        return null;
    }

    /**
     * 日期特征处理
     * */
    public Dataset<Row>  encodeDate(Dataset<Row> rawDf){
        // TODO
        rawDf.select(Constants.META_DATE_RECEIVED,Constants.META_DATE_CONSUMPTION);
        return null;
    }

    /**
     * 标记处理
     * 判断 优惠券日期是否为空
     * 判断 购买日期是否为空
     * Null & Not Null -> 普通反馈(0)
     * Null & Null -> 坏数据， 应该清除
     * Not Null & Not Null -> 正反馈（1）
     * Not Null & Null -> 负反馈(-1)
     * */
    public Dataset<Row> encodeFlag(Dataset<Row> rawDf){
        // TODO
        Dataset<Row> resultDf = rawDf.sqlContext().sql("SELECT * ," +
                " CASE " +
                "   WHEN (Date_received = 'null' AND Date <> 'null') THEN 0 " +
                "   WHEN (Date_received <> 'null' AND Date = 'null') THEN -1 " +
                "   WHEN (Date_received <> 'null' AND Date <> 'null') THEN 1 " +
                " END " +
                "   AS flag from ccf_offline_stage1_train ");

        return resultDf;
    }

}
