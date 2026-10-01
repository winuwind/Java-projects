package Messenger.Client.GUI;

import Messenger.Client.Client;
import Messenger.Message.TypeUserMessage;
import Messenger.Message.UserMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class My_graphics extends JFrame {
    private final PanelChats panelChats;
    private final CountDownLatch latch = new CountDownLatch(1);
    private boolean running = true;
    private final HashMap<Integer, PanelOpenedChat> panelOpenedChats = new HashMap<>();
    private final JPanel emptyPanel = new JPanel();
    private JPanel visiblePanel;

    public My_graphics(Client client) {
        super("Messenger");
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JDialog dialog = new JDialog(this, true);
        dialog.setSize(150, 60);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        dialog.getContentPane().setPreferredSize(new Dimension(150, 60));
        dialog.setLocationRelativeTo(null);

        JLabel label = new JLabel("Enter username");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField inputText = new JTextField();
        inputText.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton button = new JButton("Log In");
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addActionListener(_ -> enterUserName(dialog, inputText, client));

        InputMap inputMap = dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = dialog.getRootPane().getActionMap();

        String name = "Enter";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), name);
        actionMap.put(name, getAction(dialog, inputText, client));

        dialog.add(Box.createVerticalStrut(0));
        dialog.add(label);
        dialog.add(Box.createVerticalStrut(0));
        dialog.add(inputText);
        dialog.add(Box.createVerticalStrut(0));
        dialog.add(button);
        dialog.add(Box.createVerticalGlue());

        dialog.pack();
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                running = false;
                dialog.dispose();
            }
        });
        dialog.setVisible(true);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                panelChats.exit();
                for (PanelOpenedChat chat : panelOpenedChats.values()) {
                    chat.exit();
                }
                running = false;
                dispose();
                latch.countDown();
            }
        });

        panelChats = new PanelChats(this, client, panelOpenedChats);
        panelChats.setPreferredSize(new Dimension(300, 550));
        panelChats.setMaximumSize(panelChats.getPreferredSize());
        emptyPanel.setPreferredSize(new Dimension(470, 550));
        emptyPanel.setMaximumSize(emptyPanel.getPreferredSize());

        add(panelChats);
        add(emptyPanel);
        visiblePanel = emptyPanel;

        setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);
    }

    private void enterUserName(JDialog dialog, JTextField inputText, Client client) {
        String username = inputText.getText();
        client.setUserName(username);
        setTitle(username);
        UserMessage message = new UserMessage(TypeUserMessage.Init, "", username, -1);
        client.addSendingMessage(message);
        dialog.dispose();
    }

    private Action getAction(JDialog dialog, JTextField inputText, Client client) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterUserName(dialog, inputText, client);
            }
        };
    }

    public void start() {
        running = true;
        setVisible(true);
        try {
            latch.await();
        } catch (InterruptedException _) {
        }
    }

    public void update(Client client) {
        HashMap<Integer, Client.ChatMessages> chats = client.getChats();
        for (Client.ChatMessages chat : chats.values()) {
            PanelOpenedChat panel = panelOpenedChats.getOrDefault(chat.chatId, null);
            if (panel == null) {
                PanelOpenedChat newPanel = new PanelOpenedChat(this, client, chat.chatId);
                newPanel.setPreferredSize(new Dimension(470, 550));
                newPanel.setMaximumSize(newPanel.getPreferredSize());
                panelOpenedChats.put(chat.chatId, newPanel);
            } else {
                panel.update();
            }
        }
        ArrayList<Integer> chatIds = new ArrayList<>();
        for (int chatId : panelOpenedChats.keySet()) {
            if (!chats.containsKey(chatId)) {
                chatIds.add(chatId);
                PanelOpenedChat panel = panelOpenedChats.get(chatId);
                panel.exit();
            }
        }
        for (Integer chatId : chatIds) {
            panelOpenedChats.remove(chatId);
        }
        panelChats.update();
    }

    public void setPanelOpenedChats(PanelOpenedChat panelOpenedChat) {
        remove(visiblePanel);
        if (panelOpenedChat == null) {
            visiblePanel = emptyPanel;
        } else {
            visiblePanel = panelOpenedChat;
        }
        visiblePanel.revalidate();
        visiblePanel.repaint();
        visiblePanel.setVisible(true);
        add(visiblePanel);
        revalidate();
        pack();
        repaint();
    }

    public boolean exit() {
        return !running;
    }
}
