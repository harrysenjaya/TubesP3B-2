package com.example.tugasbesar2;


import java.lang.reflect.Array;
import java.util.ArrayList;

public class EnemyMoveThread implements Runnable{
    protected Thread thread;
    protected UIThreadedWrapper uiThreadedWrapper;
    protected int height;
    protected ArrayList<Enemy> enemies;

    public EnemyMoveThread(UIThreadedWrapper uiThreadedWrapper, int height, ArrayList<Enemy> enemies){
        this.uiThreadedWrapper = uiThreadedWrapper;
        this.thread = new Thread(this);
        this.height = height;
        this.enemies = enemies;
    }

    public void start(){
        this.thread.start();
    }

    @Override
    public void run() {
        while(true) {
           for(int i = 0 ; i<this.enemies.size(); i++){
               enemies.get(i).SetY(enemies.get(i).GetY()+20);
           }
           this.uiThreadedWrapper.setEnemies(enemies);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
