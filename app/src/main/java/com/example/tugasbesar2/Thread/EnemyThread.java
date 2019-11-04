package com.example.tugasbesar2.Thread;

import com.example.tugasbesar2.Model.Enemy;
import com.example.tugasbesar2.Presenter.UIThreadedWrapper;

import java.util.Random;

public class EnemyThread implements Runnable {
    protected Thread thread;
    protected UIThreadedWrapper uiThreadedWrapper;
    protected Random random;
    protected int width;
    protected int height;
    protected int health=0;
    private boolean isPaused;
    private boolean gameOver;

    public EnemyThread(UIThreadedWrapper uiThreadedWrapper, int width, int height){
        this.uiThreadedWrapper = uiThreadedWrapper;
        this.thread = new Thread(this);
        this.random = new Random();
        this.width = width;
        this.height = height;
    }

    public void start(){
        this.thread.start();
    }

    @Override
    public void run() {
        while (!gameOver) {
            while (!this.isPaused) {
                int x = random.nextInt(width-75) + 1;
                int y = 100;
                Enemy enemy = new Enemy(x, y,health);
                this.uiThreadedWrapper.setEnemy(enemy);
                try {
                    Thread.sleep(5000);
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
