package com.ghani.qrscanner.qrcodereader;

public class DataModel {

    private long id;
    private String type, timestamp, value;

    public DataModel(long id, String type, String timestamp, String value) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.value = value;
    }

    public long getId() {
        return id;
    }
    public String getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getValue() {
        return value;
    }
}
