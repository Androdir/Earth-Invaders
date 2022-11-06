package EarthInvaders.GUIs;

import EarthInvaders.Core.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import static EarthInvaders.Core.Game.*;

public class Help implements MouseListener, MouseMotionListener {

    private static Point mouse = new Point(0, 0);

    private final Rectangle backButton = new Rectangle((int) (25 * percentageWidth), (int) (25 * percentageHeight), (int) (100 * percentageWidth), (int) (50 * percentageHeight));
    private final Rectangle lastPage = new Rectangle((int) (Game.WIDTH / 2 - (percentageWidth * 200)), (int) (Game.HEIGHT - percentageHeight * 150), (int) (percentageWidth * 100), (int) (percentageHeight * 100));
    private final Rectangle pageNum = new Rectangle((int) (Game.WIDTH / 2 - (percentageWidth * 50)), (int) (Game.HEIGHT - percentageHeight * 150), (int) (percentageWidth * 200), (int) (percentageHeight * 100));
    private final Rectangle nextPage = new Rectangle((int) (Game.WIDTH / 2 + (percentageWidth * 200)), Game.HEIGHT - (int) (percentageHeight * 150), (int) (percentageWidth * 100), (int) (percentageHeight * 100));


    private static int pageNumber = 0; // index 0 to work with array "pages"
    private final Game game;

    private ArrayList<String> pages = new ArrayList<>();

    final private String page1 = "" +
                "\n" +
                "Game Modes:\n" +
                "\n" +
                "Your objective is to kill the aliens that are invading Earth. There are 3 game modes that you can choose from: 'Levels', 'Infinite' or 'Timed'.\n" +
                "\n" +
                "- 'Levels' game mode:\n" +
                "    the game starts with 6 aliens. When you kill all the aliens, 2 extra aliens will spawn. You win when you beat level 10.\n" +
                "\n" +
                "- 'Infinite' game mode:\n" +
                "    the game starts with 6 aliens, and 2 more aliens spawn every few seconds.\n" +
                "\n" +
                "- 'Timed' game mode:\n" +
                "    similar to the game mode 'levels', but it ends when you reach 300 score.\n" +
                "";


    final private String page2 = "" +
            "\n" +
            "Game Mechanics (1/2):\n" +
            "\n" +
            "\n" +
            "You start the game with full shield. Each time you get hit the shield will get damaged. The shield breaks once you get hit 3 times. If you get hit when your shield is broken, you lose.\n" +
            "\n" +
            "\n" +
            "In the game modes 'Levels' and 'Timed', bosses spawn every 5 levels, and in the game mode 'Infinite' there is a chance that a boss spawns instead of a normal alien.\n" +
            "The boss has a better spaceship than the other aliens.\n" +
            "";

    final private String page3 = "" +
            "\n" +
            "Game Mechanics (2/2):\n" +
            "\n" +
            "Sometimes a power up spawns. There are 4 types of power ups:\n" +
            "\n" +
            "- Life:\n" +
            "    gives you an extra life\n" +
            "- Speed:\n" +
            "    makes you move faster temporarily\n" +
            "- Immunity:\n" +
            "    makes you immune to all damage temporarily\n" +
            "- Faster attack speed:\n" +
            "    makes you shoot faster temprarily\n" +
            "";

    final private String page4 = "" +
                "\n" +
                "Controls:" +
                "\n" +
                "                         P1           P2\n" +
                "- Move up:        W            up arrow\n" +
                "- Move down:    S            down arrow\n" +
                "- Move left:      A            left arrow\n" +
                "- Move right:    D            up arrow\n" +
                "- Shoot:           space     numpad 0\n" +
                "\n" +
                "- Pause: P\n" +
                "- Close the game: F12\n" +
                "";

    public Help(Game game)
    {
        this.game = game;
        pageNumber = 0;

        pages.add(page1);
        pages.add(page2);
        pages.add(page3);
        pages.add(page4);

        game.helpTextArea.setText(pages.get(pageNumber));
    }


    public void render(Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;

        if (backButton.contains(mouse)) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(backButton);

        g2d.setColor(Color.WHITE);
        g2d.fill(pageNum);

        if (lastPage.contains(mouse)) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }
        g2d.fill(lastPage);
        if (nextPage.contains(mouse)) {
            g2d.setColor(new Color(116, 116, 116));
        }
        else {
            g2d.setColor(Color.WHITE);
        }
        g2d.fill(nextPage);

        g.setFont(Game.gameFont.deriveFont(Font.BOLD, Game.WIDTH / 11f));
        g.setColor(Color.WHITE);
        game.drawCenteredString("HELP", Game.WIDTH, (int) (percentageHeight * 100), g);
        g.setFont(Game.gameFont.deriveFont(Font.BOLD, percentageWidth * 30));
        g.setColor(Color.BLACK);
        g.drawString("BACK", (int) (percentageWidth * 28), (int) (percentageHeight * 65));

        game.drawStringInRectangle("<", lastPage, false, g);
        game.drawStringInRectangle("Page " + (pageNumber + 1) + "/" + pages.size(), pageNum, false, g);
        game.drawStringInRectangle(">", nextPage, false, g);

        if (!game.helpTextArea.isShowing())
            game.helpTextArea.setVisible(true);
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        final int mx = e.getX();
        final int my = e.getY();

        final Point mouseCoords = new Point(mx, my);

        if (Game.part == Game.PART.HELP) {
            if (backButton.contains(mouseCoords)) {
                Game.playSound("click.wav");
                Game.part = Game.PART.MENU;
            }

            if (nextPage.contains(mouseCoords))
            {
                Game.playSound("click.wav");
                if (pageNumber < pages.size() - 1)
                {
                    pageNumber++;
                    game.helpTextArea.setText(pages.get(pageNumber));
                }
            }

            if (lastPage.contains(mouseCoords))
            {
                Game.playSound("click.wav");
                if (pageNumber > 0)
                {
                    pageNumber--;
                    game.helpTextArea.setText(pages.get(pageNumber));
                }
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
        if (Game.part == PART.HELP) {
            mouse.setLocation(e.getX(), e.getY());
        }
    }
}
