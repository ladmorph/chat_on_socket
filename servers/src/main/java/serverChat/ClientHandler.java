package serverChat;

import dao.UserDao;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {

    private ServerChat server;
    private ServerChatConfiguration cfg;

    private BufferedWriter writer;
    private BufferedReader reader;

    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    private int countsOfClient = 0;
    private Object object = new Object();

    public ClientHandler(Socket socket, ServerChat server, ServerChatConfiguration cfg) {
        countsOfClient++;

        this.server = server;
        this.cfg = cfg;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        try {
            while (true) {
                String username = null;
                if (reader.ready()) {
                    String message = reader.readLine();
                    int indexOf;

                    if ((indexOf = message.indexOf(":")) != -1)
                        username = message.substring(0, indexOf);

                    UserDao userDao = new UserDao();

                    int count = userDao.getMessages(username);
                    count++;
                    userDao.updateMessagesByUsername(username, count);
                    server.sendMessageToAll(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

    }

    public void sendMessage(String message) {
        try {
            writer.write(message + "\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        server.removeClient(this);
        countsOfClient--;
    }
}
