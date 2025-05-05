package Messenger.Client.GUI;

import Messenger.Server.Chat;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.time.format.DateTimeFormatter;

public class PanelMessage extends JPanel {
    private final String sender;
    private final String text;

    public PanelMessage(Chat.Message message) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setBackground(new Color(255, 80, 255));

        setSize(300, Integer.MAX_VALUE);

        sender = message.getSender();
        text = message.getData();

        JLabel senderLabel = new JLabel(message.getSender());
        senderLabel.setForeground(Color.ORANGE);
        senderLabel.setFont(senderLabel.getFont().deriveFont(Font.BOLD));
        senderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(senderLabel, BorderLayout.NORTH);

        if (!message.isImage()) {

            JTextArea messageArea = new JTextArea(message.getData());
            messageArea.setEditable(false);
            messageArea.setLineWrap(true);
            messageArea.setWrapStyleWord(true);
            messageArea.setBackground(getBackground());
            messageArea.setForeground(Color.WHITE);


            FontMetrics fm = messageArea.getFontMetrics(messageArea.getFont());
            int maxWidth = 300;

            int textWidth = fm.stringWidth(text);
            int lineHeight = fm.getHeight();

            int lines = Math.max(1, countLines());

            int preferredHeight = lines * lineHeight + 10;

            maxWidth = Math.min(maxWidth, textWidth);

            messageArea.setPreferredSize(new Dimension(maxWidth, preferredHeight));
            add(messageArea, BorderLayout.CENTER);
        } else {
            add(new ImagePanel(message.getData()), BorderLayout.CENTER);
        }

        JLabel dateLabel = new JLabel(message.getSendingTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        dateLabel.setForeground(Color.GRAY);
        dateLabel.setFont(dateLabel.getFont().deriveFont(Font.ITALIC, 10f));
        dateLabel.setHorizontalAlignment(JLabel.RIGHT);
        add(dateLabel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(Math.max(getPreferredSize().width, 150), getPreferredSize().height));
        setMaximumSize(new Dimension(300, getPreferredSize().height));

        revalidate();
        repaint();
    }

    private int countLines() {
        int lines = 0;
        int countSymbolsInWord = 0;
        JTextArea area = new JTextArea();
        FontMetrics fm = area.getFontMetrics(area.getFont());
        StringBuilder string = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c == '\n') {
                lines++;
                countSymbolsInWord = 0;
                string = new StringBuilder();
            }
            else if (Character.isWhitespace(c) || "!.,;:()[]{}".indexOf(c) >= 0) {
                countSymbolsInWord = 0;
                string.append(c);
            }
            else {
                countSymbolsInWord++;
                string.append(c);
            }
            int textWidth = fm.stringWidth(string.toString());
            if(textWidth == 300){
                string = new StringBuilder(string.substring(string.length() - countSymbolsInWord));
                if(string.length() == 300){
                    string = new StringBuilder();
                    countSymbolsInWord = 0;
                }
                lines++;
            }
        }
        return ++lines;
    }

    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

        g2.setColor(new Color(239, 96, 19));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

        g2.dispose();
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return text;
    }
}