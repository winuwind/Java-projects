package Messenger.Server;

import java.io.IOException;
import java.net.Socket;

public class ServerListenerThread implements Runnable {
    private final Server server;

    public ServerListenerThread(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Socket clientSocket;
            try {
                clientSocket = server.getServerSocket().accept();
            } catch (IOException _) {
                break;
            }
            ClientHandler clientHandler = new ClientHandler(clientSocket, server);
            Thread thread = new Thread(clientHandler);
            thread.start();
            server.getClients().add(clientHandler);
            server.getThreads().add(thread);
        }
        for (ClientHandler clientHandler : server.getClients()) {
            clientHandler.exit();
        }
        for (Thread t : server.getThreads()) {
            t.interrupt();
        }
    }
}
