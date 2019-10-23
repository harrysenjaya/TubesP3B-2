package com.example.tugasbesar2;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.util.ArrayList;

public class UIThreadedWrapper extends Handler {

    protected final static int MSG0=0;
    protected final static int MSG1=1;
    protected MainActivity mainActivity;

    public UIThreadedWrapper(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void handleMessage(Message msg){
        if(msg.what==UIThreadedWrapper.MSG0){
            Enemy parameter = (Enemy) msg.obj;
            this.mainActivity.setEnemy(parameter);
        }
        else if(msg.what==UIThreadedWrapper.MSG1){
            ArrayList<Enemy> parameter = (ArrayList<Enemy>) msg.obj;
            this.mainActivity.setEnemies(parameter);
        }
    }

    public void setEnemy(Enemy enemy) {
        Message msg = new Message();
        msg.what = MSG0;
        msg.obj = enemy;
        this.sendMessage(msg);
    }

    public void setEnemies(ArrayList<Enemy> enemies){
        Message msg = new Message();
        msg.what = MSG1;
        msg.obj = enemies;
        this.sendMessage(msg);
    }


}
