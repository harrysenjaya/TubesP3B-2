package com.example.tugasbesar2;

import java.util.ArrayList;

public class BulletMoveThread implements Runnable{

    protected Thread thread;
    protected UIThreadedWrapper uiThreadedWrapper;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private boolean isPaused;

    public BulletMoveThread(UIThreadedWrapper uiThreadedWrapper, ArrayList<Bullet> bullets, ArrayList<Enemy> enemies){
        this.uiThreadedWrapper = uiThreadedWrapper;
        this.thread = new Thread(this);
        this.bullets = bullets;
        this.enemies = enemies;
    }

    public void start(){
        this.thread.start();
    }


    @Override
    public void run() {
        while (true) {
            while (!isPaused) {
                for (int i = 0; i < this.bullets.size(); i++) {
                    for (int j = 0; j < this.enemies.size(); j++) {
                        if (Math.abs(this.bullets.get(i).getX() - this.enemies.get(j).GetX()) < 75 && Math.abs(this.bullets.get(i).getY() - this.enemies.get(j).GetY() + 300) < 75) {
                            this.enemies.remove(j);
                            this.bullets.remove(i);
                            this.uiThreadedWrapper.setEnemies(this.enemies);
                            this.uiThreadedWrapper.setBullets(this.bullets);
                        }
                    }
                    this.bullets.get(i).setY(this.bullets.get(i).getY() - 10);
                }
                this.uiThreadedWrapper.setBullets(this.bullets);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public void setEnemies(ArrayList<Enemy> enemies){
        this.enemies = enemies;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
