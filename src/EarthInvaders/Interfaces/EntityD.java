package EarthInvaders.Interfaces;

import java.awt.*;

public interface EntityD {

    public void tick();
    public void render(Graphics g);
    public Rectangle getBounds();

    public double getX();
    public double getY();

}
