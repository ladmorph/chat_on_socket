package serverChat;

import dao.UserDao;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class ClientHandler implements Runnable {


    private Socket socket;
    private ServerChat server;

    private BufferedWriter writer;

    private BufferedReader reader;


    public ClientHandler(Socket socket, ServerChat server) {
        this.socket = socket;
        this.server = server;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        while (true) {
            String message = null;
            String username = null;
            try {
                if (reader.ready()) {
                    message = reader.readLine();
                    int indexOf;

                    if ((indexOf = message.indexOf(":")) != -1)
                        username = message.substring(0, indexOf);

                    UserDao userDao = new UserDao();

                    int count = userDao.getMessages(username);
                    count++;
                    userDao.updateMessagesByUsername(username, count);
                    server.sendMessageToAll(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
