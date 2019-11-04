package com.example.tugasbesar2.Thread;

import com.example.tugasbesar2.Model.Bullet;
import com.example.tugasbesar2.Model.Player;
import com.example.tugasbesar2.Presenter.UIThreadedWrapper;

public class BulletThread implements Runnable{
    protected Thread thread;
    protected UIThreadedWrapper uiThreadedWrapper;
    private Player player;
    private boolean isPaused;
    private boolean gameOver;

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
        while (!gameOver) {
            while (!isPaused) {
                Bullet bullet = new Bullet(this.player.getX(), this.player.getY() - 100);
                this.uiThreadedWrapper.setBullet(bullet);
                try {
                    Thread.sleep(500);
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

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}

