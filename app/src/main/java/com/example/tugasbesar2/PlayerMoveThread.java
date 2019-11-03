package com.example.tugasbesar2;

import android.util.Log;

import java.util.Random;

public class PlayerMoveThread implements Runnable {
    protected Thread thread;
    protected UIThreadedWrapper uiThreadedWrapper;
    protected int width;
    protected Player player;
    protected int down = 5;
    protected int up = 0;
    protected boolean kanan;
    private boolean isPaused = true;
    private boolean gameOver;

    public PlayerMoveThread(UIThreadedWrapper uiThreadedWrapper, int width, Player player){
        this.uiThreadedWrapper = uiThreadedWrapper;
        this.thread = new Thread(this);
        this.player = player;
        this.width = width;
    }

    public void start(){
        this.thread.start();
    }

    @Override
    public void run() {
        while(this.player.getX() > 0 && this.player.getX() < this.width && !gameOver) {
            if(!this.isPaused){
                if (this.kanan) {
                   if (this.player.getX() + ((this.width / 8)-70) < this.width) {
                       this.player.setX(this.player.getX() + down);
                       this.player.setY(this.player.getY());
                   }
               } else {
                   if (this.player.getX() - ((this.width / 8)-70) > 0) {
                       this.player.setX(this.player.getX() - down);
                       this.player.setY(this.player.getY());
                   }
               }
               this.uiThreadedWrapper.setPlayer(this.player);
               try {
                   Thread.sleep(25);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
            else{
                if (this.kanan) {
                    if (this.player.getX() + ((this.width / 8)-70) < this.width) {
                        this.player.setX(this.player.getX() + up);
                        this.player.setY(this.player.getY());
                    }
                } else {
                    if (this.player.getX() - ((this.width / 8)-70) > 0) {
                        this.player.setX(this.player.getX() - up);
                        this.player.setY(this.player.getY());
                    }
                }
                this.uiThreadedWrapper.setPlayer(this.player);
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
       }
    }

    public void setPaused(boolean isPaused){
        this.isPaused = isPaused;
    }

    public void setKanan(boolean kanan) {
        this.kanan = kanan;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
