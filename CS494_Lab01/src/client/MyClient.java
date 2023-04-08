package client;

public class MyClient {
    public static ClientGameCore clientGameCore = new ClientGameCore();

    public static void main(String[] args) {
        new Thread(clientGameCore).start();
    }
}
