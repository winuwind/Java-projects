package Messenger.Client;

import Messenger.Message.ServerMessage;
import Messenger.Message.TypeServerMessage;
import Messenger.Message.TypeUserMessage;
import Messenger.Universal.StaticFunctions;
import Messenger.Message.UserMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {
    private final Client client;
    private Socket socket;
    private boolean running = true;

    public ClientThread(Client client, String ip, int port) {
        this.client = client;
        try {
            socket = new Socket(ip, port);
        } catch (IOException _) {
            System.out.println("Connection refused");
            System.exit(1);
        }
    }

    private void sendMessage(UserMessage message, PrintWriter out) {
        try {
            String messageString = StaticFunctions.serializeToString(message);
            out.println(messageString);
        } catch (IOException _) {
        }
    }

    private void receiveMessage() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            while (!Thread.currentThread().isInterrupted() && running) {
                String message;
                try {
                    message = in.nextLine();
                    if (message == null) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }
                ServerMessage serverMessage;
                try {
                    serverMessage = (ServerMessage) StaticFunctions.deserializeFromString(message);
                } catch (ClassNotFoundException e) {
                    continue;
                }
                if (serverMessage != null) {
                    if (serverMessage.type() == TypeServerMessage.Exit) {
                        running = false;
                    }
                }
                client.receiveMessage(serverMessage);
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Thread readerThread = new Thread(this::receiveMessage);
        readerThread.start();
        PrintWriter out;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (!Thread.currentThread().isInterrupted()) {
            UserMessage clientMessage = client.getSendingMessage();
            if (clientMessage != null) {
                sendMessage(clientMessage, out);
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        UserMessage clientMessage = new UserMessage(TypeUserMessage.Exit, "", client.getUserName(), -1);
        sendMessage(clientMessage, out);
        out.close();
        readerThread.interrupt();
    }
}
