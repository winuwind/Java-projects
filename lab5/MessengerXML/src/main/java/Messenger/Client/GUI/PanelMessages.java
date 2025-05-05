package Messenger.Client.GUI;

import Messenger.Client.Client;
import Messenger.Message.TypeUserMessage;
import Messenger.Message.UserMessage;
import Messenger.Server.Chat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelMessages extends JPanel {
    private final ArrayList<Chat.Message> displayedMessages;
    private final JPanel messagesPanel;
    private final int chatId;
    private final JScrollPane scrollPane;

    public PanelMessages(Client client, int chatId) {
        super();
        setLayout(new BorderLayout());

        this.chatId = chatId;
        displayedMessages = new ArrayList<>();

        JPanel chatPanel = new JPanel(new BorderLayout());

        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        JButton historyButton = new JButton("View more messages");
        historyButton.addActionListener(_ -> {
            UserMessage message = new UserMessage(TypeUserMessage.GetMessages, String.valueOf(displayedMessages.size()), client.getUserName(), chatId);
            client.addSendingMessage(message);
        });
        chatPanel.add(historyButton, BorderLayout.NORTH);
        chatPanel.add(messagesPanel, BorderLayout.SOUTH);
        scrollPane = new JScrollPane(chatPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setVisible(true);
        add(scrollPane, BorderLayout.CENTER);

        if (client.getChats() != null) {
            Client.ChatMessages chatMessages = client.getChats().getOrDefault(chatId, null);
            if (chatMessages != null) {
                ArrayList<Chat.Message> messages_ = chatMessages.messages;
                for (Chat.Message message : messages_) {
                    PanelMessage panel = new PanelMessage(message);
                    displayedMessages.add(message);
                    addMessageBottom(getPanelMessages(client, message, panel));
                }
            }
        }
    }

    public JPanel getPanelMessages(Client client, Chat.Message msg, PanelMessage panel) {
        JPanel newPanel = new JPanel(new FlowLayout());
        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(400 - panel.getPreferredSize().width, panel.getPreferredSize().height));
        if (client.getUserName().equals(msg.getSender())) {
            newPanel.add(emptyPanel);
            newPanel.add(panel);
        } else {
            newPanel.add(panel);
            newPanel.add(emptyPanel);
        }
        return newPanel;
    }

    public void addMessageBottom(JPanel message) {
        messagesPanel.add(message);
        JPanel verticalStrut = new JPanel();
        verticalStrut.setPreferredSize(new Dimension(400, 5));
        verticalStrut.setMinimumSize(new Dimension(400, 5));
        verticalStrut.setMaximumSize(new Dimension(400, 5));
        messagesPanel.add(verticalStrut);
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setValue(verticalBar.getMaximum());
        revalidate();
        repaint();
    }

    public void addMessageTop(JPanel message) {
        JPanel verticalStrut = new JPanel();
        verticalStrut.setPreferredSize(new Dimension(400, 5));
        verticalStrut.setMinimumSize(new Dimension(400, 5));
        verticalStrut.setMaximumSize(new Dimension(400, 5));
        messagesPanel.add(verticalStrut, 0);
        messagesPanel.add(message, 0);
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setValue(verticalBar.getMaximum());
        revalidate();
        repaint();
    }

    public void update(Client client) {
        Client.ChatMessages chatMessages = client.getChats().getOrDefault(chatId, null);
        if (chatMessages == null) {
            return;
        }
        ArrayList<Chat.Message> allMessages = chatMessages.messages;
        if (allMessages.isEmpty()) {
            return;
        }

        int firstKnownIndex = -1;
        for (int i = 0; i < allMessages.size(); i++) {
            if (!displayedMessages.isEmpty() && allMessages.get(i).equals(displayedMessages.getFirst())) {
                firstKnownIndex = i;
                break;
            }
        }

        if (firstKnownIndex > 0) {
            ArrayList<Chat.Message> newTop = new ArrayList<>(allMessages.subList(0, firstKnownIndex));
            for (int i = newTop.size() - 1; i >= 0; i--) {
                Chat.Message msg = newTop.get(i);
                PanelMessage panel = new PanelMessage(msg);
                addMessageTop(getPanelMessages(client, msg, panel));
                displayedMessages.addFirst(msg);
            }
        }

        int lastKnownIndex = -1;
        for (int i = allMessages.size() - 1; i >= 0; i--) {
            if (!displayedMessages.isEmpty() && allMessages.get(i).equals(displayedMessages.getLast())) {
                lastKnownIndex = i;
                break;
            }
        }

        if (lastKnownIndex < allMessages.size() - 1) {
            ArrayList<Chat.Message> newBottom = new ArrayList<>(allMessages.subList(lastKnownIndex + 1, allMessages.size()));
            for (Chat.Message msg : newBottom) {
                PanelMessage panel = new PanelMessage(msg);
                addMessageBottom(getPanelMessages(client, msg, panel));
                displayedMessages.add(msg);
            }
        }
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setValue(verticalBar.getMaximum());
        revalidate();
        repaint();
    }

    public void exit() {
    }
}
