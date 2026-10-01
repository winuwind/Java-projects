package Messenger.Universal;

import Messenger.Message.ServerMessage;
import Messenger.Message.TypeServerMessage;
import Messenger.Message.TypeUserMessage;
import Messenger.Message.UserMessage;
import Messenger.Server.Chat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class StaticFunctions {
    public static String escapeXml(String xml) {
        return xml.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    public static String unescapeXml(String escaped) {
        return escaped.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&apos;", "'");
    }

    public static String getText(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() == 0) return "";
        return list.item(0).getTextContent();
    }

    public static byte[] toXML(UserMessage message) {
        String str = String.format("""
                    <usermessage>
                        <type>%s</type>
                        <data>%s</data>
                        <userName>%s</userName>
                        <chatId>%d</chatId>
                    </usermessage>
                """, message.type(), escapeXml(message.data()), message.userName(), message.chatId());
        byte[] xmlBytes = str.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(4 + xmlBytes.length);
        buffer.putInt(xmlBytes.length);
        buffer.put(xmlBytes);
        return buffer.array();
    }

    public static UserMessage userMessageFromXML(byte[] xmlBytes) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlBytes));
            Element root = doc.getDocumentElement();
            root.normalize();

            String typeStr = getText(root, "type");
            String dataEscaped = getText(root, "data");
            String userName = getText(root, "userName");
            int chatId = Integer.parseInt(getText(root, "chatId"));

            String unescapedData = unescapeXml(dataEscaped);
            return new UserMessage(TypeUserMessage.valueOf(typeStr), unescapedData, userName, chatId);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при разборе UserMessage", e);
        }
    }


    public static byte[] toXML(ServerMessage message) {
        TypeServerMessage type = message.type();
        String xml = "";
        switch (type) {
            case Delete, RenameChat, RemoveUser, Exit -> xml = String.format("""
                        <servermessage>
                            <type>%s</type>
                            <data>%s</data>
                            <chatId>%d</chatId>
                        </servermessage>
                    """, message.type(), escapeXml(message.data()), message.chatId());
            case Simple, Image -> xml = String.format("""
                                <servermessage>
                                    <type>%s</type>
                                    <message>
                                        <type>%b</type>
                                        <data>%s</data>
                                        <sender>%s</sender>
                                        <date>%s</date>
                                    </message>
                                    <chatId>%d</chatId>
                                </servermessage>
                            """, message.type(),
                    message.message().isImage(), escapeXml(message.message().getData()),
                    escapeXml(message.message().getSender()), escapeXml(message.message().getSendingTime().toString()),
                    message.chatId());
            case History -> {
                String start = String.format("""
                        <servermessage>
                            <type>%s</type>
                            <chatMessage>
                                <name>%s</name>
                                <chatId>%d</chatId>
                                <messages>
                        """, message.type(), escapeXml(message.chatMessage().nameChat()), message.chatMessage().chatId());
                StringBuilder str = new StringBuilder(start);
                for (Chat.Message message1 : message.chatMessage().messages()) {
                    String messageStr = String.format("""
                                                <message>
                                                <type>%b</type>
                                                <data>%s</data>
                                                <sender>%s</sender>
                                                <date>%s</date>
                                                </message>
                                    """, message1.isImage(), escapeXml(message1.getData()),
                            escapeXml(message1.getSender()), escapeXml(message1.getSendingTime().toString()));
                    str.append(messageStr);
                }
                String end = String.format("""
                                </messages>
                            </chatMessage>
                            <chatId>%d</chatId>
                        </servermessage>
                        """, message.chatId());
                xml = str.append(end).toString();
            }
            case ChatInfo -> {
                String start = String.format("""
                        <servermessage>
                            <type>%s</type>
                            <chatInfo>
                                <name>%s</name>
                                <chatId>%d</chatId>
                                <creator>%s</creator>
                                <users>
                        """, message.type(), escapeXml(message.chatInfo().getChatName()), message.chatInfo().getChatId(), escapeXml(message.chatInfo().getNameCreator()));
                StringBuilder str = new StringBuilder(start);
                for (String user : message.chatInfo().getUsers()) {
                    String userStr = String.format("""
                                        <user>%s</user>
                            """, user);
                    str.append(userStr);
                }
                String end = String.format("""
                                </users>
                            </chatInfo>
                            <chatId>%d</chatId>
                        </servermessage>
                        """, message.chatId());
                xml = str.append(end).toString();
            }
        }
        byte[] xmlBytes = xml.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(4 + xmlBytes.length);
        buffer.putInt(xmlBytes.length);
        buffer.put(xmlBytes);
        return buffer.array();
    }

    public static ServerMessage serverMessageFromXml(byte[] xmlBytes) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlBytes)); // используем байты напрямую
            Element root = doc.getDocumentElement();
            root.normalize();

            TypeServerMessage type = TypeServerMessage.valueOf(getText(root, "type"));
            int chatId = Integer.parseInt(getText(root, "chatId"));

            switch (type) {
                case Delete, RenameChat, RemoveUser, Exit -> {
                    String data = unescapeXml(getText(root, "data"));
                    return new ServerMessage(type, data, chatId, null, null, null);
                }
                case Simple, Image -> {
                    Element msgElem = (Element) root.getElementsByTagName("message").item(0);
                    boolean msgType = Boolean.parseBoolean(getText(msgElem, "type"));
                    String data = unescapeXml(getText(msgElem, "data"));
                    String sender = unescapeXml(getText(msgElem, "sender"));
                    String dateStr = getText(msgElem, "date");

                    Chat.Message message = new Chat.Message(data, sender, msgType);
                    message.setTime(LocalDateTime.parse(dateStr));

                    return new ServerMessage(type, null, chatId, message, null, null);
                }
                case History -> {
                    Element chatElem = (Element) root.getElementsByTagName("chatMessage").item(0);
                    String nameChat = unescapeXml(getText(chatElem, "name"));
                    int chatIdInner = Integer.parseInt(getText(chatElem, "chatId"));

                    ArrayList<Chat.Message> messages = new ArrayList<>();
                    NodeList messageNodes = chatElem.getElementsByTagName("message");
                    for (int i = 0; i < messageNodes.getLength(); i++) {
                        Element msgEl = (Element) messageNodes.item(i);
                        boolean typeMsg = Boolean.parseBoolean(getText(msgEl, "type"));
                        String data = unescapeXml(getText(msgEl, "data"));
                        String sender = unescapeXml(getText(msgEl, "sender"));
                        String dateStr = getText(msgEl, "date");

                        Chat.Message msg = new Chat.Message(data, sender, typeMsg);
                        msg.setTime(LocalDateTime.parse(dateStr));
                        messages.add(msg);
                    }

                    Chat.ChatMessage chatMessage = new Chat.ChatMessage(nameChat, chatIdInner, messages);
                    return new ServerMessage(type, null, chatId, null, chatMessage, null);
                }
                case ChatInfo -> {
                    Element infoElem = (Element) root.getElementsByTagName("chatInfo").item(0);
                    String nameChat = unescapeXml(getText(infoElem, "name"));
                    int chatIdInfo = Integer.parseInt(getText(infoElem, "chatId"));
                    String creator = unescapeXml(getText(infoElem, "creator"));

                    ArrayList<String> users = new ArrayList<>();
                    NodeList userNodes = infoElem.getElementsByTagName("user");
                    for (int i = 0; i < userNodes.getLength(); i++) {
                        String user = userNodes.item(i).getTextContent();
                        users.add(user);
                    }

                    Chat.ChatInfo chatInfo = new Chat.ChatInfo(nameChat, creator, chatIdInfo, users);
                    return new ServerMessage(type, null, chatId, null, null, chatInfo);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при разборе ServerMessage", e);
        }

        return null;
    }
}
