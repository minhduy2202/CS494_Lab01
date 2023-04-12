package client;

import packet.Packet;
import session.ClientSession;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientNetwork implements Runnable {
    SocketChannel clientSocketChannel;

    Selector mainSelector;

    String serverIp;
    int serverPort;

//    ClientGameCore clientGameCore;
    private static ClientSession clientSession;

    ClientHandler clientHandler;

    public ClientNetwork( String serverIp, int serverPort){
        this.serverIp = serverIp;
        this.serverPort = serverPort;

        try {
            mainSelector = Selector.open();
            clientSocketChannel = SocketChannel.open();
            clientSocketChannel.configureBlocking(false);

            clientSession = new ClientSession(clientSocketChannel, mainSelector);

            if (clientSocketChannel.connect(new InetSocketAddress(serverIp, serverPort))){
                clientSocketChannel.register(mainSelector, SelectionKey.OP_READ, clientSession);
            } else {
                clientSocketChannel.register(mainSelector, SelectionKey.OP_CONNECT, clientSession);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        this.clientHandler = new ClientHandler(clientSession);
    }


    @Override
    public void run() {
        while (true) {
            try {

                if (mainSelector.select() == 0)
                    continue;

                Set<SelectionKey> readyKeys = mainSelector.selectedKeys();
                Iterator<SelectionKey> iterator = readyKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (!key.isValid())
                        continue;
                    if (key.isConnectable()) {
                        handleConnect(key);
                    }

                    if (key.isReadable()) {
                        handleRead(key);
                    }

                    if (key.isWritable()) {

                        handleWrite(key);
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleConnect(SelectionKey key){
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            socketChannel.finishConnect();
        } catch (IOException e) {
            System.out.println(e);
            key.cancel();
            return;
        }
        key.interestOps(SelectionKey.OP_READ);
    }
    // handle writing to channels
    private void handleWrite(SelectionKey key) throws IOException {
        ClientSession clientSession = (ClientSession) key.attachment();
        clientSession.write(key);
    }

    // handle reading from the channels
    synchronized public void handleRead(SelectionKey key) throws IOException {
        ClientSession clientSession = (ClientSession) key.attachment();
        clientSession.read(key);
        clientHandler.runHandler();
    }

    public synchronized void sendPacket2Server(Packet packet) {
        clientSession.sendPacket(packet);
    }

    public ClientHandler getClientHandlerTmp(){
        return this.clientHandler;
    }
}
