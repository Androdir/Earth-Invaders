package EarthInvaders.Interfaces;

import java.awt.*;

public interface EntityB {

    public void tick();
    public void render(Graphics g);
    public Rectangle getBounds();

    public double getX();
    public double getY();
}
