package EarthInvaders.Core;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static EarthInvaders.Core.Game.percentageWidth;

public class Textures {

    public BufferedImage player1Bullet, player2Bullet, enemy, enemyBullet, bossBullet, powerUpSpeed, powerUpCooldown, powerUpImmunity, powerUpLife;
    public Image player1, player2, boss, explosion;

    private SpriteSheet spriteSheet;
    private ImageLoader imageLoader;

    public Textures(Game game) {
        spriteSheet = new SpriteSheet(game.getSpriteSheet());
        imageLoader = new ImageLoader();

        getTextures();
    }

    private void getTextures() {
        try {
            player1 = imageLoader.resizeImage(imageLoader.loadGif("res\\player 1.gif"), (int) (64 * percentageWidth), (int) (64 * percentageWidth));
            player2 = imageLoader.resizeImage(imageLoader.loadGif("res\\player 2.gif"), (int) (64 * percentageWidth), (int) (64 * percentageWidth));
            boss = imageLoader.resizeImage(imageLoader.loadGif("res\\boss.gif"), (int) (32 * percentageWidth), (int) (32 * percentageWidth));
            explosion = imageLoader.resizeImage(imageLoader.loadGif("res\\explosion.gif"), (int) (128 * percentageWidth), (int) (128 * percentageWidth));

            powerUpCooldown = imageLoader.resizeImage(imageLoader.loadImage("res\\attackspeed_powerup.png"), (int) (64 * percentageWidth), (int) (64 * percentageWidth));
            powerUpLife = imageLoader.resizeImage(imageLoader.loadImage("res\\health_powerup.png"), (int) (64 * percentageWidth), (int) (64 * percentageWidth));
            powerUpImmunity = imageLoader.resizeImage(imageLoader.loadImage("res\\immune_powerup.png"), (int) (64 * percentageWidth), (int) (64 * percentageWidth));
            powerUpSpeed = imageLoader.resizeImage(imageLoader.loadImage("res\\speed_powerup.png"), (int) (64 * percentageWidth), (int) (64 * percentageWidth));

            player1Bullet = imageLoader.resizeImage(spriteSheet.grabImage(1, 1, 32, 32), (int) (32 * percentageWidth), (int) (32 * percentageWidth));
            player2Bullet = imageLoader.resizeImage(spriteSheet.grabImage(1, 2, 32, 32), (int) (32 * percentageWidth), (int) (32 * percentageWidth));
            enemy = imageLoader.resizeImage(spriteSheet.grabImage(2, 1, 32, 32), (int) (32 * percentageWidth), (int) (32 * percentageWidth));
            enemyBullet = imageLoader.resizeImage(spriteSheet.grabImage(3, 1, 32, 32), (int) (32 * percentageWidth), (int) (32 * percentageWidth));
            bossBullet = imageLoader.resizeImage(spriteSheet.grabImage(3, 2, 32, 32), (int) (32 * percentageWidth), (int) (32 * percentageWidth));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
