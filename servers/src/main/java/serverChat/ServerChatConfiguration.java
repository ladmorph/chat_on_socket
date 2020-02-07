package serverChat;

public class ServerChatConfiguration {

    private int serverPort;
    private int notificationTime;

    public int getPort() {
        return serverPort;
    }

    public void setPort(int port) {
        serverPort = port;
    }

    public int getServerPort() {
        return serverPort;
    }

    public ServerChatConfiguration setServerPort(int serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public int getNotificationTime() {
        return notificationTime;
    }

    public ServerChatConfiguration setNotificationTime(int notificationTime) {
        this.notificationTime = notificationTime;
        return this;
    }
}
