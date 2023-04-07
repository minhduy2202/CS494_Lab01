package server;

public class MyServer {
    public static GameCore gameCore = new GameCore();

    public static void main(String[] args) {
        new Thread(gameCore).start();
    }

    public static void handlePacket(ClientSession clientSession) {
        new Thread(new ServerHandler(clientSession, gameCore)).start();
    }
}
