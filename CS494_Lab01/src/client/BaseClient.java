package client;

/*package whatever //do not write package name here */
import server.packet.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class BaseClient {
    public static void main(String[] args) {

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        Integer clientState = 0;

        try {

            String msg = "";
            System.out.println(
                    "Connection accepted by the Server..");
            SocketChannel client = SocketChannel.open(
                    new InetSocketAddress("localhost", 8080));

            while (true) {
                System.out.print("Enter packet id: ");
                int packetId = Integer.parseInt(stdin.readLine());

                Packet packet = new Packet(packetId);

                String key = "", value;
                while (true) {
                    System.out.print("Enter key (exit): ");
                    key = stdin.readLine();
                    if ((key.toLowerCase().equals("exit"))) {
                        break;
                    }
                    System.out.print("Enter value: ");
                    value = stdin.readLine();
                    packet.addKey(key, value);
                }

                System.out.println("Data to send:" + packet.getData().toString());

                //
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.put(packet.toBytes());
                buffer.flip();
                int bytesWritten = client.write(buffer);
                System.out.println(String.format(
                        "Sent packet: %s (%d bytes)",
                        new Packet(buffer.array()), bytesWritten));

                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int bytesRead = client.read(readBuffer);
                Packet receivePacket = new Packet(readBuffer.array());
                System.out.println(String.format("Received packet (%d bytes)", bytesRead));
                System.out.println("\tPacket id: " + receivePacket.getPacketId());
                System.out.println("\tData: " + receivePacket.getData().toString());

                System.out.println("####################\n");

                if (packetId == -1) {
                    break;
                }
            }

            client.close();
            System.out.println("Client connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
