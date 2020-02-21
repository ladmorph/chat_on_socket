package serverChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class ServerChat {


    private static final Logger logger = Logger.getLogger(ServerSocket.class.getName());
    private ServerChatConfiguration cfg;
    private Set<ClientHandler> clients;

    public ServerChat(ServerChatConfiguration cfg) {
        this.cfg = cfg;
        clients = new HashSet<>();
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
                    logger.info("ServerChat starting with: " + cfg.getPort() + " port!");
                    while (true) {
                        Socket socket = serverSocket.accept();
                        logger.info("New user connected.");
                        ClientHandler clientHandler = new ClientHandler(socket, ServerChat.this, cfg);
                        clients.add(clientHandler);
                        new Thread(clientHandler).start();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        return thread; //return the thread to restart it in case of an error
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

}
