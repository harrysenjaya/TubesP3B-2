package com.example.tugasbesar2;

public class Result {
    private String[] result;
    private String error;

    public Result(String[] res, String err){
        this.result = res;
        this.error = err;
    }

    public String getError() {
        return error;
    }

    public String[] getResult() {
        return result;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setResult(String[] result) {
        this.result = result;
    }
}
