package EarthInvaders.Interfaces;

import java.awt.*;

public interface EntityC {

    void tick();
    void render(Graphics g);
    Rectangle getBounds();

    double getX();
    double getY();

    int getLives();
    void setLives(int lives);

}
