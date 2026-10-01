package Messenger.Server;

import Messenger.Message.ServerMessage;
import Messenger.Message.TypeServerMessage;
import Messenger.Message.UserMessage;
import Messenger.Message.TypeUserMessage;
import Messenger.Universal.StaticFunctions;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private final ArrayList<ClientHandler> clients;
    private final ArrayList<Thread> threads;
    private final ExecutorService threadPool;
    private final HashMap<Integer, Chat> chats;
    private final boolean flagdb;

    public Server(int port, boolean flagdb) {
        this.port = port;
        this.flagdb = flagdb;
        threads = new ArrayList<>();
        threadPool = Executors.newFixedThreadPool(16);
        clients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.exit(1);
        }

        if(flagdb) {
            chats = DataBase.getChats();
        }
        else {
            chats = new HashMap<>();
        }
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public ArrayList<ClientHandler> getUsers() {
        return clients;
    }

    private int getNewChatId() {
        int i = 0;
        while (chats.containsKey(i)) {
            i++;
        }
        return i;
    }

    private synchronized void createNewChat(String data, String name) {
        String[] users = data.split(";");
        int chatId = getNewChatId();
        Chat chat = new Chat(users, name, chatId, null, flagdb);
        chats.put(chatId, chat);
        if(flagdb) {
            DataBase.createChat(chat);
        }
        ArrayList<Chat.Message> messages = new ArrayList<>(chat.getMessages().subList(Math.max(chat.getMessages().size() - 100, 0), chat.getMessages().size()));
        Chat.ChatMessage chatMessage = new Chat.ChatMessage(chat.getNameChat(), chatId, messages);
        try {
            String newData = StaticFunctions.serializeToString(chatMessage);
            ServerMessage serverMessage = new ServerMessage(TypeServerMessage.History, newData, chatId);

            for (ClientHandler client : clients) {
                for (String user : chat.getUsers()) {
                    if (user.equals(client.getName())) {
                        client.addNewServerMessage(serverMessage);
                        break;
                    }
                }
            }
        } catch (IOException _) {
        }
    }

    private synchronized void addUsersToChat(String data, String name, int chatId) {
        String[] users = data.split(";");
        Chat chat = chats.getOrDefault(chatId, null);
        if (chat != null) {
            chat.addUser(users, name);
            ArrayList<Chat.Message> messages = new ArrayList<>(chat.getMessages().subList(Math.max(chat.getMessages().size() - 100, 0), chat.getMessages().size()));
            Chat.ChatMessage chatMessage = new Chat.ChatMessage(chat.getNameChat(), chatId, messages);
            try {
                String newData = StaticFunctions.serializeToString(chatMessage);
                ServerMessage serverMessage = new ServerMessage(TypeServerMessage.History, newData, chatId);

                for (ClientHandler client : clients) {
                    for (String user : users) {
                        if (chat.isParticipant(user)) {
                            if (user.equals(client.getName())) {
                                client.addNewServerMessage(serverMessage);
                                break;
                            }
                        }
                    }
                }
            } catch (IOException _) {
            }
        }
    }

    private synchronized void removeUserFromChat(String data, String name, int chatId) {
        Chat chat = chats.getOrDefault(chatId, null);
        if (chat != null) {
            chat.removeUser(data, name);
            ServerMessage serverMessage = new ServerMessage(TypeServerMessage.RemoveUser, data, chatId);
            for (ClientHandler client : clients) {
                if (client.getName().equals(name)) {
                    client.addNewServerMessage(serverMessage);
                }
            }
        }
    }

    private synchronized void deleteChat(String name, int chatId) {
        Chat chat = chats.getOrDefault(chatId, null);
        if (chat != null) {
            if (chat.deleteChat(name)) {
                chats.remove(chatId);
                ServerMessage serverMessage = new ServerMessage(TypeServerMessage.Delete, name, chatId);
                for (ClientHandler client : clients) {
                    if (chat.isParticipant(client.getName())) {
                        client.addNewServerMessage(serverMessage);
                    }
                }
            } else {
                ServerMessage serverMessage = new ServerMessage(TypeServerMessage.RemoveUser, name, chatId);
                for (ClientHandler client : clients) {
                    if (!client.getName().equals(name)) {
                        client.addNewServerMessage(serverMessage);
                    } else {
                        client.addNewServerMessage(new ServerMessage(TypeServerMessage.Delete, name, chatId));
                    }
                }
            }
        }
    }

    private synchronized void renameChat(String data, String name, int chatId) {
        Chat chat = chats.getOrDefault(chatId, null);
        if (chat != null) {
            chat.renameChat(data, name);
            ServerMessage serverMessage = new ServerMessage(TypeServerMessage.RenameChat, chat.getNameChat(), chatId);
            for (ClientHandler client : clients) {
                if (chat.isParticipant(client.getName())) {
                    client.addNewServerMessage(serverMessage);
                }
            }
        }
    }

    private synchronized void sendMessage(TypeUserMessage type, String data, String userName, int chatId) {
        Chat chat = chats.getOrDefault(chatId, null);
        if (chat != null) {
            try {
                Chat.Message message = new Chat.Message(data, userName, type == TypeUserMessage.Image);
                message.updateTime();
                chat.newMessage(message);
                String newData = StaticFunctions.serializeToString(message);
                ServerMessage serverMessage = new ServerMessage(TypeServerMessage.Simple, newData, chatId);
                for (ClientHandler client : clients) {
                    if (chat.isParticipant(client.getName())) {
                        client.addNewServerMessage(serverMessage);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private synchronized void init(String user) {
        for (Integer id : chats.keySet()) {
            Chat chat = chats.get(id);
            if (chat != null) {
                if (chat.isParticipant(user)) {
                    ArrayList<Chat.Message> messages = new ArrayList<>(chat.getMessages().subList(Math.max(chat.getMessages().size() - 100, 0), chat.getMessages().size()));
                    Chat.ChatMessage chatMessage = new Chat.ChatMessage(chat.getNameChat(), id, messages);
                    try {
                        String newData = StaticFunctions.serializeToString(chatMessage);
                        ServerMessage serverMessage = new ServerMessage(TypeServerMessage.History, newData, id);
                        for (ClientHandler client : clients) {
                            if (client.getName().equals(user)) {
                                client.addNewServerMessage(serverMessage);
                            }
                        }

                    } catch (IOException _) {
                    }
                }
            }
        }
    }

    private synchronized void getUsersFromChat(int chatId, String user) {
        Chat chat = chats.getOrDefault(chatId, null);
        if (chat != null) {
            Chat.ChatInfo chatInfo = new Chat.ChatInfo(chat.getNameChat(), chat.getNameCreator(), chatId, chat.getUsers());
            try {
                String newData = StaticFunctions.serializeToString(chatInfo);
                ServerMessage serverMessage = new ServerMessage(TypeServerMessage.ChatInfo, newData, chatId);
                for (ClientHandler client : clients) {
                    if (client.getName().equals(user)) {
                        client.addNewServerMessage(serverMessage);
                        return;
                    }
                }

            } catch (IOException _) {
            }
        }
    }

    private void leaveChat(String nameUser, int chatId) {
        Chat chat = chats.getOrDefault(chatId, null);
        if (chat != null) {
            chat.removeUser(nameUser, nameUser);
            ServerMessage serverMessage = new ServerMessage(TypeServerMessage.Delete, nameUser, chatId);
            for (ClientHandler client : clients) {
                if (client.getName().equals(nameUser)) {
                    client.addNewServerMessage(serverMessage);
                }
            }
        }
    }

    private void getMoreMessage(int countMessage, String nameUser, int chatId) {
        if(!flagdb){
            return;
        }
        Chat chat = chats.getOrDefault(chatId, null);
        if (chat != null) {
            ArrayList<Chat.Message> messages = DataBase.getMessages(chatId, countMessage, 100);
            Chat.ChatMessage chatMessage = new Chat.ChatMessage(chat.getNameChat(), chatId, messages);
            try {
                String newData = StaticFunctions.serializeToString(chatMessage);
                ServerMessage serverMessage = new ServerMessage(TypeServerMessage.History, newData, chatId);

                for (ClientHandler client : clients) {
                    if (nameUser.equals(client.getName())) {
                        client.addNewServerMessage(serverMessage);
                        return;
                    }
                }
            } catch (IOException _) {
            }
        }
    }

    public synchronized void newMessage(UserMessage message) {
        if (message == null) {
            return;
        }
        Future<?> future = threadPool.submit(() -> {
            TypeUserMessage typeMessage = message.type();
            switch (typeMessage) {
                case TypeUserMessage.Simple, TypeUserMessage.Image:
                    sendMessage(typeMessage, message.data(), message.userName(), message.chatId());
                    break;
                case TypeUserMessage.NewChat:
                    createNewChat(message.data(), message.userName());
                    break;
                case TypeUserMessage.Users:
                    getUsersFromChat(message.chatId(), message.userName());
                    break;
                case TypeUserMessage.Init:
                    init(message.userName());
                    break;
                case TypeUserMessage.AddUsers:
                    addUsersToChat(message.data(), message.userName(), message.chatId());
                    break;
                case TypeUserMessage.RemoveUser:
                    removeUserFromChat(message.data(), message.userName(), message.chatId());
                    break;
                case TypeUserMessage.DeleteChat:
                    deleteChat(message.userName(), message.chatId());
                    break;
                case TypeUserMessage.RenameChat:
                    renameChat(message.data(), message.userName(), message.chatId());
                    break;
                case TypeUserMessage.LeaveChat:
                    leaveChat(message.userName(), message.chatId());
                    break;
                case TypeUserMessage.GetMessages:
                    getMoreMessage(Integer.parseInt(message.data()), message.userName(), message.chatId());
                    break;
                default:
                    break;
            }
        });
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public ArrayList<Thread> getThreads() {
        return threads;
    }

    public ArrayList<ClientHandler> getClients() {
        return clients;
    }

    public void close() {
        threadPool.shutdownNow();
        try {
            serverSocket.close();
        } catch (IOException _) {

        }
    }

    public static void main(String[] args) {
        int port = 1123;
        String namedb = "serverdb";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p")) {
                port = Integer.parseInt(args[++i]);
            }
            else if (args[i].equals("-bd")) {
                namedb = args[++i];
            }
            else if (args[i].equals("-w")) {
                namedb = "";
            }
        }
        boolean flag = false;
        if(!namedb.isEmpty()){
            System.out.println("Enter password for " + namedb);
            Scanner scanner = new Scanner(System.in);
            String password = scanner.nextLine();
            DataBase.setPASSWORD(password);
            DataBase.setURL(namedb);
            flag = DataBase.testConnection();
            if(!flag){
                System.out.println("Error when connect to database");
                System.exit(0);
            }
        }

        Server server = new Server(port, flag);
        ServerListenerThread serverListenerThread = new ServerListenerThread(server);
        Thread thread = new Thread(serverListenerThread);
        thread.start();
        Scanner scanner = new Scanner(System.in);
        label:
        while (true) {
            String command = scanner.nextLine();
            switch (command) {
                case "exit":
                    thread.interrupt();
                    return;
                case "ip":
                    System.out.println(server.getIp());
                    break;
                case "port":
                    System.out.println(server.getPort());
                    break;
                case "users":
                    for (ClientHandler client : server.getClients()) {
                        System.out.println(client.getName());
                    }
                    break;
                case "chats":
                    for (Chat chat : server.chats.values()) {
                        System.out.println(chat.getNameChat());
                    }
                    break;
                case "close":
                    break label;
                case "help":
                    System.out.println("""
                            "help" - print this message
                            "ip" - print ip address of server
                            "port" - print port of server
                            "users" - print users who has connected to server
                            "chats" - print chats who has connected to server
                            "close" - close the server
                            """);
                    break;
            }
        }
        thread.interrupt();
        server.close();
    }
}
