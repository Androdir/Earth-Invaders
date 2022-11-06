package EarthInvaders.Interfaces;

import EarthInvaders.Core.PowerUp;

import java.awt.*;
import java.util.ArrayList;

public interface EntityA {

    public void tick();
    public void render(Graphics g);
    public Rectangle getBounds();

    public double getX();
    public double getY();

    void setVelX(double v);
    void setVelY(double y);

    void addPowerUp(PowerUp.POWER_UP_TYPE powerUp);
    void removePowerUp(PowerUp.POWER_UP_TYPE powerUp);
    ArrayList<PowerUp.POWER_UP_TYPE> getPowerUps();

    int getLives();
    void setLives(int lives);

    Image getTexture();
}
