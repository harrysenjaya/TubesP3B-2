package com.example.tugasbesar2;

public class Input {

    private String[] expr;
    private int precision;

    public Input(String[] expr, int precision){
        this.expr = expr;
        this.precision = precision;
    }

    public int getPrecision(){
        return precision;
    }

    public String[] getExpr(){
        return expr;
    }

    public void setExpr(String[] expr){
        this.expr = expr;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }
}
