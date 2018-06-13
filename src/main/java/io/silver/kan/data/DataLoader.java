package io.silver.kan.data;


import io.silver.kan.bean.OfflineCouponRecord;
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
                .config("spark.master", "local")
                .config("spark.some.config.option", "some-value")
                .getOrCreate();

        String csvFile = "./data/ccf_offline_stage1_test_revised.csv";

        Dataset<Row> df = spark.read().option("header","true").csv(csvFile).select("User_id");

        // 离散变量 string转化为 int index
        StringIndexer userIdStringIndexer = new StringIndexer().setInputCol("User_id").setOutputCol("user_id_index");
        df = userIdStringIndexer.fit(df).transform(df);

//        VectorIndexer userIdVectorIndexer = new VectorIndexer()
//                .setInputCol("User_id_index")
//                .setOutputCol("User_id_vec");
//        userIdVectorIndexer.fit(df);

        OneHotEncoderEstimator encoder = new OneHotEncoderEstimator()
                .setInputCols(new String[] {"user_id_index"})
                .setOutputCols(new String[] {"user_id_vec"});

        df = encoder.fit(df).transform(df);
        df.select("user_id_vec").show(false);

        BinaryClassificationMetrics metrics = new BinaryClassificationMetrics(df);

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
