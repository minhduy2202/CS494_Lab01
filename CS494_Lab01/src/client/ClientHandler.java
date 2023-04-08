package client;

import packet.Packet;
import server.GameCore;
import session.ClientSession;

import java.util.HashMap;
import java.util.Objects;

import static utils.Constants.*;

public class ClientHandler implements Runnable{
    ClientSession clientSession;

    ClientGameCore clientGameCore;
    public ClientHandler(ClientSession clientSession, ClientGameCore clientGameCore){
        this.clientSession = clientSession;

        this.clientGameCore = clientGameCore;
    }

    @Override
    public void run() {
//        System.out.println("Handler is running");
        String msg = dequeuePacketFromClient();

        Packet packet = new Packet(msg.getBytes());
        int packetID = packet.getPacketId();
        HashMap<String, String> data = packet.getData();
        System.out.println("Receive " + packet);

        switch (packetID){
            case SERVER_LOGIN_PACKET_ID -> {
                if (Objects.equals(data.get("status"), SUCCESS)){
                    clientGameCore.changeGameState(WAITING_GAME_START);
                }
            }
        }


//        ServerSender serverSender = new ServerSender();
//        serverSender.sendMessageToClient(clientSession, msg);


    }

    public String dequeuePacketFromClient(){
        return clientSession.getPacketReader().getFullMessage();
    }
}
