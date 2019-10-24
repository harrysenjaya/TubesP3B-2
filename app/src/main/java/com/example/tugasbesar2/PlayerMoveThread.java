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

    public PlayerMoveThread(UIThreadedWrapper uiThreadedWrapper, int width, Player player, boolean kanan, Pauser pauser){
        this.uiThreadedWrapper = uiThreadedWrapper;
        this.thread = new Thread(this);
        this.player = player;
        this.width = width;
        this.kanan = kanan;
        this.pauser = pauser;
    }

    public void start(){
        this.thread.start();
    }

    @Override
    public void run() {
        Log.d("KANAN",kanan+"");
        Player player = this.player;
       while(this.player.getX() > 0 && this.player.getX() < this.width) {
           try {
               this.pauser.look();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           if (this.kanan) {
                player = new Player(this.player.getX() + 5, this.player.getY());
                this.player = player;
           }
           else{
                player = new Player(this.player.getX() - 5, this.player.getY());
                this.player = player;

           }
           this.uiThreadedWrapper.setPlayer(player);
           try {
               Thread.sleep(200);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
    }
}
