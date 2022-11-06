package EarthInvaders.Core;

import java.awt.*;

public class GameEntity {

    public double x, y;

    public GameEntity(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle getBounds(int width, int height) {
        return new Rectangle((int) x, (int) y, width, height);
    }

}
