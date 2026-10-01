package Minesweeper.UI.GUI;

import Minesweeper.Field.Cell;
import Minesweeper.Field.Field;
import Minesweeper.Field.StateMine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainPanel extends JPanel {
    Window window;

    public MainPanel(Window window, Icons icons) {
        super(new GridLayout(window.getGame().getField().getSize().getHeight(), window.getGame().getField().getSize().getWidth()));
        this.window = window;
        setBackground(Color.LIGHT_GRAY);
        int width = window.getGame().getField().getSize().getWidth();
        int height = window.getGame().getField().getSize().getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                JButton button = getJButton(icons.getIcon(new Cell(StateMine.Mine)), x, y);
                button.setPreferredSize(new Dimension(16, 16));
                add(button);
            }
        }
        setMaximumSize(new Dimension(width * 16, height * 16));
    }

    private JButton getJButton(ImageIcon initialIcon, int x, int y) {
        String x_str = String.valueOf(x);
        String y_str = String.valueOf(y);
        JButton button = new JButton(initialIcon);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point leftUpCorner = button.getLocationOnScreen();
                if (e.getLocationOnScreen().x > leftUpCorner.x && e.getLocationOnScreen().x < leftUpCorner.x + button.getWidth() &&
                        e.getLocationOnScreen().y > leftUpCorner.y && e.getLocationOnScreen().y < leftUpCorner.y + button.getHeight()) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        window.setCommand("Open " + x_str + " " + y_str);
                        window.setFlagCommand(true);
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        window.setCommand("Note " + x_str + " " + y_str);
                        window.setFlagCommand(true);
                    }
                } else {
                    window.setCommand("Release " + x_str + " " + y_str);
                    window.setFlagCommand(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                window.setCommand("Press " + x_str + " " + y_str);
                window.setFlagCommand(true);
            }
        });
        return button;
    }

    public void fullUpdate(Icons icons) {
        Field field = window.getGame().getField();
        int width = field.getSize().getWidth();
        int height = field.getSize().getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Component component = getComponent(y * width + x);
                if (component instanceof JButton button) {
                    button.setIcon(icons.getIcon(field.getCell(x, y)));
                }
            }
        }
    }

    public void update(Icons icons) {
        Field field = window.getGame().getField();
        int width = field.getSize().getWidth();
        int height = field.getSize().getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (field.getCell(x, y).isUpdated()) {
                    Component component = getComponent(y * width + x);
                    if (component instanceof JButton button) {
                        button.setIcon(icons.getIcon(field.getCell(x, y)));
                    }
                    field.getCell(x, y).setUpdated(false);
                }
            }
        }
    }
}
