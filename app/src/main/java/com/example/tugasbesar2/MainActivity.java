package com.example.tugasbesar2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener,IMainActivity, SensorEventListener {
    Bitmap mBitmap;
    ImageView ivCanvas;
    Canvas mCanvas;
    Paint paint;
    TextView skor_tv;
    TextView jarak_tv;
    TextView kill_tv;
    TextView highscore;
    EnemyThread enemyThread;
    EnemyMoveThread enemyMoveThread;
    Player player;
    PlayerMoveThread playerMoveThread;
    BulletThread bulletThread;
    BulletMoveThread bulletMoveThread;
    UIThreadedWrapper objUIWrapper;
    FloatingActionButton play;
    FloatingActionButton mode;
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    Presenter presenter;
    SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;

    float[] accelerometerReading;
    float[] magnetometerReading;
    boolean pause;
    boolean sensor;
    int skor;
    int jarak;
    int kill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.play = findViewById(R.id.play);
        this.ivCanvas = findViewById(R.id.iv_canvas);
        this.skor_tv = findViewById(R.id.skor);
        this.jarak_tv = findViewById(R.id.jarak);
        this.kill_tv = findViewById(R.id.kill);
        this.highscore = findViewById(R.id.nilaiTertingginya);
        this.mode = findViewById(R.id.sensor);
        this.sensor = false;
        this.jarak = 0;
        this.kill = 0;
        this.presenter = new Presenter(this);
        this.objUIWrapper = new UIThreadedWrapper(this);
        this.play.setOnClickListener(this);
        this.ivCanvas.setOnTouchListener(this);
        this.mode.setOnClickListener(this);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.magnetometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
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
        else if(view.getId() == this.mode.getId()){
            if(this.sensor){
                this.sensor = false;
                this.playerMoveThread.setPaused(true);
            }
            else{
                this.sensor = true;
                this.playerMoveThread.setPaused(true);
            }
        }
        }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!this.pause) {
            if (!this.sensor) {
                this.playerMoveThread.setPaused(false);
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    float screenX = motionEvent.getX();
                    if (screenX > this.ivCanvas.getWidth() / 2) {
                        this.playerMoveThread.setKanan(true);
                    } else {
                        this.playerMoveThread.setKanan(false);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    this.playerMoveThread.setPaused(true);
                    this.bulletThread.setPlayer(this.player);
                    this.bulletMoveThread.setBullets(this.bullets);
                }
            }
        }
        return true;
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(this.accelerometer != null){
            this.mSensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
        if(this.magnetometer != null){
            this.mSensorManager.registerListener(this, this.magnetometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        this.mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(this.sensor) {
            int sensorType = event.sensor.getType();
            switch (sensorType) {
                case Sensor.TYPE_ACCELEROMETER:
                    this.accelerometerReading = event.values.clone();

                case Sensor.TYPE_MAGNETIC_FIELD:
                    this.magnetometerReading = event.values.clone();
            }

            final float[] rotationMatrix = new float[9];
            mSensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading);

            final float[] orientationAngles = new float[3];
            mSensorManager.getOrientation(rotationMatrix, orientationAngles);

            float roll = orientationAngles[2];

            if (Math.abs(roll) < 0.2f) {
                roll = 0;
                this.playerMoveThread.setPaused(true);
            }

            if(roll>0){
                this.playerMoveThread.setPaused(false);
                this.playerMoveThread.setKanan(true);
            }
            else if(roll<0){
                this.playerMoveThread.setPaused(false);
                this.playerMoveThread.setKanan(false);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i ){

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
    }

    public void resetCanvas() {
        this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        this.ivCanvas.invalidate();
    }


    public void drawPlayer(int x, int y) {
        int halfWidth = this.ivCanvas.getWidth()/8;
        Path path = new Path();
        path.moveTo(x, y - ((halfWidth/8)-300)); //titik atas
        path.lineTo(x - (halfWidth-70), y + (halfWidth+300)); // titik kiri bawah
        path.lineTo(x + (halfWidth-70), y + (halfWidth+300)); // titik kanan bawah
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
        this.jarak+=1;
        this.jarak_tv.setText(this.jarak+"");
        this.skor = this.kill + this.jarak;
        this.skor_tv.setText(this.skor+"");
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

    public void kill(){
            this.kill+=1;
            this.kill_tv.setText(this.kill+"");
            this.skor = this.kill + this.jarak;
            this.skor_tv.setText(this.skor+"");
    }

    @Override
    public void gameOver(){
        this.enemyMoveThread.setPaused(true);
        this.enemyThread.setPaused(true);
        this.bulletThread.setPaused(true);
        this.bulletMoveThread.setPaused(true);
        this.playerMoveThread.setPaused(true);
        this.pause = true;
        this.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCanvas();
            }
        });
//        final Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.gameover);
//        Button close = dialog.findViewById(R.id.close);
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
    }

    @Override
    public void sendResult(Result result){
        String[] res = result.getResult();
        String temp ="";
        boolean flag = true;
        for(int i=0; i<res.length;i++){
            if(flag){
                temp+= res[i];
                flag = false;
            }
            else{
                temp+=", " +res[i];
            }
        }
        this.skor_tv.setText(temp);
    }
}
