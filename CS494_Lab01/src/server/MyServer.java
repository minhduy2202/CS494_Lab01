package server;

import utils.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {
    public static final ExecutorService workerPool = Executors.newCachedThreadPool();

    public static GameCore gameCore = new GameCore();

    public static void main(String[] args) {

        new Thread(gameCore).start();

//        ServerSocketChannel serverSocketChannel;
//        Selector selector;
//        try {
//            serverSocketChannel = ServerSocketChannel.open();
//            InetSocketAddress address = new InetSocketAddress(Constants.SERVER_IP, Constants.PORT);
//            serverSocketChannel.bind(address);
//            serverSocketChannel.configureBlocking(false);
//            selector = Selector.open();
//            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        new Thread(new SelectorLoop(selector, serverSocketChannel, gameCore)).start();
    }

    public static void handlePacket(ClientSession clientSession) {
        workerPool.execute(new ServerHandler(clientSession, gameCore));
    }
}
