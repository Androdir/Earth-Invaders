package EarthInvaders.Core;

import EarthInvaders.Interfaces.EntityB;

import java.awt.*;
import java.util.Random;

import static EarthInvaders.Core.Game.percentageHeight;
import static EarthInvaders.Core.Game.percentageWidth;

public class PlayerBullet extends GameEntity implements EntityB {

    private final Textures t;
    private final Game game;
    private final Controller c;
    private final Random r = new Random();
    private final String key;

    public PlayerBullet(double x, double y, Textures t, Game game, Controller c, String key) {
        super(x,y);
        this.t = t;
        this.c = c;
        this.game = game;
        this.key = key;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, (int) (14 * percentageWidth), (int) (19 * percentageHeight));
    }

    public void tick() {
        y -= 6f * percentageWidth;

        if (Physics.collisionBC(this, game.ec)) {
            c.removeEntity(this);

            Physics.entityC.setLives(Physics.entityC.getLives() - 1);
            if (Physics.entityC.getLives() <= 0)
            {
                c.removeEntity(Physics.entityC);
                c.createExplosion(Physics.entityC.getX() - 32 * percentageWidth, Physics.entityC.getY() - 32 * percentageHeight, true);

                game.setEnemyKilled(game.getEnemyKilled() + 1);
                game.setTotalKills(game.getTotalKills() + 1);
                game.setScore(game.getScore() + 5);

                if (game.getEnemyKilled() >= game.getEnemyCount() && Game.gamemode != Game.GAMEMODE.INFINITE) {
                    game.setEnemyCount(game.getEnemyCount() + 1);
                    game.setEnemyKilled(0);
                    game.setLevel(game.getLevel() + 1);

                    if (game.getLevel() % 2 == 0 && game.getLevel() > 2) {
                        c.createEnemy(game.getEnemyCount());
                        if (game.getLevel() < 10) {
                            c.spawnBoss(1);
                            game.setEnemyCount(game.getEnemyCount() + 1);
                        }
                        else {
                            game.setEnemyCount(game.getEnemyCount() + 2);
                            c.spawnBoss(2);
                        }
                    }
                    else {
                        c.createEnemy(game.getEnemyCount());
                    }


                    if (game.getLevel() > Game.LEVELS_TO_WIN && Game.gamemode == Game.GAMEMODE.LEVELS)
                    {
                        game.endGame();
                        game.saveHighscoreAndStopGame(game.levelsHighScoreSave);
                        Game.part = Game.PART.WIN;
                    }
                }
                if (Game.gamemode == Game.GAMEMODE.TIMED) {
                    if (game.getScore() >= Game.TIMED_MAX_SCORE) {
                        game.endGame();
                        game.saveHighscoreAndStopGame(game.timedHighScoreSave);
                        Game.part = Game.PART.WIN;
                    }
                }
            }
        }

        if (Physics.collisionBD(this, game.ed)) {
            int randomNumber = r.nextInt(3 - 1 + 1) + 1;
            if (randomNumber == 1) {
                game.setScore(game.getScore() + 1);
                c.removeEntity(this);
                c.removeEntity(Physics.entityD);
            }
            if (Game.gamemode == Game.GAMEMODE.TIMED) {
                if (game.getScore() >= Game.TIMED_MAX_SCORE) {
                    game.endGame();
                    game.saveHighscoreAndStopGame(game.timedHighScoreSave);
                    Game.part = Game.PART.WIN;
                }
            }
        }

        if (y > Game.HEIGHT)
            c.removeEntity(this);
    }



    public void render(Graphics g) {

        if (key.equals("p1"))
            g.drawImage(t.player1Bullet, (int) x, (int) y, null);
        else
            g.drawImage(t.player2Bullet, (int) x, (int) y, null);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
