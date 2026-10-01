package Messenger.Client.GUI;

import Messenger.Client.Client;
import Messenger.Client.GUI.Button.ButtonEditor;
import Messenger.Client.GUI.Button.ButtonRenderer;
import Messenger.Message.TypeUserMessage;
import Messenger.Message.UserMessage;
import Messenger.Server.Chat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PanelChatInfo extends JPanel {
    private final JPanel topPanel;
    private final JLabel titleLabel;
    private final JLabel ownerLabel;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JScrollPane scrollPane;

    public PanelChatInfo(WindowChatInfo dialog, Client client, int chatId, My_graphics frame) {
        setLayout(new BorderLayout(10, 5));
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        setPreferredSize(new Dimension(400, 600));

        String chatName = "Unknown";
        String ownerName = "root";

        Chat.ChatInfo chatInfo = client.getChatInfo().getOrDefault(chatId, null);

        if (chatInfo == null) {
            UserMessage message = new UserMessage(TypeUserMessage.Users, "", client.getUserName(), chatId);
            client.addSendingMessage(message);
        } else {
            chatName = chatInfo.getChatName();
            ownerName = chatInfo.getNameCreator();
        }

        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        titleLabel = new JLabel("Chat: " + chatName + " (ID: " + chatId + ")");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        topPanel.add(titleLabel);
        ownerLabel = new JLabel("Owner: " + ownerName);
        ownerLabel.setFont(ownerLabel.getFont().deriveFont(Font.PLAIN, 13f));
        topPanel.add(ownerLabel);

        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Add Users");
        btnAdd.addActionListener(_ -> addUser(dialog, client, chatId));
        JButton btnRename = new JButton("Rename Chat");
        btnRename.addActionListener(_ -> renameChat(dialog, client, chatId));
        JButton btnDelete = new JButton("Delete Chat");
        btnDelete.addActionListener(_ -> {
            UserMessage message = new UserMessage(TypeUserMessage.DeleteChat, "", client.getUserName(), chatId);
            frame.setPanelOpenedChats(null);
            client.addSendingMessage(message);
            dialog.close();
        });
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRename);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.CENTER);

        String[] columnNames = {"User", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        TableColumn buttonColumn = table.getColumnModel().getColumn(1);
        buttonColumn.setCellRenderer(new ButtonRenderer("Remove"));
        ButtonEditor buttonEditor = new ButtonEditor(new JCheckBox(), "Remove");
        buttonColumn.setCellEditor(buttonEditor);

        buttonEditor.setListener(_ -> {
            int row = table.getEditingRow();
            if (row >= 0) {
                String name = (String) tableModel.getValueAt(row, 0);
                UserMessage message = new UserMessage(TypeUserMessage.RemoveUser, name, client.getUserName(), chatId);
                client.addSendingMessage(message);
            }
        });

        scrollPane = new JScrollPane(table);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setSize(400, 400);
        scrollPane.setVisible(true);
        add(scrollPane, BorderLayout.SOUTH);
    }

    public void renameChat(JDialog dialog, Client client, int chatId) {
        JDialog inputDialog = new JDialog(dialog, true);
        inputDialog.setSize(300, 150);
        inputDialog.setLayout(new BorderLayout());
        inputDialog.setLocationRelativeTo(null);
        inputDialog.setTitle("Enter new name");

        JTextField textField = new JTextField();
        inputDialog.add(textField, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(_ -> {
            String userInput = textField.getText();
            if (userInput.isEmpty()) {
                return;
            }
            UserMessage message = new UserMessage(TypeUserMessage.RenameChat, userInput, client.getUserName(), chatId);
            client.addSendingMessage(message);
            inputDialog.dispose();
        });

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(okButton);

        inputDialog.add(buttonPanel, BorderLayout.SOUTH);

        inputDialog.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK), "Exit");
        inputDialog.getRootPane().getActionMap().put("Exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputDialog.dispose();
            }
        });

        inputDialog.setLocationRelativeTo(null);
        inputDialog.setVisible(true);
    }

    public void addUser(JDialog dialog, Client client, int chatId) {
        JDialog inputDialog = new JDialog(dialog, true);
        inputDialog.setSize(300, 150);
        inputDialog.setLayout(new BorderLayout());
        inputDialog.setLocationRelativeTo(null);
        inputDialog.setTitle("Enter user's nickname");

        JTextField textField = new JTextField();
        inputDialog.add(textField, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(_ -> {
            String userInput = textField.getText();
            if (userInput.isEmpty()) {
                return;
            }
            UserMessage message = new UserMessage(TypeUserMessage.AddUsers, userInput, client.getUserName(), chatId);
            client.addSendingMessage(message);
            inputDialog.dispose();
        });

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(okButton);

        inputDialog.add(buttonPanel, BorderLayout.SOUTH);

        inputDialog.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK), "Exit");
        inputDialog.getRootPane().getActionMap().put("Exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputDialog.dispose();
            }
        });

        inputDialog.setLocationRelativeTo(null);
        inputDialog.setVisible(true);
    }

    public void setParticipants(ArrayList<String> participants) {
        tableModel.setRowCount(0);
        for (String participant : participants) {
            Object[] row = {participant};
            tableModel.addRow(row);
        }
    }

    public void exit() {
    }

    public void update(Client client, int chatId) {
        UserMessage message = new UserMessage(TypeUserMessage.Users, "", client.getUserName(), chatId);
        client.addSendingMessage(message);
        Chat.ChatInfo chatInfo_ = client.getChatInfo().getOrDefault(chatId, null);

        if (chatInfo_ != null) {
            titleLabel.setText("Chat: " + chatInfo_.getChatName() + " (ID: " + chatId + ")");
            ownerLabel.setText("Owner: " + chatInfo_.getNameCreator());
            setParticipants(chatInfo_.getUsers());
        }
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setValue(verticalBar.getMaximum());
        titleLabel.revalidate();
        titleLabel.repaint();
        ownerLabel.revalidate();
        ownerLabel.repaint();
        topPanel.revalidate();
        topPanel.repaint();
        revalidate();
        repaint();
    }
}
