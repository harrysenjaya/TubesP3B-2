package com.example.tugasbesar2;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BulletThread implements Runnable{
    protected Thread thread;
    protected UIThreadedWrapper uiThreadedWrapper;
    private Player player;
    private boolean isPaused;

    public BulletThread(UIThreadedWrapper uiThreadedWrapper,Player player){
        this.uiThreadedWrapper = uiThreadedWrapper;
        this.thread = new Thread(this);
        this.player = player;
    }

    public void start(){
        this.thread.start();
    }


    @Override
    public void run() {
        while (true) {
            while (!isPaused) {
                Bullet bullet = new Bullet(this.player.getX(), this.player.getY() - 100);
                this.uiThreadedWrapper.setBullet(bullet);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}