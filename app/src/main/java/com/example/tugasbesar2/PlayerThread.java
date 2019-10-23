//package com.example.tugasbesar2;
//
//import java.util.Random;
//
//public class PlayerThread implements Runnable {
//    protected Thread thread;
//    protected UIThreadedWrapper uiThreadedWrapper;
//    protected Random random;
//
//    public PlayerThread(UIThreadedWrapper uiThreadedWrapper){
//        this.uiThreadedWrapper = uiThreadedWrapper;
//        this.thread = new Thread(this);
//        this.random = new Random();
//    }
//
//    public void start(){
//        this.thread.start();
//    }
//
//    @Override
//    public void run() {
//        while(true) {
//            int x =
//                    this.uiThreadedWrapper.setTextViewOut(circle);
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
