package Messenger.Server;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Chat {
    private final ArrayList<String> users;
    private String nameChat;
    private final String nameCreator;
    private final int chatId;
    private final ArrayList<Message> messages;
    private final boolean flagdb;

    public Chat(String[] users, String nameCreator, int chatId, ArrayList<Message> messages, boolean flagdb) {
        this.users = new ArrayList<>();
        this.chatId = chatId;
        if (users.length == 0) {
            nameChat = "default";
        } else {
            nameChat = users[0];
        }
        this.nameCreator = Objects.requireNonNullElse(nameCreator, "root");
        this.users.add(nameCreator);
        this.flagdb = flagdb;
        for (int i = 1; i < users.length; i++) {
            boolean found = false;
            for (String user : this.users) {
                if (user.equals(users[i])) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                this.users.add(users[i]);
            }
        }
        this.messages = Objects.requireNonNullElseGet(messages, ArrayList::new);
    }

    public String getNameChat() {
        return nameChat;
    }

    public String getNameCreator() {
        return nameCreator;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public int getChatId() {
        return chatId;
    }

    public void renameChat(String newName, String nameUser) {
        if (!nameUser.equals(nameCreator)) {
            return;
        }
        nameChat = newName;
        if (flagdb) {
            DataBase.renameChat(this);
        }
    }

    public void addUser(String[] newUsers, String nameUser) {
        if (!nameUser.equals(nameCreator)) {
            return;
        }
        for (String newUser : newUsers) {
            boolean found = false;
            for (String user : users) {
                if (user.equals(newUser)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                users.add(newUser);
                if(flagdb) {
                    DataBase.addUser(this, newUser);
                }
            }
        }
    }

    public void removeUser(String user, String nameUser) {
        if (!nameUser.equals(nameCreator) && !user.equals(nameUser)) {
            return;
        }
        if(user.equals(nameCreator)) {
            deleteChat(user);
        }
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).equals(user)) {
                users.remove(i);
                if(flagdb) {
                    DataBase.deleteUser(chatId, user);
                }
                return;
            }
        }
    }

    public boolean deleteChat(String nameUser) {
        if (!nameUser.equals(nameCreator)) {
            removeUser(nameUser, nameCreator);
            return false;
        } else {
            if(flagdb) {
                DataBase.deleteChat(chatId);
            }
            return true;
        }
    }

    public void newMessage(Message message) {
        messages.add(message);
        if(flagdb) {
            DataBase.addMessage(this, message);
        }
        if (messages.size() > 100) {
            messages.removeFirst();
        }
    }

    public boolean isParticipant(String user) {
        for (String user_ : users) {
            if (user.equals(user_)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getUsers() {
        return new ArrayList<>(users);
    }

    public static class Message implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private final boolean type;
        private final String data;
        private LocalDateTime sendingTime;
        private final String sender;

        public Message(String data, String sender, boolean type) {
            this.data = data;
            this.type = type;
            this.sender = sender;
            sendingTime = LocalDateTime.now();
        }

        public boolean isImage() {
            return type;
        }

        public void updateTime() {
            sendingTime = LocalDateTime.now();
        }

        public void setTime(LocalDateTime time) {
            sendingTime = time;
        }

        public String getData() {
            return data;
        }

        public LocalDateTime getSendingTime() {
            return sendingTime;
        }

        public String getSender() {
            return sender;
        }
    }

    public static class ChatInfo implements Serializable {
        String nameChat;
        String nameCreator;
        int chatId;
        ArrayList<String> users;
        @Serial
        private static final long serialVersionUID = 1L;

        public ChatInfo(String nameChat, String nameCreator, int chatId, ArrayList<String> users) {
            this.nameChat = nameChat;
            this.nameCreator = nameCreator;
            this.chatId = chatId;
            this.users = users;
        }

        public String getChatName() {
            return nameChat;
        }

        public int getChatId() {
            return chatId;
        }

        public ArrayList<String> getUsers() {
            return users;
        }

        public String getNameCreator() {
            return nameCreator;
        }

        public void setNameChat(String name) {
            nameChat = name;
        }
    }

    public record ChatMessage(String nameChat, int chatId, ArrayList<Message> messages) implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
    }
}
