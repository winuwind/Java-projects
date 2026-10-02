package Messenger.Client.GUI;

import Messenger.Client.Client;
import Messenger.Message.TypeUserMessage;
import Messenger.Message.UserMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class PanelChats extends JPanel {
    private final My_graphics frame;
    private final Client client;
    HashMap<Integer, PanelOpenedChat> panelOpenedChats;
    private final JPanel panelChats;
    private String users;

    public PanelChats(My_graphics frame, Client client, HashMap<Integer, PanelOpenedChat> panelOpenedChats) {
        super();
        this.client = client;
        this.frame = frame;
        this.panelOpenedChats = panelOpenedChats;
        users = "";

        panelChats = new JPanel();
        panelChats.setLayout(new BoxLayout(panelChats, BoxLayout.Y_AXIS));
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BorderLayout());
        helpPanel.add(panelChats, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(helpPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        setLayout(new BorderLayout());
        scrollPane.setPreferredSize(new Dimension(300, 550));
        add(scrollPane, BorderLayout.CENTER);

        JButton newChatButton = new JButton("New Chat");
        add(newChatButton, BorderLayout.SOUTH);
        newChatButton.addActionListener(_ -> {
            newChat(frame);
            if (!users.isEmpty()) {
                UserMessage message = new UserMessage(TypeUserMessage.NewChat, users.replace('\n', ';'), client.getUserName(), -1);
                client.addSendingMessage(message);
            }
        });
        revalidate();
    }

    private void newChat(JFrame frame) {
        JDialog inputDialog = new JDialog(frame, true);
        inputDialog.setSize(300, 150);
        inputDialog.setLayout(new BorderLayout());
        inputDialog.setLocationRelativeTo(null);
        inputDialog.setTitle("Enter name and users");

        JTextField textField = new JTextField();
        inputDialog.add(textField, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(_ -> {
            String userInput = textField.getText();
            if (userInput.isEmpty()) {
                users = "";
                return;
            }
            inputDialog.dispose();
            users = userInput;
        });

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(okButton);

        inputDialog.add(buttonPanel, BorderLayout.SOUTH);

        inputDialog.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK), "Exit");
        inputDialog.getRootPane().getActionMap().put("Exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                users = "";
                inputDialog.dispose();
            }
        });

        inputDialog.setLocationRelativeTo(null);
        inputDialog.setVisible(true);
    }

    public void update() {
        HashMap<Integer, Client.ChatMessages> chats = client.getChats();
        for (Integer i : chats.keySet()) {
            Client.ChatMessages chat = chats.get(i);
            boolean flag = false;
            for (Component component : panelChats.getComponents()) {
                if (component instanceof JPanel panelChat) {
                    if (panelChat.getComponent(0) instanceof JLabel label) {
                        int chatId = Integer.parseInt(label.getText());
                        if (i == chatId) {
                            flag = true;
                            break;
                        }
                    }
                }
            }
            if (!flag) {
                JPanel panelChat = new JPanel();
                panelChat.setLayout(new FlowLayout());
                JLabel idLabel = new JLabel(String.valueOf(i));
                idLabel.setVisible(false);
                panelChat.add(idLabel);
                JButton button = new JButton(chat.name);
                button.addActionListener(_ -> {
                    PanelOpenedChat openedChat = panelOpenedChats.getOrDefault(i, null);
                    frame.setPanelOpenedChats(openedChat);
                });
                button.setPreferredSize(new Dimension(250, 50));
                panelChat.add(button);
                panelChats.add(panelChat);
            }
        }
        for (Component component : panelChats.getComponents()) {
            if (component instanceof JPanel panelChat) {
                if (panelChat.getComponent(0) instanceof JLabel label) {

                    int chatId = Integer.parseInt(label.getText());
                    if (!chats.containsKey(chatId)) {
                        panelChats.remove(panelChat);
                    } else {
                        if (panelChat.getComponent(1) instanceof JButton button) {
                            button.setText(chats.get(chatId).name);
                        }
                    }
                }
            }
        }

        revalidate();
    }

    public void exit() {
    }
}
