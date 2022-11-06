package EarthInvaders.Core;

import EarthInvaders.Interfaces.EntityA;
import EarthInvaders.Interfaces.EntityE;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class PowerUp extends GameEntity implements EntityE {

    /*
    * POWER UP LIST
    * -------------
    * Power up 1: shorter bullet cooldown for a few seconds
    * Power up 2: immune to ALL damage for a few seconds
    * Power up 3: +1 health
    * Power up 4: increased speed (+25%? +50%?)
    * */

    private static final int AMOUNT_OF_POWER_UPS = POWER_UP_TYPE.values().length;
    private static int POWER_UP_CHOSEN;
    private static final long TIME_UNTIL_DELETE = 15000; // in ms
    public static final long TIME_UNTIL_EFFECT_ENDS = 5000; // in ms
    final Timer deleteTimer = new Timer();
    private BufferedImage texture;

    public enum POWER_UP_TYPE
    {
        SHORTER_ATTACK_COOLDOWN,
        IMMUNITY,
        EXTRA_LIFE,
        INCREASED_SPEED
    }

    private static POWER_UP_TYPE THIS_POWER_UP;
    private Controller controller;

    public PowerUp(double x, double y, Controller controller, Textures textures) {
        super(x, y);
        this.controller = controller;

        POWER_UP_CHOSEN = ThreadLocalRandom.current().nextInt(1, AMOUNT_OF_POWER_UPS + 1);

        if (POWER_UP_CHOSEN == 1)
        {
            THIS_POWER_UP = POWER_UP_TYPE.SHORTER_ATTACK_COOLDOWN;
            setTexture(textures.powerUpCooldown);
        }
        else if (POWER_UP_CHOSEN == 2)
        {
            THIS_POWER_UP = POWER_UP_TYPE.IMMUNITY;
            setTexture(textures.powerUpImmunity);
        }
        else if (POWER_UP_CHOSEN == 3)
        {
            THIS_POWER_UP = POWER_UP_TYPE.EXTRA_LIFE;
            setTexture(textures.powerUpLife);
        }
        else
        {
            THIS_POWER_UP = POWER_UP_TYPE.INCREASED_SPEED;
            setTexture(textures.powerUpSpeed);
        }

        deleteTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                controller.removeEntity(PowerUp.this);
                deleteTimer.cancel();
            }
        }, TIME_UNTIL_DELETE);
    }

    @Override
    public void tick() {
        if (Physics.collisionEA(this, controller.ea)) {
            controller.removeEntity(this);

            EntityA entA = Physics.entityA;

            if (controller.ea.contains(entA)) {
                Game.playSound("powerUpSound.wav");

                entA.addPowerUp(THIS_POWER_UP);
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(getTexture(), ((int) getX()), ((int) getY()), null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(((int) getX()), ((int) getY()), 64, 64);
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
    public BufferedImage getTexture() {
        return this.texture;
    }

    @Override
    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }
}
