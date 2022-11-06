package EarthInvaders.Enemies;

import EarthInvaders.Core.*;
import EarthInvaders.Interfaces.EntityD;

import java.awt.*;

import static EarthInvaders.Core.Game.percentageHeight;
import static EarthInvaders.Core.Game.percentageWidth;

public class EnemyBullet extends GameEntity implements EntityD {

    private static final int width = (int) (14 * percentageWidth);
    private static final int height = (int) (18 * percentageHeight);

    private Textures t;
    private Controller c;
    private Game game;

    public EnemyBullet(double x, double y, Textures t, Game game, Controller c) {
        super(x,y);
        this.t = t;
        this.game = game;
        this.c = c;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public void tick() {
        y += 6f * percentageWidth + (0.20 * percentageWidth * game.getLevel());

        if (game.ea.contains(game.p1)) {
            if (Physics.collisionDA(this, game.p1)) {
                c.removeEntity(this);

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
                        c.removeEntity(game.p1);
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

        if (Game.withFriend && game.ea.contains(game.p2)) {
            if (Physics.collisionDA(this, game.p2)) {
                c.removeEntity(this);


                if (!game.p2.getPowerUps().contains(PowerUp.POWER_UP_TYPE.IMMUNITY))
                {
                    if (game.p2.getLives() > 0) {
                        game.p2.setLives(game.p2.getLives() - 1);
                        if (Game.PLAY_SFX)
                        {
                            Game.playSound("playerHurtSound.wav");
                        }
                    }

                    if (game.p2.getLives() <= 0) {
                        c.removeEntity(game.p2);
                        if (Game.PLAY_SFX) {
                            Game.playSound("playerDeath.wav");
                        }
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

        if (y > Game.HEIGHT)
            c.removeEntity(this);
    }

    public void render(Graphics g) {
        g.drawImage(t.enemyBullet, (int) x, (int) y, null);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
