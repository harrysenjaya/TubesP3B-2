package com.example.tugasbesar2;


import java.lang.reflect.Array;
import java.util.ArrayList;

public class EnemyMoveThread implements Runnable{
    protected Thread thread;
    protected UIThreadedWrapper uiThreadedWrapper;
    protected int height;
    protected ArrayList<Enemy> enemies;
    protected Pauser pauser;

    public EnemyMoveThread(UIThreadedWrapper uiThreadedWrapper, int height, ArrayList<Enemy> enemies, Pauser pauser){
        this.uiThreadedWrapper = uiThreadedWrapper;
        this.thread = new Thread(this);
        this.height = height;
        this.enemies = enemies;
        this.pauser = pauser;
    }

    public void start(){
        this.thread.start();
    }

    @Override
    public void run() {
        while(true) {
            try {
                this.pauser.look();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
