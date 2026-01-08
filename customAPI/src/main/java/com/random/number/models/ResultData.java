package com.random.number.models;

public class ResultData {
    private Object result;

    // Constructor for double
    public ResultData(double result) {
        this.result = result;
    }

    // Constructor for String
    public ResultData(String result) {
        this.result = result;
    }

    // Getter and Setter
    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
