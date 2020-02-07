package socket;

import java.io.*;
import java.net.Socket;

public class ClientSocket {

    private final String HOST = "localhost";
    private final int PORT = 8080;

    private BufferedWriter writer;
    private BufferedReader reader;


    public ClientSocket() throws IOException {
        Socket socket = new Socket(HOST, PORT);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


    }

    public void sendMessage(String message) {
        try {
            if (!message.isEmpty()) {

                writer.write(message + "\n");
                writer.flush();
                if (message.equals("quit"))
                    System.exit(1);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMessage() {
        String answer = null;
        try {
            if ((answer = reader.readLine()) != null)
                System.out.println(answer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
