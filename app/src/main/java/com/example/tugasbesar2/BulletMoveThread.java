package com.example.tugasbesar2;

import android.util.Log;

import java.util.ArrayList;

public class BulletMoveThread implements Runnable {

    protected Thread thread;
    protected UIThreadedWrapper uiThreadedWrapper;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private boolean isPaused;
    private boolean gameOver;

    public BulletMoveThread(UIThreadedWrapper uiThreadedWrapper, ArrayList<Bullet> bullets, ArrayList<Enemy> enemies) {
        this.uiThreadedWrapper = uiThreadedWrapper;
        this.thread = new Thread(this);
        this.bullets = bullets;
        this.enemies = enemies;
    }

    public void start() {
        this.thread.start();
    }


    @Override
    public void run() {
        while (!gameOver) {
            while (!isPaused) {
                for (int i = 0; i < this.bullets.size(); i++) {
                    if (this.bullets.get(i).getY() < -1000) {
                        this.bullets.remove(i);
                        continue;
                    }
                    for (int j = 0; j < this.enemies.size(); j++) {
                        if (Math.abs(this.bullets.get(i).getX() - this.enemies.get(j).GetX()) < 75 && Math.abs(this.bullets.get(i).getY() - this.enemies.get(j).GetY() + 300) < 75) {
                            if (this.enemies.get(j).getHealth() <= 0) {
                                this.enemies.remove(j);
                                this.uiThreadedWrapper.kill();
                            } else {
                                this.enemies.get(j).setHealth(this.enemies.get(j).getHealth() - 1);
                            }
                            this.bullets.remove(i);
                            this.uiThreadedWrapper.setEnemies(this.enemies);
                            this.uiThreadedWrapper.setBullets(this.bullets);

                        }
                    }
                    if (i < this.bullets.size()) {
                        this.bullets.get(i).setY(this.bullets.get(i).getY() - 10);
                    }
                }
                this.uiThreadedWrapper.setBullets(this.bullets);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void setBullets (ArrayList < Bullet > bullets) {
        this.bullets = bullets;
    }

    public void setEnemies (ArrayList < Enemy > enemies) {
        this.enemies = enemies;
    }

    public void setPaused ( boolean paused){
        isPaused = paused;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}

