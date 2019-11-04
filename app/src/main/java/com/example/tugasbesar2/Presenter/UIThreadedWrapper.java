package com.example.tugasbesar2.Presenter;


import android.os.Handler;
import android.os.Message;

import com.example.tugasbesar2.MainActivity;
import com.example.tugasbesar2.Model.Bullet;
import com.example.tugasbesar2.Model.Enemy;
import com.example.tugasbesar2.Model.Player;

import java.util.ArrayList;

public class UIThreadedWrapper extends Handler {

    protected final static int setEnemy=0;
    protected final static int setEnemies=1;
    protected final static int setPlayer=2;
    protected final static int setBullet=3;
    protected final static int setBullets=4;
    protected final static int kill=5;
    protected final static int gameOver=6;
    protected final static int init=7;


    protected Enemy enemy;
    protected ArrayList<Enemy> enemies = new ArrayList<>();
    protected Player player;
    protected Bullet bullet;
    protected ArrayList<Bullet> bullets = new ArrayList<>();



    protected MainActivity mainActivity;

    public UIThreadedWrapper(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void handleMessage(Message msg){
        if(msg.what==UIThreadedWrapper.setEnemy){
            Enemy parameter = (Enemy) msg.obj;
            this.mainActivity.setEnemy(parameter, this.player, this.enemies, this.bullets);
        }
        else if(msg.what==UIThreadedWrapper.setEnemies){
            ArrayList<Enemy> parameter = (ArrayList<Enemy>) msg.obj;
            this.enemies = parameter;
            this.mainActivity.setEnemies(player, this.enemies, this.bullets);
        }
        else if(msg.what==UIThreadedWrapper.setPlayer){
            Player parameter = (Player) msg.obj;
            this.player = parameter;
            this.mainActivity.setPlayer(player, this.enemies, this.bullets);
        }
        else if(msg.what==UIThreadedWrapper.setBullet){
            Bullet parameter = (Bullet) msg.obj;
            this.bullets.add(parameter);
            this.mainActivity.setBullet(player, bullet, this.enemies, this.bullets);
        }
        else if(msg.what==UIThreadedWrapper.setBullets){
            ArrayList<Bullet> parameter = (ArrayList<Bullet>) msg.obj;
            this.bullets = parameter;
            this.mainActivity.setBullets(this.player, this.enemies, this.bullets);
        }
        else if(msg.what==UIThreadedWrapper.kill){
            this.mainActivity.kill();
        }
        else if(msg.what==UIThreadedWrapper.gameOver){
            this.mainActivity.gameOver();
        }
        else if(msg.what==UIThreadedWrapper.init){
            this.mainActivity.setThread(player, enemies, bullets);
        }
    }

    public void addEnemy(Enemy enemy){
        this.enemies.add(enemy);
        this.mainActivity.drawSetEnemy(this.player, this.bullets, this.enemies);
    }

    public void setEnemy(Enemy enemy) {
        Message msg = new Message();
        msg.what = setEnemy;
        msg.obj = enemy;
        this.sendMessage(msg);
    }

    public void setEnemies(ArrayList<Enemy> enemies){
        Message msg = new Message();
        msg.what = setEnemies;
        msg.obj = enemies;
        this.sendMessage(msg);
    }

    public void setPlayer(Player player){
        Message msg = new Message();
        msg.what = setPlayer;
        msg.obj = player;
        this.sendMessage(msg);
    }

    public void setBullet(Bullet bullet){
        Message msg = new Message();
        msg.what = setBullet;
        msg.obj = bullet;
        this.sendMessage(msg);
    }

    public void setBullets(ArrayList<Bullet> bullets){
        Message msg = new Message();
        msg.what = setBullets;
        msg.obj = bullets;
        this.sendMessage(msg);
    }

    public void kill(){
        Message msg = new Message();
        msg.what = kill;
        this.sendMessage(msg);
    }

    public void gameOver(){
        Message msg = new Message();
        msg.what = gameOver;
        this.sendMessage(msg);
    }

    public void setInitData(Player player, ArrayList<Enemy> enemies, ArrayList<Bullet> bullets){
        this.player = player;
        this.enemies = enemies;
        this.bullets = bullets;
        Message msg = new Message();
        msg.what = init;
        this.sendMessage(msg);
    }

    public void getBullet(){
        this.mainActivity.setBulletThread(bullets, player);
    }

}
