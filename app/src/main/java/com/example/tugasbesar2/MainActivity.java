package com.example.tugasbesar2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Bitmap mBitmap;
    ImageView ivCanvas;
    Button start;
    Canvas mCanvas;

    Paint paint;
    Player p1;
    Enemy enemy1;

    int x;
    int y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.start = findViewById(R.id.btn_start);
        this.ivCanvas = findViewById(R.id.iv_canvas);
        start.setOnClickListener(this);
        this.p1 = new Player(x, y);
        this.enemy1 = new Enemy(x, y);

    }

    @Override
    public void onClick(View view) {
        initiateCanvas();
    }

    public void initiateCanvas() {
        mBitmap = Bitmap.createBitmap(ivCanvas.getWidth(), ivCanvas.getHeight(), Bitmap.Config.ARGB_8888);

        this.ivCanvas.setImageBitmap(mBitmap);

        this.mCanvas = new Canvas(mBitmap);

        this.paint = new Paint();
        int mColorTest = ResourcesCompat.getColor(getResources(), R.color.colorAccent, null);
        this.paint.setColor(mColorTest);
        Path path = new Path();

        resetCanvas();
        this.x = ivCanvas.getWidth()/2;
        this.y = ivCanvas.getHeight()/2;

        p1.drawTriangle(mCanvas, paint, x, y);
        enemy1.drawEnemy(mCanvas, paint, x, y);

    }

    public void resetCanvas() {

        int mColorBackground = ResourcesCompat.getColor(getResources(),R.color.background, null);
        mCanvas.drawColor(mColorBackground);

        this.ivCanvas.invalidate();
    }


    public void drawEnemy(Canvas canvas, Paint paint, int x, int y){
        mCanvas.drawCircle(x/2 , y/2,75, paint );
    }

}
