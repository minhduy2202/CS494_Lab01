package client;

public class MyClient {
//    public ClientGameCore clientGameCore = new ClientGameCore();

    public static void main(String[] args) {
        new Thread(new ClientGameCore()).start();
    }
}
