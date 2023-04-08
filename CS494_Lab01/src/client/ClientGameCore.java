package client;

import packet.Packet;
import session.ClientSession;
import utils.Constants;
import utils.Question;
import utils.RandomSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

import static utils.Constants.*;

public class ClientGameCore implements Runnable {

    ClientNetwork clientNetwork;

    RandomSet<Question> questionSet = null;

    HashSet<ClientSession> usedMoveTurn = new HashSet<>();

    private Integer gameState = Constants.LOG_IN;
    int curPlayerIdx = 0;
    int nTurns = 0;
    Question curQuestion = null;
    boolean sentQuestion = false;
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    boolean isUpdate = false;

    public ClientGameCore() {
        startClientConnection();
    }

    private void startClientConnection(){
        clientNetwork = new ClientNetwork(this, SERVER_IP, PORT);
        new Thread(clientNetwork).start();
    }

    synchronized public void execute() {
        switch (this.gameState) {
            case LOG_IN -> {
                Integer clientState = 0;

                try {
                    String msg = "";
                    System.out.println(
                            "Connection accepted by the Server..");

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
                    clientNetwork.sendPacket2Server(packet);

                    System.out.println("####################\n");

                    if (packetId == -1) {
                        break;
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
                while (true){
                    if (isUpdate){
                        break;
                    }
                }
            }
            case GAME_STARTED -> {
                System.out.println("Starting the game");
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
        this.isUpdate = true;
    }

    @Override
    public void run() {
        while (true) {
            this.execute();
        }
    }
}
