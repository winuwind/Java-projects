package Messenger.Client;

import Messenger.Message.ServerMessage;
import Messenger.Message.TypeServerMessage;
import Messenger.Message.TypeUserMessage;
import Messenger.Universal.StaticFunctions;
import Messenger.Message.UserMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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

    private void sendMessage(UserMessage message, DataOutputStream dos) {
        byte[] xmlBytes = StaticFunctions.toXML(message);
        try {
            dos.write(xmlBytes);
            dos.flush();
        } catch (IOException _) {
        }
    }

    private void receiveMessage() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            while (!Thread.currentThread().isInterrupted() && running) {
                byte[] xmlBytes;
                try {
                    int length = dis.readInt();
                    xmlBytes = new byte[length];
                    dis.readFully(xmlBytes);
                } catch (Exception e) {
                    continue;
                }
                ServerMessage serverMessage = StaticFunctions.serverMessageFromXml(xmlBytes);
                if (serverMessage != null) {
                    if (serverMessage.type() == TypeServerMessage.Exit) {
                        running = false;
                    }
                }
                client.receiveMessage(serverMessage);
            }
            dis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Thread readerThread = new Thread(this::receiveMessage);
        readerThread.start();
        DataOutputStream dos;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        while (!Thread.currentThread().isInterrupted()) {
            UserMessage clientMessage = client.getSendingMessage();
            if (clientMessage != null) {
                sendMessage(clientMessage, dos);
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        UserMessage clientMessage = new UserMessage(TypeUserMessage.Exit, "", client.getUserName(), -1);
        sendMessage(clientMessage, dos);
        try {
            dos.close();
        } catch (IOException _) {
        }
        readerThread.interrupt();
    }
}
