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

    public void drawEnemy(Canvas canvas, Paint paint, int x, int y ){
        canvas.drawCircle(x/2, y/2, 75, paint);
    }
}
