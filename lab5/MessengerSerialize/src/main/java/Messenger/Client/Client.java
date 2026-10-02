package Messenger.Client;

import Messenger.Client.GUI.My_graphics;
import Messenger.Message.ServerMessage;
import Messenger.Message.TypeServerMessage;
import Messenger.Message.UserMessage;
import Messenger.Server.Chat;
import Messenger.Universal.StaticFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {
    private String userName = "";
    private final My_graphics graphics;
    private final ArrayList<UserMessage> sendingMessage = new ArrayList<>();
    private final HashMap<Integer, ChatMessages> chats;
    private final HashMap<Integer, Chat.ChatInfo> chatInfo = new HashMap<>();
    private boolean isUpdated = false;

    public Client() {
        chats = new HashMap<>();
        this.graphics = new My_graphics(this);
    }

    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 1123;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-a")) {
                ip = args[++i];
            } else if (args[i].equals("-p")) {
                port = Integer.parseInt(args[++i]);
            }
        }
        Client client = new Client();
        if (client.exit()) {
            System.exit(0);
        }
        ClientThread clientThread = new ClientThread(client, ip, port);
        Thread thread = new Thread(clientThread);
        thread.start();
        client.start();
        if (client.exit()) {
            thread.interrupt();
        }
    }

    public HashMap<Integer, ChatMessages> getChats() {
        synchronized (chats) {
            return new HashMap<>(chats);
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void start() {
        graphics.start();
    }

    public boolean exit() {
        return graphics.exit();
    }

    private void getMessage(String data, int chatId) {
        if (chats.containsKey(chatId)) {
            Chat.Message message;
            try {
                message = (Chat.Message) StaticFunctions.deserializeFromString(data);
                ArrayList<Chat.Message> messages = chats.get(chatId).messages;
                if (messages.size() < chats.get(chatId).maxCountMessages) {
                    messages.add(message);
                } else {
                    messages.removeFirst();
                    messages.add(message);
                }
            } catch (IOException | ClassNotFoundException _) {
            }
        }
        graphics.update(this);
    }

    public HashMap<Integer, Chat.ChatInfo> getChatInfo() {
        return chatInfo;
    }

    private void getChatInfo(String data) {
        try {
            Chat.ChatInfo info = (Chat.ChatInfo) StaticFunctions.deserializeFromString(data);
            chatInfo.remove(info.getChatId());
            chatInfo.put(info.getChatId(), info);
        } catch (IOException | ClassNotFoundException _) {
        }
        graphics.update(this);
    }

    private void removeChatFromLists(int chatId) {
        chats.remove(chatId);
        isUpdated = true;
        graphics.update(this);
        graphics.setPanelOpenedChats(null);
    }

    private void addNewMessages(String data) {
        try {
            Chat.ChatMessage chatMessage = (Chat.ChatMessage) StaticFunctions.deserializeFromString(data);
            int chatId = chatMessage.chatId();
            synchronized (chats) {
                if (!chats.containsKey(chatId)) {
                    ChatMessages chatMessages = new ChatMessages(chatMessage.nameChat(), chatId, Math.max(100, chatMessage.messages().size()), chatMessage.messages());
                    chats.put(chatId, chatMessages);
                    isUpdated = true;
                } else {
                    ChatMessages chatMessages = chats.get(chatId);
                    for (Chat.Message message : chatMessage.messages().reversed()) {
                        chatMessages.messages.addFirst(message);
                    }
                    chatMessages.maxCountMessages = Math.max(chatMessages.maxCountMessages, chatMessages.messages.size());
                }
            }
        } catch (IOException | ClassNotFoundException _) {
        }
        graphics.update(this);
    }

    public void receiveMessage(ServerMessage message) {

        if (message == null) {
            return;
        }
        TypeServerMessage type = message.type();
        switch (type) {
            case Simple, Image -> getMessage(message.data(), message.chatId());
            case ChatInfo -> getChatInfo(message.data());
            case Delete -> removeChatFromLists(message.chatId());
            case History -> addNewMessages(message.data());
            case RenameChat -> {
                ChatMessages chat = chats.getOrDefault(message.chatId(), null);
                if (chat != null) {
                    chat.name = message.data();
                }
                Chat.ChatInfo info = chatInfo.getOrDefault(message.chatId(), null);
                if (info != null) {
                    info.setNameChat(message.data());
                }
                graphics.update(this);
            }
            case RemoveUser -> graphics.update(this);
        }
    }

    public UserMessage getSendingMessage() {
        if (sendingMessage.isEmpty()) {
            return null;
        }
        return sendingMessage.removeFirst();
    }

    public void addSendingMessage(UserMessage message) {
        sendingMessage.add(message);
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public static class ChatMessages {
        public String name;
        public int chatId;
        public int maxCountMessages;
        public ArrayList<Chat.Message> messages;

        public ChatMessages(String name, int chatId, int maxCountMessages, ArrayList<Chat.Message> messages) {
            this.name = name;
            this.chatId = chatId;
            this.maxCountMessages = maxCountMessages;
            this.messages = messages;
        }
    }
}
