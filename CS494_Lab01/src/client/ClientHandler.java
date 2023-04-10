package client;

import packet.Packet;
import server.GameCore;
import session.ClientSession;
import utils.Constants;
import utils.Question;

import java.util.HashMap;
import java.util.Map;
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
    synchronized public void run() {
//        System.out.println("Handler is running");
        String msg = dequeuePacketFromClient();

        Packet packet = new Packet(msg.getBytes());
        int packetID = packet.getPacketId();
        HashMap<String, String> data = packet.getData();
        System.out.println("Receive " + packet);

        switch (packetID){
            case SERVER_LOGIN_PACKET_ID -> {
                if (Objects.equals(data.get(STATUS), SUCCESS)){
                    clientGameCore.setPlayerJoinOrder(Integer.parseInt(data.get(PLAYER_ORDER_NUMBER)));
                    System.out.println("In server login");
                    clientGameCore.changeGameState(WAITING_GAME_START);
                    clientGameCore.setIsUpdate(true);

                } else {
                    System.out.println("Error login");
                }
            }
            case SERVER_START_GAME_PACKET_ID -> {
                clientGameCore.setNumberOfPlayers(Integer.parseInt(data.get(NUMBER_OF_PLAYERS)));
                clientGameCore.setNumberOfQuestions(Integer.parseInt(data.get(NUMBER_OF_QUESTIONS)));
                clientGameCore.setPlayerGameOrder(Integer.parseInt(data.get(PLAYER_ORDER_NUMBER)));
                System.out.println("In server start game");
                clientGameCore.changeGameState(SPLASH_GAME_START);
                clientGameCore.setIsUpdate(true);
            }
            case SERVER_QUESTION_PACKET_ID -> {
                String question = data.get(QUESTION);
                Map<String, String> answers = new HashMap<String, String>(){{
                    put(ANSWER_A, data.get(ANSWER_A));
                    put(ANSWER_B, data.get(ANSWER_B));
                    put(ANSWER_C, data.get(ANSWER_C));
                    put(ANSWER_D, data.get(ANSWER_D));
                }};
                Question receivedQuestion = new Question(clientGameCore.autoIncreaseQuestionIdx(), question, answers, null);
                clientGameCore.setCurQuestion(receivedQuestion);
                clientGameCore.setCurQuestionCandidate(data.get(CANDIDATE));
                clientGameCore.changeGameState(GAME_STARTED);
                clientGameCore.setIsUpdate(true);
            }
        }


//        ServerSender serverSender = new ServerSender();
//        serverSender.sendMessageToClient(clientSession, msg);


    }

    public String dequeuePacketFromClient(){
        return clientSession.getPacketReader().getFullMessage();
    }
}
