package server;

import packet.Packet;
import session.ClientSession;

public class ServerHandler implements Runnable{
    ClientSession clientSession;
    GameCore gameCore;

    public ServerHandler(ClientSession clientSession, GameCore gameCore){
        this.clientSession = clientSession;

        this.gameCore = gameCore;
    }

    @Override
    public void run() {
//        System.out.println("Handler is running");
        String msg = dequeuePacketFromClient();

        Packet packet = new Packet(msg.getBytes());
//        System.out.println("Receive " + packet);

        try {
            gameCore.execute(packet, clientSession);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        ServerSender serverSender = new ServerSender();
//        serverSender.sendMessageToClient(clientSession, msg);


    }

    public String dequeuePacketFromClient(){
        return clientSession.getPacketReader().getFullMessage();
    }
}
