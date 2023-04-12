package client;

import packet.Packet;
import session.ClientSession;
import utils.Question;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static utils.Constants.*;

public class ClientHandler {
    ClientSession clientSession;

    Player player = new Player();

    private final PropertyChangeSupport support;

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void setStatePlayer(Player player) {
        if (this.player.equals(player)){
            System.out.println("Equal");
        } else {
            System.out.println("Not equal");
        }
        support.firePropertyChange("player", this.player, player);
        this.player = player;
    }

    public ClientHandler(ClientSession clientSession){
        this.clientSession = clientSession;
        support = new PropertyChangeSupport(this);
    }

    public void runHandler() {
        String msg = dequeuePacketFromClient();

        Packet packet = new Packet(msg.getBytes());
        int packetID = packet.getPacketId();
        HashMap<String, String> data = packet.getData();
        System.out.println("Receive " + packet);

        switch (packetID){
            case SERVER_LOGIN_PACKET_ID -> {
                if (Objects.equals(data.get(STATUS), SUCCESS)){
                    System.out.println("In server login");
                    player.setUsername(data.get(USERNAME));
                } else {
                    System.out.println("Error login");
                }
                Player playerNewState = new Player(player);
                playerNewState.setLoginStatus(data.get(STATUS));
                playerNewState.setReceivePacketID(SERVER_LOGIN_PACKET_ID);
                setStatePlayer(playerNewState);
            }
            case SERVER_START_GAME_PACKET_ID -> {
                System.out.println("In server start game");
                Player playerNewState = new Player(player);
                playerNewState.setNumberOfPlayers(Integer.parseInt(data.get(NUMBER_OF_PLAYERS)));
                playerNewState.setNumberOfQuestions(Integer.parseInt(data.get(NUMBER_OF_QUESTIONS)));
                playerNewState.setPlayerOrder(Integer.parseInt(data.get(PLAYER_ORDER_NUMBER)));
                playerNewState.setReceivePacketID(SERVER_START_GAME_PACKET_ID);
                setStatePlayer(playerNewState);
            }
            case SERVER_QUESTION_PACKET_ID -> {
                String question = data.get(QUESTION);
                Map<String, String> answers = new HashMap<String, String>(){{
                    put(ANSWER_A, data.get(ANSWER_A));
                    put(ANSWER_B, data.get(ANSWER_B));
                    put(ANSWER_C, data.get(ANSWER_C));
                    put(ANSWER_D, data.get(ANSWER_D));
                }};

                Question curQuestion = new Question(0, question, answers, null);
                Player playerNewState = new Player(player);
                playerNewState.setQuestion(curQuestion);
                playerNewState.setCurCandidate(data.get(CANDIDATE));
                playerNewState.setCurQuestionIdx(Integer.parseInt(data.get(CUR_QUESTION_NUMBER)));
                playerNewState.setReceivePacketID(SERVER_QUESTION_PACKET_ID);
                setStatePlayer(playerNewState);
            }
            case SERVER_QUESTION_RESULT_PACKET_ID -> {
                Player playerNewState = new Player(player);
                playerNewState.setResult(data.get(RESULT));
                playerNewState.setSolution(data.get(SOLUTION));
                playerNewState.setCurCandidate(data.get(CANDIDATE));
                playerNewState.setReceivePacketID(SERVER_QUESTION_RESULT_PACKET_ID);
                setStatePlayer(playerNewState);
            }
            case SERVER_MOVE_TURN_PACKET_ID -> {
                Player playerNewState = new Player(player);
                if (Objects.equals(data.get(STATUS), SUCCESS)){
                    playerNewState.setUsedSkip(true);
                }
                playerNewState.setMoveTurnStatus(data.get(STATUS));
                playerNewState.setReceivePacketID(SERVER_MOVE_TURN_PACKET_ID);
                setStatePlayer(playerNewState);
            }
            case SERVER_WIN_PACKET_ID -> {
                Player playerNewState = new Player(player);
                playerNewState.setReceivePacketID(SERVER_WIN_PACKET_ID);
                setStatePlayer(playerNewState);
            }
            case SERVER_LOSE_PACKET_ID -> {
                Player playerNewState = new Player(player);
                playerNewState.setReceivePacketID(SERVER_LOSE_PACKET_ID);
                setStatePlayer(playerNewState);
            }
        }
    }

    public String dequeuePacketFromClient(){
        return clientSession.getPacketReader().getFullMessage();
    }
}
