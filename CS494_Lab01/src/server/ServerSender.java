package server;

import packet.Packet;
import session.ClientSession;

import java.util.List;

public class ServerSender {
    public void sendMessageToClient(ClientSession clientSession, String msg){
        clientSession.sendMessage(msg);
    }
    public void sendMessageToAllClients(List<ClientSession> acceptedClientSession, String msg){
        for (ClientSession cs: acceptedClientSession){
            sendMessageToClient(cs, msg);
        }
    }

    synchronized public void sendPacketToClient(ClientSession clientSession, Packet packet) {
        clientSession.sendPacket(packet);
    }

    synchronized public void sendPacketToAllClients(List<ClientSession> clientSessions, Packet packet) {
        for (ClientSession cs: clientSessions) {
            sendPacketToClient(cs, packet);
        }
    }
}
