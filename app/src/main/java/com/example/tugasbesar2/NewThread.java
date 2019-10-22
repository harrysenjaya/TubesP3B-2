package com.example.tugasbesar2;

import java.util.Random;

public class NewThread implements Runnable {
    protected Thread thread;
    protected UIThreadedWrapper uiThreadedWrapper;
    protected Random random;
    protected Timer t ;

    public TesThread(UIThreadedWrapper uiThreadedWrapper){
        this.uiThreadedWrapper = uiThreadedWrapper;
        this.thread = new Thread(this);
        this.random = new Random();
    }

    public void start(){
        this.thread.start();
    }

    @Override
    public void run() {
        while(true) {
            int x = random.nextInt(100 + 10) - 100;
            int y = random.nextInt(100 + 10) - 100;
            Circle circle = new Circle(x, y);
            this.uiThreadedWrapper.setTextViewOut(circle);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
