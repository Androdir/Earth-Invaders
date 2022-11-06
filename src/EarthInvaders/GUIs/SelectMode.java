package EarthInvaders.GUIs;

import EarthInvaders.Core.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static EarthInvaders.Core.Game.percentageHeight;
import static EarthInvaders.Core.Game.percentageWidth;

public class SelectMode implements MouseListener, MouseMotionListener {

    private static Point mouse = new Point(0, 0);

    private final Rectangle backButton = new Rectangle((int) (25 * percentageWidth), (int) (25 * percentageHeight), (int) (100 * percentageWidth), (int) (50 * percentageHeight));
    private final Rectangle singlePlayer = new Rectangle((int) (Game.WIDTH / 2 - 500 * percentageWidth), (int) (Game.HEIGHT / 2 - 50 * percentageHeight), (int) (300 * percentageWidth), (int) (100 * percentageHeight));
    private final Rectangle againstFriend = new Rectangle((int) (Game.WIDTH / 2 - 100 * percentageWidth), (int) (Game.HEIGHT / 2 - 50 * percentageHeight), (int) (300 * percentageWidth), (int) (100 * percentageHeight));
    private final Rectangle withFriend = new Rectangle((int) (Game.WIDTH / 2 + 300 * percentageWidth), (int) (Game.HEIGHT / 2 - 50 * percentageHeight), (int) (300 * percentageWidth), (int) (100 * percentageHeight));
    private final Rectangle levels = new Rectangle((int) (Game.WIDTH / 2 - 500 * percentageWidth), (int) (Game.HEIGHT / 2 - 50 + 200 * percentageHeight), (int) (300 * percentageWidth), (int) (100 * percentageHeight));
    private final Rectangle infinite = new Rectangle((int) (Game.WIDTH / 2 - 100 * percentageWidth), (int) (Game.HEIGHT / 2 - 50 + 200 * percentageHeight), (int) (300 * percentageWidth), (int) (100 * percentageHeight));
    private final Rectangle timed = new Rectangle((int) (Game.WIDTH / 2 + 300 * percentageWidth), (int) (Game.HEIGHT / 2 - 50 + 200 * percentageHeight), (int) (300 * percentageWidth), (int) (100 * percentageHeight));

    public void render(Graphics g, Game game) {
        Graphics2D g2d = (Graphics2D) g;

        if (backButton.contains(mouse)) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(backButton);

        if (singlePlayer.contains(mouse) || Game.singlePlayer) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(singlePlayer);

        if (againstFriend.contains(mouse) || Game.againstFriend) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(againstFriend);

        if (withFriend.contains(mouse) || Game.withFriend) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(withFriend);

        if (levels.contains(mouse) || Game.gamemode == Game.GAMEMODE.LEVELS) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(levels);

        if (infinite.contains(mouse) || Game.gamemode == Game.GAMEMODE.INFINITE) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(infinite);

        if (timed.contains(mouse) || Game.gamemode == Game.GAMEMODE.TIMED) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(timed);

        g.setFont(Game.gameFont.deriveFont(Font.BOLD, Game.WIDTH / 11f));
        g.setColor(Color.WHITE);
        game.drawCenteredString("SELECT MODE", Game.WIDTH, (int) (100 * percentageWidth), g);
        g.setFont(Game.gameFont.deriveFont(Font.BOLD, 30f  * percentageWidth));
        g.setColor(Color.BLACK);
        g.drawString("BACK", (int) (28 * percentageWidth), (int) (65 * percentageWidth));

        game.drawStringInRectangle("1 Player", singlePlayer, false, g);
        game.drawStringInRectangle("2 Players ", againstFriend, false, g);
        game.drawStringInRectangle("Co-op", withFriend, false, g);
        game.drawStringInRectangle("Levels", levels, false, g);
        game.drawStringInRectangle("Infinite", infinite, false, g);
        game.drawStringInRectangle("Timed", timed, false, g);
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        if (Game.part == Game.PART.MODES) {
            if (backButton.contains(mx,my)) {
                Game.playSound("click.wav");
                Game.part = Game.PART.MENU;
            }
            if (singlePlayer.contains(mx,my)) {
                Game.playSound("click.wav");
                Game.againstFriend = false;
                Game.withFriend = false;
                Game.singlePlayer = true;
            }
            if (againstFriend.contains(mx,my)) {
                Game.playSound("click.wav");
                Game.againstFriend = true;
                Game.withFriend = false;
                Game.singlePlayer = false;
            }
            if (withFriend.contains(mx,my)) {
                Game.playSound("click.wav");
                Game.againstFriend = false;
                Game.withFriend = true;
                Game.singlePlayer = false;
            }
            if (timed.contains(mx,my)) {
                Game.playSound("click.wav");
                Game.gamemode = Game.GAMEMODE.TIMED;
            }
            if (levels.contains(mx,my)) {
                Game.playSound("click.wav");
                Game.gamemode = Game.GAMEMODE.LEVELS;
            }
            if (infinite.contains(mx,my)) {
                Game.playSound("click.wav");
                Game.gamemode = Game.GAMEMODE.INFINITE;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (Game.part == Game.PART.MODES) {
            mouse.setLocation(e.getX(), e.getY());
        }
    }
}
