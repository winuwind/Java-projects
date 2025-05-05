package Messenger.Client.GUI;

import Messenger.Client.Client;
import Messenger.Client.GUI.Button.FileChooser;
import Messenger.Message.TypeUserMessage;
import Messenger.Message.UserMessage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class PanelOpenedChat extends JPanel {
    private final PanelMessages panelMessages;
    private final int chatId;
    private final JPanel panelChatName;
    private final Client client;
    private final My_graphics frame;

    public PanelOpenedChat(My_graphics frame, Client client, int chatId) {
        super();
        this.chatId = chatId;
        this.client = client;
        this.frame = frame;
        panelMessages = new PanelMessages(client, chatId);
        panelChatName = new JPanel();
        panelChatName.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

        setPanelChatName();

        setLayout(new BorderLayout());
        panelMessages.setPreferredSize(new Dimension(470, 500));

        JPanel panelSender = new JPanel();
        panelSender.setLayout(new BorderLayout());

        JButton imageButton = new JButton("Image");

        JTextArea input = new JTextArea();
        input.setLineWrap(true);
        input.setWrapStyleWord(true);
        input.getDocument().addDocumentListener(new DocumentListener() {
            void updateHeight() {
                int lineCount = input.getLineCount();
                JTextArea textArea = new JTextArea();
                FontMetrics fontMetrics = textArea.getFontMetrics(textArea.getFont());
                int newHeight = Math.min(450, lineCount * fontMetrics.getHeight());
                int oldHeight = input.getPreferredSize().height;
                input.setPreferredSize(new Dimension(input.getPreferredSize().width, newHeight));
                input.revalidate();
                input.repaint();
                panelMessages.setPreferredSize(new Dimension(400, panelMessages.getPreferredSize().height + oldHeight - newHeight));
                panelMessages.revalidate();
                panelMessages.repaint();
                revalidate();
                repaint();
            }

            @Override public void insertUpdate(DocumentEvent e) { updateHeight(); }
            @Override public void removeUpdate(DocumentEvent e) { updateHeight(); }
            @Override public void changedUpdate(DocumentEvent e) { updateHeight(); }
        });


        JButton sendButton = new JButton("Send");

        panelSender.add(imageButton, BorderLayout.WEST);
        panelSender.add(input, BorderLayout.CENTER);
        panelSender.add(sendButton, BorderLayout.EAST);
        imageButton.addActionListener(_ -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose image");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileChooser.ImageFileFilter());
            fileChooser.setAccessory(new FileChooser.ImagePreview(fileChooser));

            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    File selectedFile = fileChooser.getSelectedFile();
                    byte[] imageBytes = new byte[(int) selectedFile.length()];
                    FileInputStream fileInputStream = new FileInputStream(selectedFile);
                    fileInputStream.read(imageBytes);
                    fileInputStream.close();
                    UserMessage message = new UserMessage(TypeUserMessage.Image, Base64.getEncoder().encodeToString(imageBytes), client.getUserName(), chatId);
                    client.addSendingMessage(message);
                } catch (IOException _) {
                }
            }
        });
        sendButton.addActionListener(_ -> sendMessage(input));

        InputMap inputMap = input.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = input.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "customEnter");
        actionMap.put("customEnter", getAction(input));
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK), "newLine");
        actionMap.put("newLine", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input.append("\n");
            }
        });
        add(panelChatName, BorderLayout.NORTH);
        add(panelMessages, BorderLayout.CENTER);
        add(panelSender, BorderLayout.SOUTH);
    }

    private void setPanelChatName() {
        panelChatName.removeAll();
        String name = "Unknown";
        if (client.getChats().containsKey(chatId)) {
            name = client.getChats().get(chatId).name;
        }
        JLabel nameLabel = new JLabel(name);
        panelChatName.add(nameLabel);
        JButton viewButton = new JButton("Info");
        viewButton.addActionListener(_ -> new WindowChatInfo(frame, client, chatId));
        panelChatName.add(viewButton);
    }

    private Action getAction(JTextArea input) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(input);
            }
        };
    }

    private void sendMessage(JTextArea input) {
        String data = input.getText();
        if (data.isEmpty()) {
            return;
        }
        input.setText("");
        UserMessage message = new UserMessage(TypeUserMessage.Simple, data, client.getUserName(), chatId);
        client.addSendingMessage(message);
    }

    public void update() {
        panelMessages.update(client);
        setPanelChatName();
        revalidate();
        repaint();
    }

    public void exit() {
        panelMessages.exit();
    }
}
