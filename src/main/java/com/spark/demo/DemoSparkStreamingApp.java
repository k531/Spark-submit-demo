package com.spark.demo;

import com.spark.demo.model.RateItem;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

public class DemoSparkStreamingApp {
    public static void main(String[] args) throws TimeoutException, StreamingQueryException {

        // Initialize Spark Streaming Context
        SparkSession spark = SparkSession
                .builder()
                .appName("JavaStructuredNetworkWordCount")
                .getOrCreate();

        Dataset<Row> lines = spark
                .readStream()
                .format("rate")
                .option("rowsPerSecond", "100")
                .option("port", 9999)
                .load();

        Encoder<RateItem> rateModelEncoder = Encoders.bean(RateItem.class);

        // Split the lines into words
        Dataset<RateItem> evens = lines
                .as(rateModelEncoder)
                .map((MapFunction<RateItem, RateItem>) rateItem -> new RateItem(rateItem.getTimestamp(), ThreadLocalRandom.current().nextInt(0, 11)), rateModelEncoder)
                .filter((FilterFunction<RateItem>) rateModel -> rateModel.getValue() % 2 == 0);

        // Generate running word count
        Dataset<Row> wordCounts = evens.groupBy("value").count();

        // Start running the query that prints the running counts to the console
        StreamingQuery query = wordCounts.writeStream()
                .outputMode("update")
                .format("console")
                .start();

        query.awaitTermination();
    }
}
