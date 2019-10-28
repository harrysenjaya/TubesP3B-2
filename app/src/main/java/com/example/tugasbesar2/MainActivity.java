package com.example.tugasbesar2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener,IMainActivity {
    Bitmap mBitmap;
    ImageView ivCanvas;
    Canvas mCanvas;
    Paint paint;
    EnemyThread enemyThread;
    EnemyMoveThread enemyMoveThread;
    Player player;
    PlayerMoveThread playerMoveThread;
    UIThreadedWrapper objUIWrapper;
    FloatingActionButton play;
    ArrayList<Enemy> enemies = new ArrayList<>();
    boolean run;
    boolean pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.play = findViewById(R.id.play);
        this.ivCanvas = findViewById(R.id.iv_canvas);
        this.objUIWrapper = new UIThreadedWrapper(this);
        this.play.setOnClickListener(this);
        this.ivCanvas.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == this.play.getId()) {
            if (!this.run) {
                initiateCanvas();
                this.run = true;
            } else {
                if(!this.pause) {
                    this.enemyMoveThread.setPaused(true);
                    this.enemyThread.setPaused(true);
                    this.playerMoveThread.setPaused(true);
                    this.pause = true;
                }
                else if(this.pause){
                    this.enemyMoveThread.setPaused(false);
                    this.enemyThread.setPaused(false);
                    this.playerMoveThread.setPaused(false);
                    this.pause = false;
                }
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!this.pause) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                this.playerMoveThread.setPaused(false);
                Log.d("ACTION DOWN", "");
                float screenX = motionEvent.getX();
                if (screenX > this.ivCanvas.getWidth() / 2) {
                    this.playerMoveThread.setKanan(true);
                    this.playerMoveThread.start();
                } else {
                    this.playerMoveThread.setKanan(false);
                    this.playerMoveThread.start();
                }
            }

            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                this.playerMoveThread.setPaused(true);
            }
        }
        return true;
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
        int x = ivCanvas.getWidth() / 2;
        int y = ivCanvas.getHeight() / 2;

        Player player = new Player(x, (int) (ivCanvas.getHeight() - (ivCanvas.getHeight() * 0.3)));
        this.player = player;

        this.drawPlayer(x, (int) (ivCanvas.getHeight() - (ivCanvas.getHeight() * 0.3)));
        this.playerMoveThread = new PlayerMoveThread(this.objUIWrapper, this.ivCanvas.getWidth(), this.player);
        this.enemyThread = new EnemyThread(this.objUIWrapper, this.ivCanvas.getWidth(), this.ivCanvas.getHeight());
        this.enemyThread.start();
        this.enemyMoveThread = new EnemyMoveThread(this.objUIWrapper, this.ivCanvas.getHeight(), this.enemies);
        this.enemyMoveThread.start();
    }

    public void resetCanvas() {
        this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        this.ivCanvas.invalidate();
    }


    public void drawPlayer(int x, int y) {
        int halfWidth = x / 2;
        Path path = new Path();
        path.moveTo(x, y - halfWidth); //titik atas
        path.lineTo(x - halfWidth, y + halfWidth); // titik kiri bawah
        path.lineTo(x + halfWidth, y + halfWidth); // titik kanan bawah
        path.lineTo(x, y - halfWidth);
        path.close();

        this.mCanvas.drawPath(path, paint);

    }

    public void drawEnemy(int x, int y) {
        this.mCanvas.drawCircle(x / 2, y / 2, 75, this.paint);
    }

    public void setEnemy(Enemy enemy) {
        this.enemies.add(enemy);
        Player player = this.player;
        resetCanvas();
        this.drawPlayer(player.getX(), player.getY());
        for (int i = 0; i < this.enemies.size(); i++) {
            this.drawEnemy(this.enemies.get(i).GetX(), this.enemies.get(i).GetY());
        }
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        Player player = this.player;
        this.enemies = enemies;
        resetCanvas();
        this.drawPlayer(player.getX(), player.getY());
        for (int i = 0; i < this.enemies.size(); i++) {
            this.drawEnemy(this.enemies.get(i).GetX(), this.enemies.get(i).GetY());
        }
    }

    public void setPlayer(Player player) {
        resetCanvas();
        this.player = player;
        this.drawPlayer(player.getX(), player.getY());
        for (int i = 0; i < this.enemies.size(); i++) {
            this.drawEnemy(this.enemies.get(i).GetX(), this.enemies.get(i).GetY());
        }
    }
}
