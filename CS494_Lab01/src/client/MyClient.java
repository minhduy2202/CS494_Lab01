package client;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyClient {
    SocketChannel clientSocketChannel;

    Selector mainSelector;

    private static ClientSession clientSession;
    public static ExecutorService workerPool = Executors.newCachedThreadPool();
}
