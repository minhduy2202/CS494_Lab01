package Client;

import Client.Packet.ClientPacketType;
import Client.Packet.Packet;
import Client.Packet.PacketBuffer;
import Client.Packet.ServerPacketType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static Client.Constants.PORT;
import static Client.Constants.SERVER_IP;

public class Client {
    private SocketChannel socketChannel;

    private Socket socket;

    private int ClientID;

    private Packet packet;

    public void startClient() throws IOException  {
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(SERVER_IP, PORT));

        while (!socketChannel.finishConnect()) {
            // Wait for connection to complete
            System.out.println("Wait for the connection to the server");
        }

        System.out.println("Connection successfully to server on port" + PORT);
    }

    public void run() throws Exception {
        while (true){
            if (!socketChannel.isConnected()){
                System.out.println("Server is disconnected");
                break;
            }
            PacketBuffer packetBuffer = new PacketBuffer();
            try {
                int numBytes = socketChannel.read(packetBuffer.getBuffer());
                if (numBytes == -1){
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.parsePacket(packetBuffer);
        }
    }

    private void parsePacket(PacketBuffer packetBuffer) throws Exception {
        int packetType = packetBuffer.readInt();
        ServerPacketType type = PacketBuffer.lookupPacket(packetType);

        switch (type) {
            default:
            case INVALID:
                break;

            case RECEIVE_TEST_DATA:
                handleReceiveTestData(packetBuffer);
                break;
        }
    }

    private void handleReceiveTestData(PacketBuffer packetBuffer) throws Exception {
        String uppercase = packetBuffer.readString(StandardCharsets.US_ASCII);
        System.out.println("Receive uppercase string" + uppercase + "from server");
    }

    public void handleHandshakeServer(Packet packet) throws Exception {
        return;
    }

    public void handleSendUsername(Packet packet) throws Exception {
        return;
    }

    public void handleGiveAnswer(Packet packet) throws Exception{
        return;
    }

    public void sendTestData(String data) throws IOException {
        PacketBuffer packetBuffer = new PacketBuffer(ClientPacketType.SEND_TEST_DATA.getId());
        packetBuffer.putString(data, StandardCharsets.US_ASCII);

        socketChannel.write(packetBuffer.getBuffer().flip());

        System.out.println("Send string " + data + " to server");
    }
}
