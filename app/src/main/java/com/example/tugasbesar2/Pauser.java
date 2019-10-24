package com.example.tugasbesar2;

public class Pauser{

    private boolean isPaused=false;

    public synchronized void pause(){
        isPaused=true;
    }

    public synchronized void resume(){
        isPaused=false;
        notifyAll();
    }

    public synchronized void look() throws InterruptedException {
        while(isPaused){
            wait();
        }
    }

}