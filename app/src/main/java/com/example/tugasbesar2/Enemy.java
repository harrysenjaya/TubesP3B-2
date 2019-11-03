package com.example.tugasbesar2;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy {
    private int x;
    private int y;
    //power Up
    private int health;

    public Enemy(int x, int y, int health) {
        this.x = x;
        this.y = y;
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int healt) {
        this.health = healt;
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
