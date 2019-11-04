package com.example.tugasbesar2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tugasbesar2.Model.Bullet;
import com.example.tugasbesar2.Model.Enemy;
import com.example.tugasbesar2.Model.Player;
import com.example.tugasbesar2.Thread.BulletMoveThread;
import com.example.tugasbesar2.Thread.BulletThread;
import com.example.tugasbesar2.Thread.EnemyMoveThread;
import com.example.tugasbesar2.Thread.EnemyThread;
import com.example.tugasbesar2.Thread.PlayerMoveThread;
import com.example.tugasbesar2.Presenter.PostCalculateTask;
import com.example.tugasbesar2.Presenter.UIThreadedWrapper;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener,IMainActivity, SensorEventListener {
    protected Bitmap mBitmap;
    protected ImageView ivCanvas;
    protected Canvas mCanvas;
    protected Paint paint;
    protected TextView skor_tv;
    protected TextView jarak_tv;
    protected TextView kill_tv;
    protected TextView highscore;
    protected EnemyThread enemyThread;
    protected EnemyMoveThread enemyMoveThread;
    protected PlayerMoveThread playerMoveThread;
    protected BulletThread bulletThread;
    protected BulletMoveThread bulletMoveThread;
    protected UIThreadedWrapper objUIWrapper;
    protected FloatingActionButton play;
    protected FloatingActionButton mode;
    protected SensorManager mSensorManager;
    protected Sensor accelerometer;
    protected Sensor magnetometer;
    protected PostCalculateTask postCalculateTask;

    protected float[] accelerometerReading;
    protected float[] magnetometerReading;
    protected boolean pause;
    protected boolean sensor;
    protected int skor;
    protected int jarak;
    protected int kill;

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
        this.objUIWrapper = new UIThreadedWrapper(this);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.magnetometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.postCalculateTask = new PostCalculateTask(this, this);
    }

    @Override
    public void onWindowFocusChanged(boolean focus){
        super.onWindowFocusChanged(focus);
        initiate();
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
                    objUIWrapper.getBullet();
                }
            }
        }
        return true;
    }


    public void setBulletThread(ArrayList<Bullet> bullets, Player player){
        this.bulletThread.setPlayer(player);
        this.bulletMoveThread.setBullets(bullets);
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
        Log.d("SENSOR AKTIF","");
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

    public void initiate() {
        this.jarak = 0;
        this.kill = 0;

        this.sensor = false;
        this.kill_tv.setText(0+"");
        this.skor_tv.setText(0+"");
        this.jarak_tv.setText(0+"");
        this.play.setOnClickListener(this);
        this.ivCanvas.setOnTouchListener(this);
        this.mode.setOnClickListener(this);
        this.postCalculateTask.executeGET(2017730067);

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
        ArrayList<Enemy> enemies = new ArrayList<>();
        ArrayList<Bullet> bullets = new ArrayList<>();
        objUIWrapper.setInitData(player,enemies,bullets);

        this.drawPlayer(x, (int) (ivCanvas.getHeight() - (ivCanvas.getHeight() * 0.3)));

    }

    public void setThread(Player player, ArrayList<Enemy> enemies, ArrayList<Bullet> bullets){
        Log.d("setthread", "mask");
        this.playerMoveThread = new PlayerMoveThread(this.objUIWrapper, this.ivCanvas.getWidth(), player);
        this.playerMoveThread.start();
        this.enemyThread = new EnemyThread(this.objUIWrapper, this.ivCanvas.getWidth(), this.ivCanvas.getHeight());
        this.enemyThread.start();
        this.enemyMoveThread = new EnemyMoveThread(this.objUIWrapper, this.ivCanvas.getHeight(), enemies);
        this.enemyMoveThread.start();
        this.bulletThread = new BulletThread(this.objUIWrapper,player);
        this.bulletThread.start();
        this.bulletMoveThread = new BulletMoveThread(this.objUIWrapper,bullets,enemies);
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


    public void setEnemy(Enemy enemy, Player pl, ArrayList<Enemy> enemies, ArrayList<Bullet> bullets) {
        int nextH=(int) Math.floor(Integer.parseInt(this.kill_tv.getText().toString()) / 5);
        enemy.setHealth(enemy.getHealth()+nextH);
        objUIWrapper.addEnemy(enemy);



    }

    public void drawSetEnemy(Player pl, ArrayList<Bullet> bullets, ArrayList<Enemy> enemies){
        this.bulletMoveThread.setEnemies(enemies);
        resetCanvas();
        this.drawPlayer(pl.getX(), pl.getY());
        for (int i = 0; i < enemies.size(); i++) {
            this.drawEnemy(enemies.get(i).GetX(), enemies.get(i).GetY());
        }
        for (int i = 0; i < bullets.size(); i++) {
            this.drawBullet(bullets.get(i).getX(), bullets.get(i).getY());
        }
        this.jarak+=1;
        this.jarak_tv.setText(this.jarak+"");
        this.skor = this.kill + this.jarak;
        this.skor_tv.setText(this.skor+"");
    }



    public void setEnemies(Player player, ArrayList<Enemy> enemies, ArrayList<Bullet> bullets) {

        resetCanvas();
        this.drawPlayer(player.getX(), player.getY());
        for (int i = 0; i < enemies.size(); i++) {
            this.drawEnemy(enemies.get(i).GetX(), enemies.get(i).GetY());
        }
        for (int i = 0; i < bullets.size(); i++) {
            this.drawBullet(bullets.get(i).getX(), bullets.get(i).getY());
        }
    }

    public void setPlayer(Player player, ArrayList<Enemy> enemies, ArrayList<Bullet> bullets) {
        resetCanvas();
        this.drawPlayer(player.getX(), player.getY());
        for (int i = 0; i < enemies.size(); i++) {
            this.drawEnemy(enemies.get(i).GetX(), enemies.get(i).GetY());
        }
        for (int i = 0; i < bullets.size(); i++) {
            this.drawBullet(bullets.get(i).getX(), bullets.get(i).getY());
        }
    }

    public void setBullet(Player player, Bullet bullet, ArrayList<Enemy> enemies, ArrayList<Bullet> bullets){
        resetCanvas();
        this.drawPlayer(player.getX(),player.getY());
        for (int i = 0; i < enemies.size(); i++) {
            this.drawEnemy(enemies.get(i).GetX(), enemies.get(i).GetY());
        }
        for (int i = 0; i < bullets.size(); i++) {
            this.drawBullet(bullets.get(i).getX(), bullets.get(i).getY());
        }

    }

    public void setBullets(Player player, ArrayList<Enemy> enemies, ArrayList<Bullet> bullets){
        resetCanvas();
        this.drawPlayer(player.getX(),player.getY());
        for (int i = 0; i < enemies.size(); i++) {
            this.drawEnemy(enemies.get(i).GetX(), enemies.get(i).GetY());
        }
        for (int i = 0; i < bullets.size(); i++) {
            this.drawBullet(bullets.get(i).getX(), bullets.get(i).getY());
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
        this.enemyThread.setGameOver(true);
        this.enemyMoveThread.setGameOver(true);
        this.bulletMoveThread.setGameOver(true);
        this.bulletThread.setGameOver(true);
        this.playerMoveThread.setGameOver(true);
        if(Integer.parseInt(this.highscore.getText().toString())<this.skor) {
            this.postCalculateTask.executePOST(2017730067, 1, this.skor);
        }
        this.mode.setOnClickListener(null);
        this.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiate();
            }
        });
    }

    @Override
    public void sendResult(String result){
        this.highscore.setText(result);
    }
}
