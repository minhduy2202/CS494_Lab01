package server;

import packet.Packet;
import session.ClientSession;
import utils.Constants;
import utils.Loader;
import utils.Question;
import utils.RandomSet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.*;

public class GameCore implements Runnable {

    private LinkedList<ClientSession> clientSessions = null;

    RandomSet<Question> questionSet = null;

    HashSet<ClientSession> usedMoveTurn = new HashSet<>();

    private Integer gameState = Constants.WAITING_FOR_PLAYERS;
    int curPlayerIdx = 0;
    int nTurns = 0;
    Question curQuestion = null;
    boolean sentQuestion = false;

    ServerSender serverSender = new ServerSender();
    private int curQuestionNumber = 0;

    private Long countDownStartTime = null;

    public GameCore() {
        this.clientSessions = new LinkedList<>();
        this.questionSet = new RandomSet<>(Loader.loadCSV("CS494_Lab01/src/utils/questions.csv"));

        openNonBlockingSocket();
    }

    void openNonBlockingSocket() {
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress(Constants.SERVER_IP, Constants.PORT);
            serverSocketChannel.bind(address);
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new ServerNetwork(selector, serverSocketChannel, this)).start();
    }

    public GameCore(LinkedList<ClientSession> sessions) {
        this.clientSessions = new LinkedList<>();
        this.clientSessions.addAll(sessions);
        this.questionSet = new RandomSet<>(Loader.loadCSV("CS494_Lab01/src/utils/questions.csv"));
    }

    synchronized public void moveToNextPlayer(boolean removeCurrentPlayer) {
        if (removeCurrentPlayer) {
            this.clientSessions.remove(this.curPlayerIdx);
        } else {
            this.curPlayerIdx++;
        }
        this.curPlayerIdx %= clientSessions.size();
    }

    synchronized public void execute(Packet packet, ClientSession sender) throws InterruptedException {

        switch (this.gameState) {

            case Constants.WAITING_FOR_PLAYERS -> {
                if (packet != null && packet.getPacketId() == Constants.CLIENT_LOGIN_PACKET_ID) {
                    String username = packet.getKey(Constants.USERNAME);
                    String status = Constants.SUCCESS;
                    for (ClientSession cs : this.clientSessions) {
                        if (username.equals(cs.getUsername())) {
                            status = Constants.FAIL;
                            break;
                        }
                    }

                    if (status.equals(Constants.SUCCESS)) {
                        sender.setUsername(username);
                    }

                    Packet responsePacket = new Packet(Constants.SERVER_LOGIN_PACKET_ID);
                    responsePacket.addKey(Constants.STATUS, status);
                    responsePacket.addKey(Constants.PLAYER_ORDER_NUMBER, String.valueOf(this.clientSessions.size()));

                    sendPacket2SingleClient(responsePacket, sender);

                    // check constraint that if game has enough players
                    if (this.clientSessions.size() == Constants.MAX_PLAYER) {
                        this.gameState = Constants.GAME_READY;
                    }

                    if (this.turnOnTimer()) {
                        this.countDownStartTime = System.currentTimeMillis();
                    }
                }

                if (this.countDownStartTime != null) {
                    if (System.currentTimeMillis() - this.countDownStartTime > Constants.WAITING_TIME_OUT_MS) {
                        this.gameState = Constants.GAME_READY;
                    }
                }
            }

            case Constants.GAME_READY -> {
                // adjust number of questions
                int maxQuestions = this.clientSessions.size() * 3;
                while (this.questionSet.size() > maxQuestions) {
                    this.questionSet.pollRandom(new Random());
                }

                // send start game packet to all client sessions
                Packet startGamePacket = new Packet(Constants.SERVER_START_GAME_PACKET_ID);
                startGamePacket.addKey(Constants.NUMBER_OF_PLAYERS, String.valueOf(this.clientSessions.size()));
                startGamePacket.addKey(Constants.NUMBER_OF_QUESTIONS, String.valueOf(this.questionSet.size()));
                startGamePacket.addKey(Constants.PLAYER_ORDER_NUMBER, null);
                Collections.shuffle(this.clientSessions);
                for (int i = 0; i < this.clientSessions.size(); i++) {
                    startGamePacket.updateKey(Constants.PLAYER_ORDER_NUMBER, String.valueOf(i + 1));
                    sendPacket2SingleClient(startGamePacket, this.clientSessions.get(i));
                }
                this.gameState = Constants.GAME_STARTED;
                Thread.sleep(2000);
            }

            case Constants.GAME_STARTED -> {
                if (!sentQuestion) {
                    // send question to the current player
                    if (this.curQuestion == null) {
                        this.curQuestion = this.questionSet.pollRandom(new Random());
                        this.curQuestionNumber += 1;
                    }

                    // create new packet to send to the current client turn
                    Packet questionPacket = createQuestionPacket();

//                    sendPacket2SingleClient(questionPacket, this.clientSessions.get(curPlayerIdx));
                    sendPacket2AllClients(questionPacket);

                    sentQuestion = true;
                } else {
                    // handle received packet
                    // check if the packet is from the current player
                    if (packet != null && sender.getUsername().equals(this.clientSessions.get(curPlayerIdx).getUsername())) {
                        // check if the packet is an answer packet
                        if (packet.getPacketId() == Constants.CLIENT_ANSWER_PACKET_ID) {
                            String answer = packet.getKey(Constants.ANSWER);
                            Packet resultPacket = new Packet(Constants.SERVER_QUESTION_RESULT_PACKET_ID);

                            String result = null;
                            if (answer.equalsIgnoreCase(this.curQuestion.getSolution())) {
                                result = Constants.CORRECT;
                            } else {
                                result = Constants.WRONG;
                            }
                            resultPacket.addKey(Constants.RESULT, result);
                            resultPacket.addKey(Constants.SOLUTION, this.curQuestion.getSolution());
                            resultPacket.addKey(Constants.CANDIDATE, this.clientSessions.get(curPlayerIdx).getUsername());

                            if (result.equals(Constants.CORRECT)) {
                                // move to next player
                                this.moveToNextPlayer(false);
                            } else {
                                // remove current player from the game
                                this.moveToNextPlayer(true);
                            }

                            nTurns++;
                            this.sentQuestion = false;
                            this.curQuestion = null;

                            sendPacket2AllClients(resultPacket);

                        } else if (packet.getPacketId() == Constants.CLIENT_MOVE_TURN_PACKET_ID) {
                            // this is a move turn packet
                            String result = Constants.SUCCESS;
                            if (this.usedMoveTurn.contains(sender)) {
                                // this player has already used the move turn
                                // send error packet
                                // Packet errorPacket = new Packet(Constants.SERVER_ERROR_PACKET_ID);
                                // errorPacket.addKey(Constants.ERROR, Constants.MOVE_TURN_USED);
                                // serverSender.sendPacketToClient(sender, errorPacket);
                                result = Constants.FAIL;
                            } else {
                                this.usedMoveTurn.add(sender);
                                this.sentQuestion = false;
                                this.moveToNextPlayer(false);
                                this.nTurns++;
                            }

                            Packet moveTurnResultPacket = new Packet(Constants.SERVER_MOVE_TURN_PACKET_ID);
                            moveTurnResultPacket.addKey(Constants.STATUS, result);
                            moveTurnResultPacket.addKey(Constants.CANDIDATE, this.clientSessions.get(curPlayerIdx).getUsername());

                            sendPacket2AllClients(moveTurnResultPacket);
                        } else if (packet.getPacketId() == Constants.CLIENT_TIMEOUT_PACKET_ID) {
                            Packet resultPacket = new Packet(Constants.SERVER_QUESTION_RESULT_PACKET_ID);

                            resultPacket.addKey(Constants.RESULT, Constants.TIME_OUT);
                            resultPacket.addKey(Constants.SOLUTION, this.curQuestion.getSolution());
                            resultPacket.addKey(Constants.CANDIDATE, this.clientSessions.get(curPlayerIdx).getUsername());

                            this.moveToNextPlayer(true);

                            nTurns++;
                            this.sentQuestion = false;
                            this.curQuestion = null;

                            sendPacket2AllClients(resultPacket);
                        }
                    }
                }
            }
        }

    }

    private Packet createQuestionPacket() {
        Packet questionPacket = new Packet(Constants.SERVER_QUESTION_PACKET_ID);
        questionPacket.addKey(Constants.QUESTION, curQuestion.getQuestion());
        questionPacket.addKey(Constants.ANSWER_A, curQuestion.getAnswers().get(Constants.ANSWER_A));
        questionPacket.addKey(Constants.ANSWER_B, curQuestion.getAnswers().get(Constants.ANSWER_B));
        questionPacket.addKey(Constants.ANSWER_C, curQuestion.getAnswers().get(Constants.ANSWER_C));
        questionPacket.addKey(Constants.ANSWER_D, curQuestion.getAnswers().get(Constants.ANSWER_D));
        questionPacket.addKey(Constants.CANDIDATE, this.clientSessions.get(curPlayerIdx).getUsername());
        questionPacket.addKey(Constants.CUR_QUESTION_NUMBER, String.valueOf(this.curQuestionNumber));

        if (usedMoveTurn.contains(this.clientSessions.get(curPlayerIdx))) {
            questionPacket.addKey(Constants.MOVE_TURN_USED, Constants.TRUE);
        } else {
            questionPacket.addKey(Constants.MOVE_TURN_USED, Constants.FALSE);
        }
        return questionPacket;
    }

    synchronized private void sendPacket2SingleClient(Packet packet, ClientSession sender) {
        new Thread() {
            public void run() {
                serverSender.sendPacketToClient(sender, packet);
            }
        }.start();
    }

    synchronized private void sendPacket2AllClients(Packet packet) {
        new Thread() {
            public void run() {
                serverSender.sendPacketToAllClients(clientSessions, packet);
            }
        }.start();
    }

    synchronized public void addClientSession(ClientSession clientSession) {
        if (this.gameState == Constants.WAITING_FOR_PLAYERS)
            this.clientSessions.add(clientSession);
    }

    synchronized private boolean turnOnTimer(){
        int cnt = 0;
        for (ClientSession clientSession : this.clientSessions) {
            if (clientSession.getUsername() != null) {
                cnt += 1;
                if (cnt == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    synchronized public void removeClientSessionWithID(int id){
        for(int i = 0; i < this.clientSessions.size();i++){
            if (this.clientSessions.get(i).getID() == id){
                this.clientSessions.remove(i);
                break;
            }
        }
    }

    synchronized public void printUsername(){
        if (this.clientSessions.size() == 0) {
            System.out.println("There is no connected player at game core thread");
            return;
        }
        System.out.print("Current players at game core thread: ");
        for (ClientSession cs : this.clientSessions) {
            System.out.print(cs.getID() + "-" + cs.getUsername() + ", ");
        }
        System.out.println();
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.execute(null, null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
