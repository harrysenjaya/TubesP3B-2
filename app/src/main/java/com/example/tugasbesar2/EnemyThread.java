package com.example.tugasbesar2;

import java.util.Random;

public class EnemyThread implements Runnable {
    protected Thread thread;
    protected UIThreadedWrapper uiThreadedWrapper;
    protected Random random;
    protected int width;
    protected int height;

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
        while(true) {
            int x = random.nextInt(width)+1;
            int y = 200;
            Enemy enemy = new Enemy(x,y);
            this.uiThreadedWrapper.setEnemy(enemy);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
