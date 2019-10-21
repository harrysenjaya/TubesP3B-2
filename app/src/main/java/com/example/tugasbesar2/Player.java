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

    public void drawTriangle(Canvas canvas, Paint paint, int x, int y){
        int halfWidth = this.width/2;
        Path path = new Path();
        path.moveTo(x, y - halfWidth); //titik atas
        path.lineTo(x - halfWidth, y + halfWidth); // titik kiri bawah
        path.lineTo( x+ halfWidth, y + halfWidth); // titik kanan bawah
        path.lineTo( x, y - halfWidth);
        path.close();

        canvas.drawPath(path ,paint);

    }
}
