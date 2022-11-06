package EarthInvaders.Enemies;

import EarthInvaders.Core.Controller;
import EarthInvaders.Core.Game;
import EarthInvaders.Core.GameEntity;
import EarthInvaders.Core.Textures;
import EarthInvaders.Interfaces.EntityC;

import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static EarthInvaders.Core.Game.percentageHeight;
import static EarthInvaders.Core.Game.percentageWidth;

public class Boss extends GameEntity implements EntityC {

    private static final int width = (int) (32 * percentageWidth);
    private static final int height = (int) (32 * percentageHeight);

    final private Textures textures;
    final private float speed;
    private final Random random = new Random();

    int timesShot = 0;
    int lives = 2;

    public Boss(double x, double y, Textures textures, Game game, Controller controller)
    {
        super(x,y);
        this.textures = textures;

        Game.playSound("bossAlert.wav");

        if (Game.withFriend)
            this.speed = 5f;
        else
            this.speed = 4.45f;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (game.ec.contains(Boss.this) && !game.paused)
                {
                    Timer tripleBullet = new Timer();
                    tripleBullet.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (timesShot == 0)
                            {
                                Game.playSound("bossShoot.wav");
                            }
                            if (!(timesShot >= 2))
                            {
                                controller.addEntity(new BossBullet(getX(), getY(), game, controller, textures));
                                timesShot++;
                            }
                            else
                            {
                                tripleBullet.cancel();
                                timesShot = 0;
                            }
                        }
                    }, 0, 150);
                }
                else if (!game.ec.contains(Boss.this))
                {
                    timer.cancel();
                }
            }
        }, 0, Game.BOSS_BULLET_COOLDOWN);
    }


    @Override
    public void tick() {
        try {
            y += speed;

            if (y >= Game.HEIGHT) {
                y = 0;
                x = random.nextInt(Game.WIDTH - width);
            }

        } catch (Exception e) {
            System.out.println("Boss tick failure");
        }
    }

    @Override
    public void render(Graphics g) {
        try {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(textures.boss, (int) x, (int) y, null);
        } catch (Exception e) {
            System.out.println("Enemy render Failure");
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(((int) x), ((int) y), width, height);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
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
