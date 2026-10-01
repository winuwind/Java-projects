package Minesweeper.UI.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SmileButton extends JButton {
    private StateButton state;
    private StateButton prevState;
    private final Icons icons;

    public SmileButton(Window window, Icons icons) {
        super(icons.getIcon(StateButton.Simple));
        this.icons = icons;
        setPreferredSize(new Dimension(24, 24));
        setMaximumSize(new Dimension(24, 24));
        state = StateButton.Simple;
        prevState = StateButton.Simple;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point leftUpCorner = getLocationOnScreen();
                if (e.getLocationOnScreen().x > leftUpCorner.x && e.getLocationOnScreen().x < leftUpCorner.x + getWidth() &&
                        e.getLocationOnScreen().y > leftUpCorner.y && e.getLocationOnScreen().y < leftUpCorner.y + getHeight()) {
                    window.setCommand("New Game -w " + window.getGame().getField().getSize().getWidth() + " -h " + window.getGame().getField().getSize().getHeight() + " -m " + window.getGame().getField().getCountMines());
                    window.setFlagCommand(true);
                } else {
                    state = prevState;
                    setIcon(icons.getIcon(state));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                prevState = state;
                state = StateButton.Pressed;
                setIcon(icons.getIcon(StateButton.Pressed));
            }
        });
    }

    public void setState(StateButton newState) {
        prevState = state;
        state = newState;
        if(state != prevState) {
            setIcon(icons.getIcon(state));
        }
    }
}
