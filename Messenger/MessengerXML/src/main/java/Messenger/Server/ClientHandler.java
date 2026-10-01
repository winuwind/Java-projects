package Messenger.Server;

import Messenger.Message.ServerMessage;
import Messenger.Message.TypeUserMessage;
import Messenger.Message.UserMessage;
import Messenger.Universal.StaticFunctions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private String name;
    private final Server server;
    private final ArrayList<ServerMessage> serverMessages;
    private boolean running = true;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        serverMessages = new ArrayList<>();
    }

    public void addNewServerMessage(ServerMessage serverMessage) {
        this.serverMessages.add(serverMessage);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void exit() {
        running = false;
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream()); DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {
            Thread readerThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted() && running) {
                    try {
                        int length = dis.readInt();
                        byte[] xmlBytes = new byte[length];
                        dis.readFully(xmlBytes);
                        UserMessage message = StaticFunctions.userMessageFromXML(xmlBytes);
                        if (message.type() == TypeUserMessage.Exit) {
                            running = false;
                        }
                        if (message.type() == TypeUserMessage.Init) {
                            name = message.userName();
                        }
                        server.newMessage(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            readerThread.start();
            while (!Thread.currentThread().isInterrupted() && running) {
                if (serverMessages.isEmpty()) {
                    Thread.sleep(100);
                    continue;
                }
                byte[] xmlBytes = StaticFunctions.toXML(serverMessages.getFirst());
                serverMessages.removeFirst();
                dos.write(xmlBytes);
                dos.flush();
            }
            dis.close();
            dos.close();
            readerThread.interrupt();
            clientSocket.close();
            if (server.getClients().contains(this)) {
                int index = server.getClients().indexOf(this);
                server.getClients().remove(index);
                server.getThreads().remove(index);
            }
        } catch (IOException | InterruptedException _) {
        }
    }
}
