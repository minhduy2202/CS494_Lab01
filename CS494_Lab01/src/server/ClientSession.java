package server;

import packet.Packet;
import packet.PacketReader;
import packet.PacketWriter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ClientSession {
    private SocketChannel clientSocket;
    private Selector mainSelector;
    private GameCore gameCore;
    private PacketReader packetReader;
    private PacketWriter packetWriter;

    private String username = null;

    public ClientSession(SocketChannel clientSocket, Selector mainSelector, GameCore gameCore) {
        this.clientSocket = clientSocket;
        this.mainSelector = mainSelector;
        this.gameCore = gameCore;

        packetReader = new PacketReader(this);
        packetWriter = new PacketWriter(this);
    }

    public void write(SelectionKey key) throws IOException {
        packetWriter.dequeueAndWrite(key);
    }

    // writes as much as it can and returns the total bytes written
    public int writeToSocket(ByteBuffer byteBuffer) throws IOException {
        int bytesWritten = this.clientSocket.write(byteBuffer);
        int totalBytesWritten = bytesWritten;

        while (bytesWritten > 0 && byteBuffer.hasRemaining()) {
            bytesWritten = this.clientSocket.write(byteBuffer);
            totalBytesWritten += bytesWritten;
        }

        return totalBytesWritten;
    }

    // reads from the channel
    public void read(SelectionKey key) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        int bytesRead = this.clientSocket.read(byteBuffer);
        int totalBytesRead = bytesRead;

        while (bytesRead > 0) {

            if (!byteBuffer.hasRemaining()) {
                // if the buffer is filled up, enqueue it and create a new one
                packetReader.enqueueByteBuffer(byteBuffer);
                byteBuffer = ByteBuffer.allocate(4096);
            }
            bytesRead = this.clientSocket.read(byteBuffer);
            totalBytesRead += bytesRead;
        }

        // if it is not an empty buffer
        if (byteBuffer.position() != 0) {
            packetReader.enqueueByteBuffer(byteBuffer);
        }

        if (bytesRead == -1) {
            disconnectClient();
            // end of stream reached
        }

        // process the buffer queue and construct the messages
        packetReader.processBufferQueue();

    }

    // register interest in writing and add the message to the queue
    public void sendMessage(String msg) {

        // indicates that we want to write to this channel

        clientSocket.keyFor(this.mainSelector).interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        // enqueue the message
        this.packetWriter.enqueueMessage(msg);

    }

    public void sendPacket(Packet packet) {

        // indicates that we want to write to this channel

        clientSocket.keyFor(this.mainSelector).interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        // enqueue the message
        this.packetWriter.enqueuePacket(packet);

    }

    // removes all the resources associated with this client and make it offline
    public void disconnectClient() throws IOException {
        // disconnect the socket
        clientSocket.close();
    }

    public void handlePacket() {
        new Thread(new ServerHandler(this, this.gameCore)).start();
    }

    public SocketChannel getClientSocket() {
        return clientSocket;
    }

    public Selector getMainSelector() {
        return this.mainSelector;
    }

    public PacketReader getPacketReader() {
        return this.packetReader;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    // override the equals method to compare two client sessions
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ClientSession)) {
            return false;
        }
        if ((obj instanceof String)) {
            return obj.equals(this.getUsername());
        }
        ClientSession cs = (ClientSession) obj;
        return cs.getUsername().equals(this.getUsername());
    }

    // override the hashCode method to compare two client sessions
    @Override
    public int hashCode() {
        return this.getUsername().hashCode();
    }
}
