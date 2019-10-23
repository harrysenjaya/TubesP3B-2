package com.example.tugasbesar2;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy {
    private int x;
    private int y;


    public Enemy(int x, int y){
        this.x = x;
        this.y = y;
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
