package EarthInvaders.Core;

import EarthInvaders.Interfaces.EntityA;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static EarthInvaders.Core.Game.*;

public class Player extends GameEntity implements EntityA {

    private double velX;
    private double velY;

    private static final int width = (int) (64 * percentageWidth);
    private static final int height = (int) (64 * percentageHeight);

    private int lives = 4;

    private final Image image;
    private Game g;

    private boolean canShoot = true;

    private ArrayList<PowerUp.POWER_UP_TYPE> currentPowerUps = new ArrayList<>();

    private Textures t;
    private Controller controller;

    public Player(Textures t, Controller controller, double x, double y, Game g, Image image) {
        super(x,y);
        this.g = g;
        this.image = image;

        this.t = t;
        this.controller = controller;
    }

    public void tick() {
        x += velX;
        y += velY;

        if (x <= 0)
            setX(0);
        if (x >= Game.WIDTH - width)
            setX(Game.WIDTH - width);
        if (y >= Game.HEIGHT - height)
            setY(Game.HEIGHT - height);
        if (y <= (float) Game.HEIGHT - (float) Game.HEIGHT / 4 - height)
            setY((float) Game.HEIGHT - (float) Game.HEIGHT / 4 - height);
    }

    public void render(Graphics g) {
        if (this.g.ea.contains(this)) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(image, (int) x, (int) y, null);
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVelX(double x) {
        this.velX = x;
    }

    public void setVelY(double y) {
        this.velY = y;
    }

    @Override
    public void removePowerUp(PowerUp.POWER_UP_TYPE powerUp)
    {
        this.currentPowerUps.remove(powerUp);
    }

    @Override
    public void addPowerUp(PowerUp.POWER_UP_TYPE powerUp) {

        if (powerUp == PowerUp.POWER_UP_TYPE.EXTRA_LIFE)
        {
            setLives(getLives() + 1);
        }
        else
        {
            this.currentPowerUps.add(powerUp);

            Timer removePowerUp = new Timer();
            removePowerUp.schedule(new TimerTask() {
                @Override
                public void run() {
                    removePowerUp(powerUp);
                }
            }, PowerUp.TIME_UNTIL_EFFECT_ENDS);
        }
    }

    public void shoot() {
        if (canShoot) {
            canShoot = false;

            if (getTexture() == t.player1) {
                controller.addEntity(new PlayerBullet(getX() + (getBounds().width / 4), getY(), t, g, controller, "p1"));
            }
            else {
                controller.addEntity(new PlayerBullet(getX() + (getBounds().width / 4), getY(), t, g, controller, "p2"));
            }

            playSound("attack_sound_1.wav");

            final Timer cooldown = new Timer();

            if (getPowerUps().contains(PowerUp.POWER_UP_TYPE.SHORTER_ATTACK_COOLDOWN)) {
                cooldown.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        canShoot = true;
                    }
                }, PLAYER_BULLET_COOLDOWN / 5);
            }
            else {
                cooldown.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        canShoot = true;
                    }
                }, PLAYER_BULLET_COOLDOWN);
            }
        }
    }

    @Override
    public ArrayList<PowerUp.POWER_UP_TYPE> getPowerUps() {
        return this.currentPowerUps;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public void setLives(int lives) {
        this.lives = lives;
    }


    @Override
    public Image getTexture() {
        return image;
    }
}
