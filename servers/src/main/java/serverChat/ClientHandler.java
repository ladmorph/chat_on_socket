package serverChat;

import dao.UserDao;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private ServerChat server;
    private ServerChatConfiguration cfg;
    private BufferedWriter writer;
    private BufferedReader reader;

    /**
     * @param socket This is the client socket, we use it for processing and sending messages
     * @param server We use the server to delete
     *               and send a message to all users.
     * @param cfg    The configuration of the chat server.
     */
    public ClientHandler(Socket socket, ServerChat server, ServerChatConfiguration cfg) {
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
                    String message = reader.readLine(); //The following message always comes here: {username: message}
                    int indexOf;

                    if ((indexOf = message.indexOf(":")) != -1)
                        username = message.substring(0, indexOf); // getting a username from message

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
    }
}
