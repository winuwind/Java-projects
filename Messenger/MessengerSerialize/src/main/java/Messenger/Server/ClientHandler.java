package Messenger.Server;

import Messenger.Message.ServerMessage;
import Messenger.Message.TypeUserMessage;
import Messenger.Message.UserMessage;
import Messenger.Universal.StaticFunctions;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

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
        try (
                Scanner in = new Scanner(clientSocket.getInputStream());
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            Thread readerThread = new Thread(() -> {
                String inputLine;
                while (!Thread.currentThread().isInterrupted() && running) {
                    inputLine = in.nextLine();
                    try {
                        UserMessage message = (UserMessage) StaticFunctions.deserializeFromString(inputLine);
                        if (message != null) {
                            if (message.type() == TypeUserMessage.Exit) {
                                running = false;
                            }
                            if (message.type() == TypeUserMessage.Init) {
                                name = message.userName();
                            }
                            server.newMessage(message);
                        }
                    } catch (IOException | ClassNotFoundException _) {
                    }
                }
            });
            readerThread.start();
            while (!Thread.currentThread().isInterrupted() && running) {
                if (serverMessages.isEmpty()) {
                    Thread.sleep(100);
                    continue;
                }
                String outputLine = StaticFunctions.serializeToString(serverMessages.getFirst());
                serverMessages.removeFirst();
                out.println(outputLine);
            }
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
