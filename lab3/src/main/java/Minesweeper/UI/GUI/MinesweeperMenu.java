package Minesweeper.UI.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MinesweeperMenu extends JMenuBar {
    Window window;
    String command;
    boolean flagNewGameCommand;

    MinesweeperMenu(Window window) {
        super();
        this.window = window;
        this.command = "";
        this.flagNewGameCommand = false;
        setBackground(Color.LIGHT_GRAY);
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setFont(new Font("Arial", Font.BOLD, 10));
        gameMenu.add(getMenuItemNewGame());
        gameMenu.add(getMenuItem("Finish Game"));
        gameMenu.add(getMenuItem("Exit"));
        add(gameMenu);
        JMenu runMenu = new JMenu("Run");
        runMenu.setFont(new Font("Arial", Font.BOLD, 10));
        runMenu.add(getMenuItem("Pause"));
        runMenu.add(getMenuItem("Resume"));
        add(runMenu);
        JMenu scoreMenu = new JMenu("Score");
        scoreMenu.setFont(new Font("Arial", Font.BOLD, 10));
        scoreMenu.add(getMenuItem("High Scores"));
        scoreMenu.add(getMenuItem("Save Score"));
        add(scoreMenu);
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(new Font("Arial", Font.BOLD, 10));
        helpMenu.add(getMenuItem("About"));
        add(helpMenu);
    }

    private JMenuItem getMenuItemNewGame() {
        JMenuItem item = new JMenuItem("New Game");
        item.addActionListener(_ -> {
            command = "New Game ";
            showDialogWindow();
            if (flagNewGameCommand) {
                window.setCommand(command);
                window.setFlagCommand(true);
            }
        });
        item.setFont(new Font("Arial", Font.BOLD, 10));
        item.setBackground(Color.LIGHT_GRAY);
        return item;
    }

    private JMenuItem getMenuItem(String name) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(_ -> {
            window.setCommand(name);
            window.setFlagCommand(true);
        });
        item.setFont(new Font("Arial", Font.BOLD, 10));
        item.setBackground(Color.LIGHT_GRAY);
        return item;
    }


    private void showDialogInputWindow(String message) {
        JDialog inputDialog = new JDialog(window.getFrame(), "Enter " + message, true); // true - модальное окно
        inputDialog.setSize(300, 150);
        inputDialog.setLayout(new BorderLayout());

        JTextField textField = new JTextField();
        inputDialog.add(textField, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(_ -> {
            String userInput = textField.getText();
            if(!userInput.matches("\\d+")){
                textField.setText("");
                return;
            }
            else if(message.equals("count mines")){
                if(Integer.parseInt(userInput) > Integer.parseInt(command.split(" ")[3]) * Integer.parseInt(command.split(" ")[5])){
                    textField.setText("");
                    return;
                }
            }
            command += userInput;
            inputDialog.dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> {
            inputDialog.dispose();
            command = "New Game ";
            flagNewGameCommand = false;
        });

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        inputDialog.add(buttonPanel, BorderLayout.SOUTH);

        inputDialog.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK), "Exit");
        inputDialog.getRootPane().getActionMap().put("Exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e){
                inputDialog.dispose();
                command = "New Game ";
                flagNewGameCommand = false;
            }
        });

        inputDialog.setLocationRelativeTo(window.getFrame());
        inputDialog.setVisible(true);
    }

    public void showDialogWindow() {
        JDialog dialog = new JDialog(window.getFrame(), "Settings", true);
        dialog.setSize(300, 250);
        dialog.setLayout(new BorderLayout());

        JLabel textLabel = new JLabel("Choose settings for game");
        textLabel.setFont(textLabel.getFont().deriveFont(20f));
        dialog.add(textLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1));

        JButton BeginnerButton = new JButton("Beginner");
        BeginnerButton.setFont(BeginnerButton.getFont().deriveFont(20f));
        BeginnerButton.addActionListener(_ -> {
            command += "-w 9 -h 9 -m 10";
            flagNewGameCommand = true;
            dialog.dispose();
        });
        buttonPanel.add(BeginnerButton);

        JButton IntermediateButton = new JButton("Intermediate");
        IntermediateButton.setFont(BeginnerButton.getFont().deriveFont(20f));
        IntermediateButton.addActionListener(_ -> {
            command += "-w 16 -h 16 -m 40";
            flagNewGameCommand = true;
            dialog.dispose();
        });
        buttonPanel.add(IntermediateButton);

        JButton ExpertButton = new JButton("Expert");
        ExpertButton.setFont(BeginnerButton.getFont().deriveFont(20f));
        ExpertButton.addActionListener(_ -> {
            command += "-w 30 -h 30 -m 99";
            flagNewGameCommand = true;
            dialog.dispose();
        });
        buttonPanel.add(ExpertButton);

        JButton otherButton = new JButton("Other");
        otherButton.setFont(BeginnerButton.getFont().deriveFont(20f));
        otherButton.addActionListener(_ -> {
            flagNewGameCommand = true;
            command += "-w ";
            showDialogInputWindow("width");
            if (flagNewGameCommand) {
                command += " -h ";
                showDialogInputWindow("height");
            }
            if (flagNewGameCommand) {
                command += " -m ";
                showDialogInputWindow("count mines");
            }
            if (flagNewGameCommand) {
                dialog.dispose();
            } else {
                flagNewGameCommand = true;
            }
        });
        buttonPanel.add(otherButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(BeginnerButton.getFont().deriveFont(20f));
        cancelButton.addActionListener(_ -> {
            command = "";
            window.setFlagCommand(false);
            dialog.dispose();
        });
        buttonPanel.add(cancelButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK), "Exit");
        dialog.getRootPane().getActionMap().put("Exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e){
                command = "";
                window.setFlagCommand(false);
                dialog.dispose();
            }
        });

        dialog.setLocationRelativeTo(window.getFrame());
        dialog.setVisible(true);
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public boolean getFlagNewGameCommand() {
        return flagNewGameCommand;
    }
}
