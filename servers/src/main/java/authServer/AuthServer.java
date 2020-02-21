package authServer;

import dao.UserDao;
import model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Logger;

public class AuthServer {

    private ServerSocket serverSocket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private static final Logger logger = Logger.getLogger(AuthServer.class.getName());
    private final int PORT = 7070;

    public AuthServer() {}

    public Thread start() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);
                    logger.info("AuthServer starting with: " + PORT + " port!");


                    while (true) {
                        Socket socket = serverSocket.accept();
                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                        String username = reader.readLine();


                        UserDao userDao = new UserDao();
                        int count = 0;

                        try {
                            count = userDao.getMessages(username); // we get the number of messages
                        } catch (javax.persistence.NoResultException ignored) {  // if such a user does not exist we process the exception
                        }

                        User user = new User()
                                .setUsername(username)
                                .setDate(new Date().toString())
                                .setMessages(count);

                        try {
                            userDao.save(user); // trying to save the user
                        } catch (org.hibernate.exception.ConstraintViolationException ex) {
                            userDao.updateByUsername(user); // if the user exists
                        }

                        writer.write("Вы успешно зашли!" + "\n");
                        writer.flush();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return thread; //return the thread to restart it in case of an error
    }
}
