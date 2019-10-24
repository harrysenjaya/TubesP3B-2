package com.example.tugasbesar2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Player {
    private int x;
    private int y;
    private int width;


    public Player(int x, int y){
        this.x = x;
        this.y = y;
        this.width = 100;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
