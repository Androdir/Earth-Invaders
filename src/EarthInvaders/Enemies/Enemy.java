package EarthInvaders.Enemies;

import EarthInvaders.Core.*;
import EarthInvaders.Interfaces.EntityC;

import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import static EarthInvaders.Core.Game.percentageHeight;
import static EarthInvaders.Core.Game.percentageWidth;


public class Enemy extends GameEntity implements EntityC {

    private static final int width = (int) (32 * percentageWidth);
    private static final int height = (int) (32 * percentageHeight);

    final private Textures t;
    final private Controller c;
    final private Game g;
    private float max, min, speed;

    private int lives = 1;

    final private Random r = new Random();

    public Enemy(double x, double y, long bulletCooldown, Textures t, Game game, Controller control) {
        super(x,y);
        this.g = game;
        this.c = control;
        this.t = t;

        if (Game.withFriend) {
            max = 3.5f * percentageWidth;
            min = 3.1f * percentageWidth;
        }
        else {
            max = 3.5f * percentageWidth;
            min = 2.7f * percentageWidth;
        }
        if (Game.withFriend)
            speed = (r.nextFloat() * (max - min) + min) + (g.getLevel() * 0.25f * percentageWidth) + 1;
        else
            speed = (r.nextFloat() * (max - min) + min) + (g.getLevel() * 0.25f * percentageWidth);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (game.ec.contains(Enemy.this) && !game.paused) {
                    final int shootChance = ThreadLocalRandom.current().nextInt(1, 10 + 1);
                    // 90% chance to shoot
                    if (shootChance != 1)
                    {
                        c.addEntity(new EnemyBullet(getX(), getY(), t, g, c));
                    }
                }
                else
                    timer.cancel();
            }
        }, 0, bulletCooldown);
    }



    public void tick() {
        try {
            y += speed;

            if (y >= Game.HEIGHT) {
                y = 0;
                x = r.nextInt(Game.WIDTH - width);

                if (Physics.collisionCC(this, g.ec))
                {
                    x = r.nextInt(Game.WIDTH - width);
                }
            }

        } catch (Exception e) {
            System.out.println("Enemy tick failure");
        }
    }

    public void render(Graphics g) {
        try {
            g.drawImage(t.enemy, (int) x, (int) y, null);
        } catch (Exception e) {
            System.out.println("Enemy render Failure");
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public void setLives(int lives) {
        this.lives = lives;
    }
}
