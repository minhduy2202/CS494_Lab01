package server;

import session.ClientSession;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class ServerNetwork implements Runnable {
    private final Selector mainSelector;
    private final ServerSocketChannel serverSocketChannel;

    private GameCore gameCore = null;
    private ArrayList<ClientSession> acceptedClientSession = new ArrayList<ClientSession>();

    public ServerNetwork(Selector selector, ServerSocketChannel serverSocketChannel, GameCore gameCore) {
        this.serverSocketChannel = serverSocketChannel;
        this.mainSelector = selector;
        this.gameCore = gameCore;
    }

    @Override
    public void run() {
        while (mainSelector.isOpen()) {
            System.out.println("Waiting for client to connect");
            printAllUserName();

            try {
                mainSelector.select();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            Set<SelectionKey> readyKeys = mainSelector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (!key.isValid()) {
                    continue;
                }
                try {
                    if (key.isAcceptable()) {
                        handleAccept(key, serverSocketChannel);
                    }

                    if (key.isValid() && key.isReadable()) {
                        handleRead(key);
                    }

                    if (key.isValid() && key.isWritable()) {
                        handleWrite(key);
                    }

                } catch (IOException e) {
                    key.cancel();
                    try {
                        key.channel().close();
                        System.out.println("Client leave server");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

    private void handleAccept(SelectionKey key, ServerSocketChannel serverSocketChannel) throws IOException {
        // ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel)
        // key.channel();
        SocketChannel clientChannel = serverSocketChannel.accept();
//        System.out.println("Accepted connection from" + clientChannel);
        clientChannel.configureBlocking(false);

        ClientSession clientSession = new ClientSession(clientChannel, mainSelector);
        clientChannel.register(mainSelector, SelectionKey.OP_READ, clientSession);
        acceptedClientSession.add(clientSession);
        gameCore.addClientSession(clientSession);
    }

    // handle writing to channels
    private void handleWrite(SelectionKey key) throws IOException {
        ClientSession clientSession = (ClientSession) key.attachment();
        clientSession.write(key);
    }

    // handle reading from the channels
    private void handleRead(SelectionKey key) throws IOException {
        ClientSession clientSession = (ClientSession) key.attachment();
        clientSession.read(key);
        this.handlePacket(clientSession);
    }

    private void handlePacket(ClientSession clientSession){
        new Thread(new ServerHandler(clientSession, this.gameCore)).start();
    }

    private void printAllUserName() {
        if (this.acceptedClientSession.size() == 0) {
            System.out.println("There is no connected player");
            return;
        }
        System.out.print("Current players: ");
        for (ClientSession cs : this.acceptedClientSession) {
            System.out.print(cs.getUsername() + ", ");
        }
        System.out.println();
    }
}
