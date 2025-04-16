package Minesweeper.UI.GUI;

import Minesweeper.Field.StateMine;
import Minesweeper.Game.Game;
import Minesweeper.Game.Score;
import Minesweeper.UI.UserInterface;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;


public class Window extends UserInterface{
    private final JFrame frame;
    private final MinesweeperMenu menuBar;
    private MainPanel mainPanel;
    private final InfoPanel infoPanel;
    private final Icons icons;

    public Window() {
        super();
        type = "GUI";
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.GRAY);
        icons = new Icons();
        menuBar = new MinesweeperMenu(this);

        frame.setJMenuBar(menuBar);
        infoPanel = new InfoPanel(this, icons);
        mainPanel = new MainPanel(this, icons);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(infoPanel);
        frame.add(mainPanel);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                fullUpdateWindow();
            }
        });

        setHotKeys();

        timer = new Timer(100, _ -> updateInfoPanel());
        timer.start();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        menuBar.setCommand("New Game ");
        menuBar.showDialogWindow();
        command = menuBar.getCommand();
        if (menuBar.getFlagNewGameCommand()) {
            flagCommand = true;
        } else {
            exitCommand();
        }
    }

    private void setHotKeys(){
        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();

        String name = "Save Game";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), name);
        actionMap.put(name, getAction(name));

        name = "Exit";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK), name);
        actionMap.put(name, getAction(name));

        name = "Finish Game";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), name);
        actionMap.put(name, getAction(name));

        name = "New Game";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), name);
        actionMap.put(name, getAction(name));

        name = "High Scores";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK), name);
        actionMap.put(name, getAction(name));

        name = "About";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, InputEvent.ALT_DOWN_MASK), name);
        actionMap.put(name, getAction(name));

        name = "Pause";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_DOWN_MASK), name);
        actionMap.put(name, getAction(name));

        name = "Resume";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.SHIFT_DOWN_MASK), name);
        actionMap.put(name, getAction(name));
    }

    private Action getAction(String name) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                command = name;
                if(command.equals("New Game")) {
                    command += " -w " + game.getField().getSize().getWidth() + " -h " + game.getField().getSize().getHeight() + " -m " + game.getField().getCountMines();
                }
                flagCommand = true;
            }
        };
    }

    private void updateInfoPanel() {
        Score score = game.getScore();
        infoPanel.update(score);
    }

    @Override
    public void newGameCommand(Game game) {
        this.game = game;
        flagNewRecord = false;
        infoPanel.setSmileButton(StateButton.Simple);
        frame.remove(mainPanel);
        mainPanel = new MainPanel(this, icons);
        frame.add(mainPanel);
        frame.pack();
        frame.revalidate();
        fullUpdateWindow();
        frame.setLocationRelativeTo(null);
    }

    private void fullUpdateWindow() {
        mainPanel.fullUpdate(icons);
    }

    @Override
    public void updateField() {
        mainPanel.update(icons);
        if (game.isGameOver()) {
            if(game.isGameWin()){
                infoPanel.setSmileButton(StateButton.Win);
                if(game.getScore().time < game.getMinTime()) {
                    newRecord();
                }
            }
            else {
                infoPanel.setSmileButton(StateButton.Loss);
            }
        }
        else if(game.isGamePaused()){
            infoPanel.setSmileButton(StateButton.Unknown);
        }
        else{
            infoPanel.setSmileButton(StateButton.Simple);
        }
    }

    private void newRecord() {
        if(flagNewRecord) {
            return;
        }
        else{
            flagNewRecord = true;
        }
        JPanel popupPanel = new JPanel();
        popupPanel.setBackground(Color.GREEN);
        popupPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel popupLabel = new JLabel("New record!", JLabel.CENTER);
        popupLabel.setFont(new Font("Arial", Font.BOLD, 16));
        popupLabel.setForeground(Color.WHITE);

        popupPanel.add(popupLabel);
        popupPanel.setPreferredSize(new Dimension(infoPanel.getSize().width, infoPanel.getSize().height));
        popupPanel.setMaximumSize(new Dimension(infoPanel.getSize().width, infoPanel.getSize().height));

        frame.getContentPane().add(popupPanel);
        frame.pack();
        frame.revalidate();
        frame.repaint();

        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().remove(popupPanel);
                frame.pack();
                frame.revalidate();
                frame.repaint();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public final JFrame getFrame() {
        return frame;
    }


    @Override
    public void printScoresCommand(File file) {
        try {
            HighScores scores = new HighScores();
            scores.printHighScores(frame, file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void aboutCommand() {
        About about = new About();
        about.printAbout(frame);
    }

    @Override
    public void exitCommand() {
        frame.dispose();
        timer.stop();
        System.exit(0);
    }

}