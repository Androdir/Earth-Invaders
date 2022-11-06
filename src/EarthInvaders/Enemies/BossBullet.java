package EarthInvaders.Enemies;

import EarthInvaders.Core.*;
import EarthInvaders.Interfaces.EntityD;

import java.awt.*;

import static EarthInvaders.Core.Game.percentageHeight;
import static EarthInvaders.Core.Game.percentageWidth;

public class BossBullet extends GameEntity implements EntityD {

    private static final int width = (int) (16 * percentageWidth);
    private static final int height = (int) (20 * percentageHeight);

    private Textures textures;
    private Game game;
    private Controller controller;
    private final double velY = 7.2f * percentageWidth;
    private final double velX = 7.2f * percentageWidth;
    private double angle;

    public BossBullet(double x, double y, Game game, Controller controller, Textures textures) {
        super(x, y);

        this.game = game;
        this.controller = controller;
        this.textures = textures;

        if (!Game.withFriend || !game.ea.contains(game.p2)) {
            angle = Math.toDegrees(Math.atan2(game.p1.getX() - getX(), game.p1.getY() - getY()));
            angle = angle + Math.ceil(-angle / 360) * 360;
        }
        else
        {
            double distanceToP1 = Math.sqrt(Math.pow((game.p1.getX() - getX()), 2) + Math.pow(game.p1.getY() - getY(), 2));
            double distanceToP2 = Math.sqrt(Math.pow((game.p2.getX() - getX()), 2) + Math.pow(game.p2.getY() - getY(), 2));

            if (distanceToP1 < distanceToP2 && game.ea.contains(game.p1))
            {
                angle = Math.toDegrees(Math.atan2(game.p1.getX() - getX(), game.p1.getY() - getY()));
                angle = angle + Math.ceil(-angle / 360) * 360;
            }

            if (distanceToP1 > distanceToP2 && game.ea.contains(game.p2))
            {
                angle = Math.toDegrees(Math.atan2(game.p2.getX() - getX(), game.p2.getY() - getY()));
                angle = angle + Math.ceil(-angle / 360) * 360;
            }
        }

    }

    @Override
    public void tick() {
        try {
            y += velY * Math.cos(Math.toRadians(angle));
            x += velX * Math.sin(Math.toRadians(angle));

            if (controller.ea.contains(game.p1)) {
                if (Physics.collisionDA(this, game.p1)) {
                    controller.removeEntity(this);

                    if (!game.p1.getPowerUps().contains(PowerUp.POWER_UP_TYPE.IMMUNITY))
                    {
                        if (game.p1.getLives() > 0) {
                            game.p1.setLives(game.p1.getLives() - 1);
                            if (Game.PLAY_SFX)
                            {
                                Game.playSound("playerHurtSound.wav");
                            }
                        }
                        if (game.p1.getLives() <= 0) {
                            controller.removeEntity(game.p1);
                            if (Game.PLAY_SFX) {
                                Game.playSound("playerDeath.wav");
                            }
                        }
                    }

                    if (game.p1.getLives() <= 0 && !Game.withFriend) {
                        game.endGame();
                        if (Game.gamemode == Game.GAMEMODE.LEVELS)
                            game.saveHighscoreAndStopGame(game.levelsHighScoreSave);
                        if (Game.gamemode == Game.GAMEMODE.INFINITE)
                            game.saveHighscoreAndStopGame(game.infinityHighScoreSave);
                        if (Game.gamemode == Game.GAMEMODE.TIMED)
                            game.endGame();
                    }

                    else if (game.p1.getLives() <= 0 && game.p2.getLives() <= 0) {
                        game.endGame();
                        if (Game.gamemode == Game.GAMEMODE.LEVELS)
                            game.saveHighscoreAndStopGame(game.levelsHighScoreSave);
                        if (Game.gamemode == Game.GAMEMODE.INFINITE)
                            game.saveHighscoreAndStopGame(game.infinityHighScoreSave);
                        if (Game.gamemode == Game.GAMEMODE.TIMED)
                            game.endGame();
                    }
                }
            }

            if (Game.withFriend && controller.ea.contains(game.p2)) {
                if (Physics.collisionDA(this, game.p2)) {
                    this.controller.removeEntity(this);
                    if (!game.p2.getPowerUps().contains(PowerUp.POWER_UP_TYPE.IMMUNITY))
                    {
                        if (game.p2.getLives() > 0) {
                            game.p2.setLives(game.p2.getLives() - 1);
                            if (Game.PLAY_SFX)
                            {
                                Game.playSound("playerHurtSound.wav");
                            }
                        }
                    }

                    if (game.p2.getLives() <= 0) {
                        this.controller.removeEntity(game.p2);
                        if (Game.PLAY_SFX)
                        {
                            Game.playSound("playerDeath.wav");
                        }
                    }

                    if (game.p1.getLives() <= 0 && game.p2.getLives() <= 0) {
                        game.endGame();
                        if (Game.gamemode == Game.GAMEMODE.LEVELS)
                            game.saveHighscoreAndStopGame(game.levelsHighScoreSave);
                        if (Game.gamemode == Game.GAMEMODE.INFINITE)
                            game.saveHighscoreAndStopGame(game.infinityHighScoreSave);
                        if (Game.gamemode == Game.GAMEMODE.TIMED)
                            game.saveHighscoreAndStopGame(game.timedHighScoreSave);
                    }
                }
            }

            if (y > Game.HEIGHT || y < 0 || x > Game.WIDTH || x < 0)
                this.controller.removeEntity(this);
        } catch (Exception e) {
            System.out.println("Boss bullet tick failure");
        }
    }

    @Override
    public void render(Graphics g) {
        try {
            g.drawImage(textures.bossBullet, ((int) x), ((int) y), null);
        } catch (Exception e) {
            System.out.println("Boss bullet render failure");
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }
}
