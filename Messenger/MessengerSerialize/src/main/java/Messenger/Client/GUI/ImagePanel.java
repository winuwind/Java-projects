package Messenger.Client.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {
    private Image image;

    public ImagePanel(String base64String) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            BufferedImage imageOriginal = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if(imageOriginal == null) {
                image = null;
                return;
            }
            image = imageOriginal.getScaledInstance(300, imageOriginal.getHeight() * 300 / imageOriginal.getWidth(), Image.SCALE_SMOOTH);
        } catch (Exception _) {
        }
        setPreferredSize(new Dimension(300, image.getHeight(null) * 300 / image.getWidth(null))); // размер панели
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int x = (getWidth() - image.getWidth(null)) / 2;
            int y = (getHeight() - image.getHeight(null)) / 2;
            g.drawImage(image, x, y, this);
        }
    }
}