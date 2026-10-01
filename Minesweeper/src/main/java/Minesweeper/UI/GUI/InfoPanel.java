package Minesweeper.UI.GUI;

import Minesweeper.Game.Score;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private final Numpad countMines;
    private final Numpad time;
    private final SmileButton smileButton;

    public InfoPanel(Window window, Icons icons) {
        super(new BorderLayout());
        setBackground(Color.GRAY);
        countMines = new Numpad(icons);
        smileButton = new SmileButton(window, icons);
        time = new Numpad(icons);
        add(countMines, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.add(new JLabel());
        buttonPanel.add(smileButton);
        buttonPanel.add(new JLabel());
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        add(buttonPanel, BorderLayout.CENTER);
        add(time, BorderLayout.EAST);
    }

    public void update(Score score){
        countMines.update(score.countMines - score.foundedMines);
        time.update(score.time);
    }

    public void setSmileButton(StateButton state) {
        smileButton.setState(state);
    }
}
