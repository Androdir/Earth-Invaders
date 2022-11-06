package EarthInvaders.Interfaces;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface EntityE {

    void tick();
    void render(Graphics g);
    Rectangle getBounds();

    double getX();
    double getY();

    BufferedImage getTexture();
    void setTexture(BufferedImage texture);
}
