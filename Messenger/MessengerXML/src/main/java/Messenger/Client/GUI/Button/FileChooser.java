package Messenger.Client.GUI.Button;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FileChooser {
    public static class ImageFileFilter extends FileFilter {
        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) return true;
            String name = file.getName().toLowerCase();
            return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
        }

        @Override
        public String getDescription() {
            return "Image (*.png, *.jpg, *.jpeg)";
        }
    }

    public static class ImagePreview extends JComponent {
        private Image image;

        public ImagePreview(JFileChooser chooser) {
            chooser.addPropertyChangeListener(evt -> {
                if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
                    File file = (File) evt.getNewValue();
                    if (file != null && file.isFile()) {
                        try {
                            image = ImageIO.read(file);
                            if (image != null) {
                                image = image.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
                            }
                        } catch (IOException ignored) {
                            image = null;
                        }
                    } else {
                        image = null;
                    }
                    repaint();
                }
            });
            setPreferredSize(new Dimension(150, 150));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                int x = (getWidth() - image.getWidth(this)) / 2;
                int y = (getHeight() - image.getHeight(this)) / 2;
                g.drawImage(image, x, y, this);
            }
        }
    }
}