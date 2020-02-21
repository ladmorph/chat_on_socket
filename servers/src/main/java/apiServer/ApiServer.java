package apiServer;

import com.google.gson.Gson;
import dao.UserDao;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

public class ApiServer {

    private ApiServerConfiguration cfg;
    private ServerSocket serverSocket;

    private BufferedReader reader;
    private BufferedWriter writer;

    private static final Logger logger = Logger.getLogger(ApiServer.class.getName());

    public ApiServer(ApiServerConfiguration cfg) {
        this.cfg = cfg;
    }

    public Thread start() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(cfg.getPort());
                    logger.info("ServerChat starting with: " + cfg.getPort() + " port!");
                    while (true) {

                        Socket socket = serverSocket.accept();
                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                        UserDao userDao = new UserDao();
                        List list = userDao.getAllUsers();

                        String json = new Gson().toJson(list);
                        String response = "HTTP/1.1 200 OK\r\n" +
                                "Server: ChatSocket\r\n" +
                                "Content-Type: application/json\r\n" +
                                "Content-Length: " + json.getBytes().length + "\r\n" +
                                "Connection: close\r\n\r\n";
                        String result = response + json;

                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(result.getBytes("UTF-8"));
                        outputStream.flush();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return thread; //return the thread to restart it in case of an error
    }
}
