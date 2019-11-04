package com.example.tugasbesar2.Thread;


import com.example.tugasbesar2.Model.Enemy;
import com.example.tugasbesar2.Presenter.UIThreadedWrapper;

import java.util.ArrayList;

public class EnemyMoveThread implements Runnable{
    protected Thread thread;
    protected UIThreadedWrapper uiThreadedWrapper;
    protected int height;
    protected ArrayList<Enemy> enemies;
    private boolean isPaused;
    private boolean gameOver;

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
        while(!gameOver) {
            while (!this.isPaused) {
                for (int i = 0; i < this.enemies.size(); i++) {
                    if(enemies.get(i).GetY()+150>this.height){
                        this.uiThreadedWrapper.gameOver();
                    }
                    enemies.get(i).SetY(enemies.get(i).GetY() + 40);
                }
                this.uiThreadedWrapper.setEnemies(enemies);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
