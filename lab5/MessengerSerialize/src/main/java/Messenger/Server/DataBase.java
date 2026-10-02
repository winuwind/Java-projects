package Messenger.Server;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class DataBase {
    private static String URL = "jdbc:mysql://localhost:3306/serverdb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static String PASSWORD = "";

    public static void setPASSWORD(String password) {
        PASSWORD = password;
    }

    public static void setURL(String namedb) {
        URL = "jdbc:mysql://localhost:3306/" + namedb + "?useSSL=false&serverTimezone=UTC";
    }

    public static boolean testConnection() {

        String createChatsTableSQL = """
                    CREATE TABLE IF NOT EXISTS chats (
                        id INT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        creator VARCHAR(255) NOT NULL
                    )
                """;

        String createUsersTableSQL = """
                    CREATE TABLE IF NOT EXISTS users (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        chatId INT NOT NULL,
                        user VARCHAR(255),
                        FOREIGN KEY (chatId) REFERENCES chats(id) ON DELETE CASCADE
                    )
                """;

        String createMessagesTableSQL = """
                    CREATE TABLE IF NOT EXISTS messages (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        chatId INT NOT NULL,
                        user VARCHAR(255),
                        type BOOLEAN NOT NULL,
                        data TEXT,
                        created_at DATETIME,
                        FOREIGN KEY (chatId) REFERENCES chats(id) ON DELETE CASCADE
                    )
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createChatsTableSQL);
            stmt.execute(createUsersTableSQL);
            stmt.execute(createMessagesTableSQL);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public synchronized static void deleteChat(int id) {
        String sql = "DELETE FROM chats WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException _) {
        }
    }

    public synchronized static void deleteUser(int id, String username) {
        String sql = "DELETE FROM users WHERE chatId = ? AND name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException _) {
        }
    }

    public synchronized static void createChat(Chat chat) {
        String sql = "INSERT INTO chats (id, name, creator) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, chat.getChatId());
            stmt.setString(2, chat.getNameChat());
            stmt.setString(3, chat.getNameCreator());
            stmt.executeUpdate();
        } catch (SQLException _) {
        }
    }

    public static ArrayList<Chat.Message> getMessages(int id, int offsetFromEnd, int limit) {
        ArrayList<Chat.Message> messages = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            int totalMessages = 0;
            try (PreparedStatement countStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM messages WHERE chatId = ?")) {
                countStmt.setInt(1, id);
                try (ResultSet countRs = countStmt.executeQuery()) {
                    if (countRs.next()) {
                        totalMessages = countRs.getInt(1);
                    }
                }
            }

            int realOffset = Math.max(totalMessages - offsetFromEnd - limit, 0);
            limit = Math.min(limit, Math.max(totalMessages - offsetFromEnd, 0));

            try (PreparedStatement messageStmt = conn.prepareStatement(
                    "SELECT * FROM messages WHERE chatId = ? ORDER BY created_at ASC LIMIT ? OFFSET ?")) {
                messageStmt.setInt(1, id);
                messageStmt.setInt(2, limit);
                messageStmt.setInt(3, realOffset);

                try (ResultSet messageRs = messageStmt.executeQuery()) {
                    while (messageRs.next()) {
                        Chat.Message message = new Chat.Message(
                                messageRs.getString("data"),
                                messageRs.getString("user"),
                                messageRs.getBoolean("type")
                        );

                        Timestamp timestamp = messageRs.getTimestamp("created_at");
                        LocalDateTime createdAt = (timestamp != null) ? timestamp.toLocalDateTime() : null;
                        message.setTime(createdAt);

                        messages.add(message);
                    }
                }
            }
        } catch (SQLException _) {
        }

        return messages;
    }

    public static ArrayList<String> getUsers(int id) {
        ArrayList<String> users = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement usersStmt = conn.prepareStatement(
                    "SELECT * FROM users WHERE chatId = ?")) {
                usersStmt.setInt(1, id);

                try (ResultSet userRs = usersStmt.executeQuery()) {
                    while (userRs.next()) {
                        users.add(userRs.getString("user"));
                    }
                }
            }
        } catch (SQLException _) {
        }
        return users;
    }

    public static HashMap<Integer, Chat> getChats() {
        HashMap<Integer, Chat> chats = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement chatsStmt = conn.prepareStatement(
                    "SELECT * FROM chats")) {
                try (ResultSet chatRs = chatsStmt.executeQuery()) {
                    while (chatRs.next()) {
                        int chatId = chatRs.getInt("id");
                        ArrayList<String> users = getUsers(chatId);
                        String name = chatRs.getString("name");
                        String creator = chatRs.getString("creator");
                        users.addFirst(name);
                        chats.put(chatId, new Chat(users.toArray(new String[0]), creator, chatId, getMessages(chatId, 0, 100), true));
                    }
                }
            }
        } catch (SQLException _) {
        }
        return chats;
    }

    public synchronized static void addMessage(Chat chat, Chat.Message message) {
        String sql = "INSERT INTO messages (chatId, user, type, data, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, chat.getChatId());
            stmt.setString(2, message.getSender());
            stmt.setBoolean(3, message.isImage());
            stmt.setString(4, message.getData());
            stmt.setTimestamp(5, Timestamp.valueOf(message.getSendingTime()));
            stmt.executeUpdate();
        } catch (SQLException _) {
        }
    }

    public synchronized static void addUser(Chat chat, String name) {
        String sql = "INSERT INTO users (chatId, user) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, chat.getChatId());
            stmt.setString(2, name);
            stmt.executeUpdate();
        } catch (SQLException _) {
        }
    }

    public synchronized static void renameChat(Chat chat) {
        String sql = "UPDATE chats SET name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, chat.getNameChat());
            stmt.setInt(2, chat.getChatId());
            stmt.executeUpdate();
        } catch (SQLException _) {
        }
    }
}
