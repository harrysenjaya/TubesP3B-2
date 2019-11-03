package com.example.tugasbesar2;

public class Input {

    private String value;
    private String api_key;
    private String order;

    public Input(String api_key, String order, String value){
        this.api_key = api_key;
        this.order = order;
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getApi_key() {
        return api_key;
    }

    public String getOrder() {
        return order;
    }

    public String getValue() {
        return value;
    }
}

