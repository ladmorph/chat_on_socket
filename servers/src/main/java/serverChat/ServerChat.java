package serverChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerChat {


    ServerChatConfiguration cfg;
    private ArrayList<ClientHandler> clients = new ArrayList<>();

    public ServerChat(ServerChatConfiguration cfg) {
        this.cfg = cfg;
    }

    public void sendMessageToAll(String message) {
        for (ClientHandler c : clients)
            c.sendMessage(message);
    }

    public Thread start() {
        Thread thread = new Thread() {
            public void run() {
                try {

                    ServerSocket serverSocket = new ServerSocket(cfg.getPort());
                    System.out.println("Server starting with 8080 port");
                    while (true) {
                        Socket socket = serverSocket.accept();
                        ClientHandler clientHandler = new ClientHandler(socket, ServerChat.this);
                        clients.add(clientHandler);
                        new Thread(clientHandler).start();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        return thread;
    }

}
