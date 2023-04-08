package server;

import session.ClientSession;

public class MyServer {
    public static GameCore gameCore = new GameCore();

    public static void main(String[] args) {
        new Thread(gameCore).start();
    }
}
