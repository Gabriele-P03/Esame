package com.greenhouse.json;

public class JSONMap {

    private String key, value;

    public JSONMap(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\"" + this.key + "\":\"" + this.value + "\"";
    }
}
