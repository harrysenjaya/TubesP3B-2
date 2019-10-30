package com.example.tugasbesar2;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BulletThread implements Runnable{
    protected Thread thread;
    protected UIThreadedWrapper uiThreadedWrapper;
    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private boolean isPaused;

    public BulletThread(UIThreadedWrapper uiThreadedWrapper, ArrayList<Bullet> bullets, Player player, ArrayList<Enemy> enemies){
        this.uiThreadedWrapper = uiThreadedWrapper;
        this.thread = new Thread(this);
        this.bullets = bullets;
        this.player = player;
        this.enemies = enemies;
    }

    public void start(){
        this.thread.start();
    }


    @Override
    public void run() {
        while (true) {
            while (!isPaused) {
                Bullet bullet = new Bullet(this.player.getX(), this.player.getY() - 100);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < this.bullets.size(); i++) {
                    for (int j = 0; j < this.enemies.size(); j++) {
                        if (Math.abs(this.bullets.get(i).getX() - this.enemies.get(j).GetX()) < 75 && Math.abs(this.bullets.get(i).getY() - this.enemies.get(j).GetY()) < 75) {
                            this.enemies.remove(j);
                            this.uiThreadedWrapper.setEnemies(this.enemies);
                        }
                    }
                    this.bullets.get(i).setY(this.bullets.get(i).getY() - 10);
                }
                this.bullets.add(bullet);
                this.uiThreadedWrapper.setBullets(this.bullets);
            }
        }
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public void setEnemies(ArrayList<Enemy> enemies){
        this.enemies = enemies;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
