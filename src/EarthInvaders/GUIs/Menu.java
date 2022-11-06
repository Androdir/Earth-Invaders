package EarthInvaders.GUIs;

import EarthInvaders.Core.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static EarthInvaders.Core.Game.*;

public class Menu implements MouseListener, MouseMotionListener {

    private static Point mouse = new Point(0, 0);

    private final Rectangle toggleSoundFX = new Rectangle(30, Game.HEIGHT - (int) (percentageHeight * 130), (int) (percentageWidth * 270), (int) (percentageHeight * 130));
    private final Rectangle toggleMusic = new Rectangle(30, Game.HEIGHT - (int) (percentageHeight * 250), (int) (percentageWidth * 300), (int) (percentageHeight * 100));

    private final Rectangle playButton = new Rectangle(Game.WIDTH / 2 - (int) (percentageWidth * 200), (int) (Game.HEIGHT / 4 + (percentageHeight * 200)), (int) (percentageWidth * 350), (int) (percentageHeight * 80));
    private final Rectangle selectModesButton = new Rectangle(Game.WIDTH / 2 - (int) (percentageWidth * 200), (int) (Game.HEIGHT / 4 + (percentageHeight * 300)), (int) (percentageWidth * 350), (int) (percentageHeight * 80));
    private final Rectangle helpButton = new Rectangle(Game.WIDTH / 2 - (int) (percentageWidth * 200), (int) (Game.HEIGHT / 4 + (percentageHeight * 400)), (int) (percentageWidth * 350), (int) (percentageHeight * 80));
    private final Rectangle quitButton = new Rectangle(Game.WIDTH / 2 - (int) (percentageWidth * 200), (int) (Game.HEIGHT / 4 + (percentageHeight * 500)), (int) (percentageWidth * 350), (int) (percentageHeight * 80));

    private final Game game;

    public void render(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g.setFont(Game.gameFont.deriveFont(Font.BOLD, Game.WIDTH / 12f));
        g.setColor(Color.WHITE);
        game.drawCenteredString(Game.TITLE, Game.WIDTH, (int) (100 * percentageHeight), g);

        if (playButton.contains(mouse)) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(playButton);

        if (selectModesButton.contains(mouse)) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(selectModesButton);

        if (helpButton.contains(mouse)) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(helpButton);

        if (quitButton.contains(mouse)) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(quitButton);

        if (toggleMusic.contains(mouse)) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(toggleMusic);

        toggleSoundFX.setSize(toggleMusic.getSize());

        if (toggleSoundFX.contains(mouse)) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(toggleSoundFX);

        g.setFont(Game.gameFont.deriveFont(Font.BOLD, percentageWidth * 30));
        g.setColor(Color.BLACK);

        game.drawStringInRectangle("Play", playButton, false, g);
        game.drawStringInRectangle("Select Modes", selectModesButton, false, g);
        game.drawStringInRectangle("Help", helpButton, false, g);
        game.drawStringInRectangle("Quit", quitButton, false, g);
        game.drawStringInRectangle("Toggle SFX", toggleSoundFX, false, g);
        game.drawStringInRectangle("Toggle Music", toggleMusic, false, g);
    }

    public Menu(Game game) {
        this.game = game;
        game.deathMessageWait = false;
    }


    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

        int mx = e.getX();
        int my = e.getY();

        Point mouseClickCoords = new Point(mx, my);

        if (Game.part == Game.PART.MENU) {

            // play button
            if (playButton.contains(mouseClickCoords)) {
                Game.playSound("click.wav");
                Game.part = Game.PART.RDYP1;
                game.startGame();
                Game.startTime = System.currentTimeMillis();
            }

            // mode menu button
            if (selectModesButton.contains(mouseClickCoords)) {
                Game.playSound("click.wav");
                Game.part = Game.PART.MODES;
            }

            // help button
            if (helpButton.contains(mouseClickCoords)) {
                Game.playSound("click.wav");
                Game.part = Game.PART.HELP;
            }

            // toggle music button
            if (toggleMusic.contains(mouseClickCoords))
            {
                Game.playSound("click.wav");
                Game.PLAY_BG_MUSIC = !Game.PLAY_BG_MUSIC;
                if (Game.PLAY_BG_MUSIC) {
                    Game.BGM_2.setFramePosition(0);
                    Game.BGM_2.start();
                }
                else {
                    if (Game.BGM_2.isActive()) {
                        Game.BGM_2.stop();
                    }
                    else {
                        Game.BGM_1.stop();
                    }
                }
            }

            // toggle effects button
            if (toggleSoundFX.contains(mouseClickCoords))
            {
                Game.playSound("click.wav");
                Game.PLAY_SFX = !Game.PLAY_SFX;
            }

            // quit button
            if (quitButton.contains(mouseClickCoords)) {
                Game.playSound("click.wav");
                System.exit(0);
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
        if (part == PART.MENU) {
            mouse.setLocation(e.getX(), e.getY());
        }
    }
}
