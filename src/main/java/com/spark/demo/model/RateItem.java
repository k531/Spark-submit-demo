package com.spark.demo.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class RateItem implements Serializable {
    public RateItem(Timestamp timestamp, long value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    private Timestamp timestamp;
    private long value;

    public RateItem() {
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
