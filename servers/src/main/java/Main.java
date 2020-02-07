import apiServer.ApiServer;
import apiServer.ApiServerConfiguration;
import authServer.AuthServer;
import serverChat.ServerChat;
import serverChat.ServerChatConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        ServerChatConfiguration chatCfg = new ServerChatConfiguration();
        chatCfg.setPort(8080);
        ServerChat serverChat = new ServerChat(chatCfg);

        ApiServerConfiguration apiCfg = new ApiServerConfiguration();
        apiCfg.setPort(9090);
        ApiServer apiServer = new ApiServer(apiCfg);

        AuthServer authServer = new AuthServer();


        List<Thread> threads = new ArrayList<>();
        threads.add(serverChat.start());
        threads.add(apiServer.start());
        threads.add(authServer.start());

        while (true) {
            for (Thread t : threads) {
                if (!t.isAlive()) {
                    t.start();
                }
            }
            Thread.sleep(1000);
        }
    }
}
