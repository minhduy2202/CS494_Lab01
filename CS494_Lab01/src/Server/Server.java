package Server;
import Server.Packet.ClientPacketType;
import Server.Packet.PacketBuffer;
import Server.Packet.ServerPacketType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

import static Server.Constants.*;

public class Server {
    private Selector selector;
    private ServerSocketChannel serverSocket;
    private ExecutorService threadPool;
    private ConcurrentHashMap<SocketChannel, PacketBuffer> clientBuffers = new ConcurrentHashMap<>();
    private ConcurrentHashMap<SocketChannel, Future<?>> clientFutures = new ConcurrentHashMap<>();

    public void startServer() throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(SERVER_IP, PORT));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        System.out.println("Server start on port " + PORT);

        while (true) {
            System.out.println("Waiting for the client to connect to the server!");
            int numReadyChannels = selector.select();
            if (numReadyChannels <= 0) {
                continue;
            }

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    acceptConnection(key);
                } else if (key.isReadable()) {
                    readFromSocket(key);
                } else if (key.isWritable()) {
                    writeToSocket(key);
                }
            }
        }
    }

    private void acceptConnection(SelectionKey key) throws IOException {
        SocketChannel clientSocketChannel = serverSocket.accept();
        clientSocketChannel.configureBlocking(false);
        clientSocketChannel.register(selector, SelectionKey.OP_READ);
        clientBuffers.put(clientSocketChannel, new PacketBuffer(BUFFER_SIZE));
        System.out.println("Connection Accepted: " + clientSocketChannel.getLocalAddress());
    }

    private void readFromSocket(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        PacketBuffer clientBuffer = clientBuffers.get(socketChannel);
        try {
            int numBytes = socketChannel.read(clientBuffer.getBuffer());
            if (numBytes == -1) {
                disconnectClient(socketChannel);
                return;
            }

            this.parsePacket(clientBuffer, key);

            Future<?> future = clientFutures.get(socketChannel);
            if (future != null) {
                if (future.isDone()) {
                    threadPool.submit(new Worker(socketChannel, clientBuffer));
                } else {
                    future.cancel(true);
                }
            } else {
                threadPool.submit(new Worker(socketChannel, clientBuffer));
            }
        } catch (IOException e) {
            disconnectClient(socketChannel);
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToSocket(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        PacketBuffer clientBuffer = clientBuffers.get(socketChannel);

        try {
            socketChannel.write(clientBuffer.getBuffer());
            if (!clientBuffer.getBuffer().hasRemaining()) {
                key.interestOps(SelectionKey.OP_READ);
            }
        } catch (IOException e) {
            disconnectClient(socketChannel);
            e.printStackTrace();
        }
    }

    private void parsePacket(PacketBuffer packetBuffer, SelectionKey key) throws Exception {
        int packetType = packetBuffer.readInt();
        ClientPacketType type = PacketBuffer.lookupPacket(packetType);

        switch (type) {
            default:
            case INVALID:
                break;

            case SEND_TEST_DATA:
                System.out.println("Go to send test data");
                handleTestData(packetBuffer, key);
                break;
        }
    }

    private void handleTestData(PacketBuffer packetBuffer, SelectionKey key) {
        String strFromClient = packetBuffer.readString(StandardCharsets.US_ASCII);
        System.out.println("Message received: "
                + strFromClient
                + " Message length= " + strFromClient.length());
        String uppercase = strFromClient.toUpperCase();

        PacketBuffer sendPacket = new PacketBuffer(ServerPacketType.RECEIVE_TEST_DATA.getId());
        sendPacket.putString(uppercase, StandardCharsets.US_ASCII);
        SocketChannel clientSocketChannel = (SocketChannel) key.channel();
        clientBuffers.put(clientSocketChannel, sendPacket);

        if (key.isValid() && key.isWritable()){
            try {
                clientSocketChannel.write(clientBuffers.get(clientSocketChannel).getBuffer());
                if (!clientBuffers.get(clientSocketChannel).getBuffer().hasRemaining()) {
                    key.interestOps(SelectionKey.OP_READ);
                }
            } catch (IOException e) {
                disconnectClient(clientSocketChannel);
                e.printStackTrace();
            }
        }
    }

    private void disconnectClient(SocketChannel socketChannel) {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientBuffers.remove(socketChannel);
        Future<?> future = clientFutures.remove(socketChannel);
        if (future != null) {
            future.cancel(true);
        }
    }

    private class Worker implements Runnable {
        private SocketChannel socketChannel;
        private PacketBuffer clientBuffer;

        public Worker(SocketChannel socketChannel, PacketBuffer clientBuffer) {
            this.socketChannel = socketChannel;
            this.clientBuffer = clientBuffer;
        }

        @Override
        public void run() {
            String request = new String(clientBuffer.getBuffer().array()).trim();
            if (request.equals("quit")) {
                disconnectClient(socketChannel);
                return;
            }

            // Process request
            String response = processRequest(request);

            // Store response in buffer
            ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes());
            clientBuffers.put(socketChannel, new PacketBuffer(responseBuffer));

            // Register channel for write events
            clientFutures.put(socketChannel, threadPool.submit(new Sender(socketChannel)));
            selector.wakeup();
        }

        private String processRequest(String request) {
            // Process the request here and return a response string
            return "Hello, " + request + "!\n";
        }
    }

    private class Sender implements Runnable {
        private SocketChannel socketChannel;

        public Sender(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            SelectionKey key = socketChannel.keyFor(selector);
            key.interestOps(SelectionKey.OP_WRITE);
            selector.wakeup();
        }
    }
}
