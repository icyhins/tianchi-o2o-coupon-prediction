package io.silver.kan.data;


import io.silver.kan.bean.OfflineCouponRecord;
import io.silver.kan.o2o.coupon.OfflineO2OCouponDataEncoder;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.ml.feature.OneHotEncoderEstimator;
import org.apache.spark.ml.feature.OneHotEncoderModel;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorIndexer;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.spark.sql.functions.col;


/**
 * @author Silver.Kan
 * @date 2018/5/28
 */
public class DataLoader {

    public static void main(String[] args) throws AnalysisException {
        SparkSession spark = SparkSession.builder()
                .appName("O2O Coupon Prediction")
                // local[*] 表示使用所有 CPU
                .config("spark.master", "local[*]")
                .getOrCreate();

        String csvFile = "./data/ccf_offline_stage1_train.csv";

        Dataset<Row> df = spark.read().option("header","true").csv(csvFile);
        df.createOrReplaceTempView("ccf_offline_stage1_train");


        // SQL 转换处理
        df = spark.sql("SELECT * ," +
                // FLAG
                " CASE " +
                "   WHEN (Date_received = 'null' AND Date <> 'null') THEN 0 " +
                "   WHEN (Date_received <> 'null' AND Date = 'null') THEN -1 " +
                "   WHEN (Date_received <> 'null' AND Date <> 'null') THEN 1 " +
                " END " +
                "   AS flag " + "," +

                // Coupon ID 转换成有或无
                " CASE " +
                "   WHEN Coupon_id = 'null' THEN 0 ELSE 1 " +
                " END " +
                " AS coupon " + "," +

                // DISTANCE 加1
                " CASE " +
                "   WHEN Distance = 'null' THEN 0 ELSE Distance + 1 " +
                " END " +
                " AS distance1 " +
                " from ccf_offline_stage1_train ");

        // User ID 转换
        StringIndexer userIdStringIndexer = new StringIndexer().setInputCol("User_id").setOutputCol("user_id");
        df = userIdStringIndexer.fit(df).transform(df);

        // DISTANCE 归一化
        OneHotEncoderEstimator encoder = new OneHotEncoderEstimator()
                .setInputCols(new String[] {"distance1"})
                .setOutputCols(new String[] {"distance_one_hot"});
        df = encoder.fit(df).transform(df);

        df.show();


        // ML Pipeline

        // 训练集， 测试集


        // 分别使用决策树， 逻辑回归，进行训练



//
//        StringIndexer userIdStringIndexer = new StringIndexer().setInputCol("User_id").setOutputCol("user_id_index");
//        df = userIdStringIndexer.fit(df).transform(df);

//        VectorIndexer userIdVectorIndexer = new VectorIndexer()
//                .setInputCol("User_id_index")
//                .setOutputCol("User_id_vec");
//        userIdVectorIndexer.fit(df);

//        OneHotEncoderEstimator encoder = new OneHotEncoderEstimator()
//                .setInputCols(new String[] {"user_id_index"})
//                .setOutputCols(new String[] {"user_id_vec"});
//
//        df = encoder.fit(df).transform(df);
//        df.select("user_id_vec").show(false);
//
//        BinaryClassificationMetrics metrics = new BinaryClassificationMetrics(df);

//        OneHotEncoderEstimator encoder = new OneHotEncoderEstimator();
//        encoder.setInputCols(new String[]{"User_id"});
//        encoder.setOutputCols(new String[]{"User_id_vec"});
//
//        OneHotEncoderModel model = encoder.fit(df);
//        Dataset<Row> encodedDF = model.transform(df);
//
//        encodedDF.show();


//
//        df.createGlobalTempView("people");
//
//        spark.sql("select * from global_temp.people").show();
//
//        spark.newSession().sql("select * from global_temp.people").show();
//
//        OfflineCouponRecord record = new OfflineCouponRecord();
//
//        //Encoders
//        Encoder<OfflineCouponRecord> recordEncoder = Encoders.bean(OfflineCouponRecord.class);
//        Dataset<OfflineCouponRecord> recordDataset = spark.createDataset(
//                Collections.singletonList(record),
//                recordEncoder
//        );
//        recordDataset.show();
//
//        // Encoders for most common types are provided in class Encoders
//        Encoder<Integer> integerEncoder = Encoders.INT();
//        Dataset<Integer> primitiveDS = spark.createDataset(Arrays.asList(1, 2, 3), integerEncoder);
//        Dataset<Integer> transformedDS = primitiveDS.map(
//                (MapFunction<Integer, Integer>) value -> value + 1,
//                integerEncoder);
//        transformedDS.collect(); // Returns [2, 3, 4]
//
//        Dataset<OfflineCouponRecord> recordDf = spark.read().csv(csvFile).as(recordEncoder);
    }
}
