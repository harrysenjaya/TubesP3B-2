package com.example.tugasbesar2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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
    BulletThread bulletThread;
    BulletMoveThread bulletMoveThread;
    UIThreadedWrapper objUIWrapper;
    FloatingActionButton play;
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    Presenter presenter;
    boolean pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.play = findViewById(R.id.play);
        this.ivCanvas = findViewById(R.id.iv_canvas);
        this.presenter = new Presenter(this);
        this.objUIWrapper = new UIThreadedWrapper(this);
        this.play.setOnClickListener(this);
        this.ivCanvas.setOnTouchListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean focus){
        super.onWindowFocusChanged(focus);
        initiateCanvas();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == this.play.getId()) {
                if(!this.pause) {
                    this.enemyMoveThread.setPaused(true);
                    this.enemyThread.setPaused(true);
                    this.bulletThread.setPaused(true);
                    this.bulletMoveThread.setPaused(true);
                    this.pause = true;
                }
                else if(this.pause){
                    this.enemyMoveThread.setPaused(false);
                    this.enemyThread.setPaused(false);
                    this.playerMoveThread.setPaused(true);
                    this.bulletThread.setPaused(false);
                    this.bulletMoveThread.setPaused(false);
                    this.pause = false;
                }
            }
        }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!this.pause) {
            this.playerMoveThread.setPaused(false);
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d("ACTION DOWN", "");
                float screenX = motionEvent.getX();
                if (screenX > this.ivCanvas.getWidth() / 2) {
                    this.playerMoveThread.setKanan(true);
                } else {
                    this.playerMoveThread.setKanan(false);
                    Log.d("SET", "");
                }
            }

            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                this.playerMoveThread.setPaused(true);
                this.bulletThread.setPlayer(this.player);
                this.bulletMoveThread.setBullets(this.bullets);
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

        resetCanvas();
        int x = ivCanvas.getWidth() / 2;
        int y = ivCanvas.getHeight() / 2;

        Player player = new Player(x, (int) (ivCanvas.getHeight() - (ivCanvas.getHeight() * 0.3)));
        this.player = player;
        this.drawPlayer(x, (int) (ivCanvas.getHeight() - (ivCanvas.getHeight() * 0.3)));
        this.playerMoveThread = new PlayerMoveThread(this.objUIWrapper, this.ivCanvas.getWidth(), this.player);
        this.playerMoveThread.start();
        this.enemyThread = new EnemyThread(this.objUIWrapper, this.ivCanvas.getWidth(), this.ivCanvas.getHeight());
        this.enemyThread.start();
        this.enemyMoveThread = new EnemyMoveThread(this.objUIWrapper, this.ivCanvas.getHeight(), this.enemies, this.presenter);
        this.enemyMoveThread.start();
        this.bulletThread = new BulletThread(this.objUIWrapper,this.player);
        this.bulletThread.start();
        this.bulletMoveThread = new BulletMoveThread(this.objUIWrapper,this.bullets,this.enemies);
        this.bulletMoveThread.start();
            Log.d("TES","TES");
    }

    public void resetCanvas() {
        this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        this.ivCanvas.invalidate();
    }


    public void drawPlayer(int x, int y) {
        int halfWidth = this.ivCanvas.getWidth()/8;
        Path path = new Path();
        path.moveTo(x, y - ((halfWidth/8)-300)); //titik atas
        path.lineTo(x - (halfWidth-20), y + (halfWidth+300)); // titik kiri bawah
        path.lineTo(x + (halfWidth-20), y + (halfWidth+300)); // titik kanan bawah
        path.lineTo(x, y - ((halfWidth/8)-300));
        path.close();
        this.mCanvas.drawPath(path, paint);
    }

    public void drawBullet(int x, int y){
        Rect rectangle = new Rect(x+10 , y + 350, x - 10, y + 300);
        this.mCanvas.drawRect(rectangle, paint);
    }

    public void drawEnemy(int x, int y) {
        this.mCanvas.drawCircle(x, y, 75, this.paint);
    }


    public void setEnemy(Enemy enemy) {
        this.enemies.add(enemy);
        Player player = this.player;
        this.bulletMoveThread.setEnemies(this.enemies);
        resetCanvas();
        this.drawPlayer(player.getX(), player.getY());
        for (int i = 0; i < this.enemies.size(); i++) {
            this.drawEnemy(this.enemies.get(i).GetX(), this.enemies.get(i).GetY());
        }
        for (int i = 0; i < this.bullets.size(); i++) {
            this.drawBullet(this.bullets.get(i).getX(), this.bullets.get(i).getY());
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
        for (int i = 0; i < this.bullets.size(); i++) {
            this.drawBullet(this.bullets.get(i).getX(), this.bullets.get(i).getY());
        }
    }

    public void setPlayer(Player player) {
        resetCanvas();
        this.player = player;
        this.drawPlayer(player.getX(), player.getY());
        for (int i = 0; i < this.enemies.size(); i++) {
            this.drawEnemy(this.enemies.get(i).GetX(), this.enemies.get(i).GetY());
        }
        for (int i = 0; i < this.bullets.size(); i++) {
            this.drawBullet(this.bullets.get(i).getX(), this.bullets.get(i).getY());
        }
    }

    public void setBullet(Bullet bullet){
        this.bullets.add(bullet);
        resetCanvas();
        this.drawPlayer(player.getX(),player.getY());
        for (int i = 0; i < this.enemies.size(); i++) {
            this.drawEnemy(this.enemies.get(i).GetX(), this.enemies.get(i).GetY());
        }
        for (int i = 0; i < this.bullets.size(); i++) {
            this.drawBullet(this.bullets.get(i).getX(), this.bullets.get(i).getY());
        }

    }

    public void setBullets(ArrayList<Bullet> bullets){
        this.bullets = bullets;
        resetCanvas();
        this.drawPlayer(player.getX(),player.getY());
        for (int i = 0; i < this.enemies.size(); i++) {
            this.drawEnemy(this.enemies.get(i).GetX(), this.enemies.get(i).GetY());
        }
        for (int i = 0; i < this.bullets.size(); i++) {
            this.drawBullet(this.bullets.get(i).getX(), this.bullets.get(i).getY());
        }

    }

//    @Override
//    public void gameOver(){
//        this.enemyMoveThread.setPaused(true);
//        this.enemyThread.setPaused(true);
//        this.playerMoveThread.setPaused(true);
//        Dialog settingsDialog = new Dialog(this);
//        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.gameover, null));
//        settingsDialog.show();
//    }
}
