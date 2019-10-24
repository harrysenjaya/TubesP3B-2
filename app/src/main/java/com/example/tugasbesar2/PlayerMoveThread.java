package com.example.tugasbesar2;

import android.util.Log;

import java.util.Random;

public class PlayerMoveThread implements Runnable {
    protected Thread thread;
    protected UIThreadedWrapper uiThreadedWrapper;
    protected int width;
    protected Player player;
    protected boolean kanan;
    protected Pauser pauser;
    private boolean isPaused;

    public PlayerMoveThread(UIThreadedWrapper uiThreadedWrapper, int width, Player player, Pauser pauser){
        this.uiThreadedWrapper = uiThreadedWrapper;
        this.thread = new Thread(this);
        this.player = player;
        this.width = width;
        this.pauser = pauser;
    }

    public void start(){
        this.thread.start();
    }

    @Override
    public void run() {
        Log.d("KANAN",kanan+"");
       while(this.player.getX() > 0 && this.player.getX() < this.width && isPaused == false) {
           try {
               this.pauser.look();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           if (this.kanan) {
                this.player.setX(this.player.getX() + 5);
                this.player.setY(this.player.getY());
           }
           else{
               this.player.setX(this.player.getX() - 5);
               this.player.setY(this.player.getY());
           }
           this.uiThreadedWrapper.setPlayer(this.player);
           try {
               Thread.sleep(200);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
    }

    public void setIsPaused(boolean isPaused){
        this.isPaused = isPaused;
    }

    public void setKanan(boolean kanan) {
        this.kanan = kanan;
    }
}
