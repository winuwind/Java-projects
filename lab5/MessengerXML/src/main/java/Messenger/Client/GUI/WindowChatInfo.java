package Messenger.Client.GUI;

import Messenger.Client.Client;

import javax.swing.*;

public class WindowChatInfo extends JDialog {
    private final PanelChatInfo panelChatInfo;
    private final Timer timer;

    public WindowChatInfo(My_graphics frame, Client client, int chatId) {
        super(frame, true);
        panelChatInfo = new PanelChatInfo(this, client, chatId, frame);
        add(panelChatInfo);
        pack();
        setTitle("Chat Info");
        setLocationRelativeTo(frame);
        timer = new Timer(100, _ -> {
            panelChatInfo.update(client, chatId);
            revalidate();
            pack();
            repaint();
        });
        timer.start();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                close();
            }
        });
        setVisible(true);
    }

    public void close() {
        dispose();
        timer.stop();
        panelChatInfo.exit();
    }
}
