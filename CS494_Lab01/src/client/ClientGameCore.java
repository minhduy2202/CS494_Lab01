package client;

import packet.Packet;
import session.ClientSession;
import utils.Constants;
import utils.Question;
import utils.RandomSet;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Objects;

import static utils.Constants.*;

public class ClientGameCore implements Runnable {

    ClientNetwork clientNetwork;

    private Integer gameState = Constants.LOG_IN;

    String username;
    int curPlayerIdx = 0;
    int nTurns = 0;

    int numberOfPlayers = 0;

    int playerJoinOrder = 0;

    int playerGameOrder = 0;

    int numberOfQuestions = 0;
    Question curQuestion = null;
    int curQuestionIdx = 0;

    String curQuestionCandidate = null;
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    boolean isUpdate = false;


    public ClientGameCore() {
        startClientConnection();
    }

    private void startClientConnection(){
        clientNetwork = new ClientNetwork(this, SERVER_IP, PORT);
        new Thread(clientNetwork).start();
    }

    synchronized public void execute() throws IOException {
        switch (this.gameState) {
            case LOG_IN -> {
                try {
                    String msg = "";
                    System.out.println(
                            "Connection accepted by the Server..");

                    System.out.print("Enter packet id: ");
                    int packetId = Integer.parseInt(stdin.readLine());

                    Packet packet = new Packet(packetId);

                    String key = "", value = "";
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
                    clientNetwork.sendPacket2Server(packet);

                    // if no error
                    this.username = value;

                    System.out.println("####################\n");

                    if (packetId == -1) {
                        break;
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    while (true){
                        if (isUpdate){
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case WAITING_GAME_START -> {
                isUpdate = false;
                System.out.println("Log in successfully, wait for the game start");
                System.out.println("You join the game in the " + playerJoinOrder + " place");

                int i = 0;
                while (true){
                    i++;
                    if (i % 1000000000 == 0){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (getUpdate()){
                        break;
                    }
                }
            }
            case SPLASH_GAME_START -> {
                isUpdate = false;
                System.out.println("####################\n");
                System.out.println("Information before the game");
                System.out.println("####################\n");
                System.out.println("Number of players: " + numberOfPlayers);
                System.out.println("Your order: " + playerGameOrder);
                System.out.println("Number of questions: " + numberOfQuestions);
                int i = 0;
                do {
                    i++;
                    if (i % 1000000000 == 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } while (!getUpdate());
            }

            case GAME_STARTED -> {
                isUpdate = false;
                System.out.println("####################\n");
                System.out.println("Game started");
                System.out.println("####################\n");

                this.changeGameState(GIVE_ANSWER);
            }

            case GIVE_ANSWER -> {
                isUpdate = false;
                if (curQuestion == null){
                    System.out.println("Receive null question");
                } else {
                    String question = curQuestion.getQuestion();
                    String answerA = curQuestion.getAnswers().get(ANSWER_A);
                    String answerB = curQuestion.getAnswers().get(ANSWER_B);
                    String answerC = curQuestion.getAnswers().get(ANSWER_C);
                    String answerD = curQuestion.getAnswers().get(ANSWER_D);

                    String yourSolution = null;
                    System.out.println("Question " + curQuestionIdx + " : " + question);
                    System.out.println("A: " + answerA);
                    System.out.println("B: " + answerB);
                    System.out.println("C: " + answerC);
                    System.out.println("D: " + answerD);

                    if (Objects.equals(this.username, curQuestionCandidate)){
                        System.out.print("Enter your solution: ");
                        yourSolution = stdin.readLine();
                        Packet packet = new Packet(CLIENT_ANSWER_PACKET_ID);
                        packet.addKey(ANSWER, yourSolution);
                        clientNetwork.sendPacket2Server(packet);
                    }

                    int i = 0;
                    do {
                        i++;
                        if (i % 1000000000 == 0) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } while (!getUpdate());
                }
            }
//            case OTHER_PLAYER_TURN -> {
//                System.out.println("Starting the game");
//            }
//            case YOUR_PLAYER_TURN -> {
//
//            }
        }

    }

    public void changeGameState(Integer newGameState){
        this.gameState = newGameState;
    }

    public void setIsUpdate(boolean isUpdate){
        this.isUpdate = isUpdate;
    }

    public boolean getUpdate(){
        return this.isUpdate;
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void setPlayerJoinOrder(int playerJoinOrder) {
        this.playerJoinOrder = playerJoinOrder;
    }

    public void setPlayerGameOrder(int playerGameOrder) {
        this.playerGameOrder = playerGameOrder;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public void setCurQuestion(Question receiveQuestion){
        this.curQuestion = receiveQuestion;
    }

    public int autoIncreaseQuestionIdx(){
        this.curQuestionIdx += 1;
        return this.curQuestionIdx;
    }

    public void setCurQuestionCandidate(String username){
        this.curQuestionCandidate = username;
    }
}
