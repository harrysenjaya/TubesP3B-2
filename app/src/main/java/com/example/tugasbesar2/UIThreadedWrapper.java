package com.example.tugasbesar2;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.util.ArrayList;

public class UIThreadedWrapper extends Handler {

    protected final static int setEnemy=0;
    protected final static int setEnemies=1;
    protected final static int setPlayer=2;

    protected MainActivity mainActivity;

    public UIThreadedWrapper(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void handleMessage(Message msg){
        if(msg.what==UIThreadedWrapper.setEnemy){
            Enemy parameter = (Enemy) msg.obj;
            this.mainActivity.setEnemy(parameter);
        }
        else if(msg.what==UIThreadedWrapper.setEnemies){
            ArrayList<Enemy> parameter = (ArrayList<Enemy>) msg.obj;
            this.mainActivity.setEnemies(parameter);
        }
        else if(msg.what==UIThreadedWrapper.setPlayer){
            Player parameter = (Player) msg.obj;
            this.mainActivity.setPlayer(parameter);
        }
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

}
