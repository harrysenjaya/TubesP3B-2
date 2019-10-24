package com.example.tugasbesar2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener,GestureDetector.OnGestureListener,IMainActivity {
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
    GestureDetector gestureDetector;
    ArrayList<Enemy> enemies = new ArrayList<>();
    Pauser pauser;
    boolean run;
    boolean pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.play = findViewById(R.id.play);
        this.ivCanvas = findViewById(R.id.iv_canvas);
        this.gestureDetector = new GestureDetector(this,this);
        this.objUIWrapper = new UIThreadedWrapper(this);
        this.play.setOnClickListener(this);
        this.ivCanvas.setOnTouchListener(this);
        this.pauser = new Pauser();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == this.play.getId()) {
            if (!this.run) {
                initiateCanvas();
                this.run = true;
            } else {
                if(!this.pause) {
                    this.pauser.pause();
                    this.pause = true;
                }
                else{
                    this.pauser.resume();
                    this.pause = false;
                }
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return this.gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            float screenX = motionEvent.getX();
            if(screenX>this.ivCanvas.getWidth()/2){
                this.playerMoveThread = new PlayerMoveThread(this.objUIWrapper,this.ivCanvas.getWidth(),this.player, true, this.pauser);
                this.playerMoveThread.start();
            }
            else{

                this.playerMoveThread = new PlayerMoveThread(this.objUIWrapper,this.ivCanvas.getWidth(),this.player, false,this.pauser);
                this.playerMoveThread.start();
            }
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Thread.interrupted();
        }
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
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

        this.enemyThread = new EnemyThread(this.objUIWrapper, this.ivCanvas.getWidth(), this.ivCanvas.getHeight(),this.pauser);
        //    this.playerThread = new PlayerThread(this.objUIWrapper);
        this.enemyThread.start();
        //    this.playerThread.start();
        this.enemyMoveThread = new EnemyMoveThread(this.objUIWrapper, this.ivCanvas.getHeight(), this.enemies,this.pauser);
        this.enemyMoveThread.start();
    }

    public void resetCanvas() {
        int mColorBackground = ResourcesCompat.getColor(getResources(), R.color.background, null);
        mCanvas.drawColor(mColorBackground);

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