package EarthInvaders.Core;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {

    public Image resizeImage(Image image, int width, int height) {
        return image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    public BufferedImage resizeImage(BufferedImage image, int width, int height) {
        Image tempImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(tempImage, 0, 0, null);
        g.dispose();

        return resizedImage;
    }

    public BufferedImage loadImage(String path) throws IOException {
        BufferedImage image;
        image = ImageIO.read(new File(path));
        return image;
    }

    public Image loadGif(String path) throws IOException {
        return new ImageIcon(path).getImage();
    }

}
