package com.example.tugasbesar2;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy {
    private int x;
    private int y;
    //power Up
    private int healt;

    public Enemy(int x, int y, int healt) {
        this.x = x;
        this.y = y;
        this.healt = healt;
    }

    public int getHealt() {
        return healt;
    }

    public void setHealt(int healt) {
        this.healt = healt;
    }

    public int GetX(){
        return this.x;
    }

    public int GetY(){
        return this.y;
    }

    public void SetX(int x){
        this.x = x;
    }

    public void SetY(int y){
        this.y = y;
    }
}
