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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

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

    private boolean isRunning = true;

    private ServerNetwork network = null;

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

        this.network = new ServerNetwork(selector, serverSocketChannel, this);
        new Thread(this.network).start();
    }

    public GameCore(LinkedList<ClientSession> sessions) {
        this.clientSessions = new LinkedList<>();
        this.clientSessions.addAll(sessions);
        this.questionSet = new RandomSet<>(Loader.loadCSV("src/questions.csv"));
    }

    synchronized public void moveToNextPlayer(boolean removeCurrentPlayer) {
        if (removeCurrentPlayer) {
            this.clientSessions.remove(this.curPlayerIdx);
        } else {
//            this.curPlayerIdx++;
        }
        this.curPlayerIdx %= clientSessions.size();
    }

    synchronized public void execute(Packet packet, ClientSession sender) {

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
                    responsePacket.addKey(Constants.PLAYER_ORDER_NUMBER, String.valueOf(sender.getID() + 1));


                    sendPacket2SingleClient(responsePacket, sender);

                    // check constraint that if game has enough players
                    if (this.clientSessions.size() == Constants.MAX_PLAYER) {
                        this.gameState = Constants.GAME_READY;
                    }
                }
            }

            case Constants.GAME_READY -> {
                // send start game packet to all client sessions
                Collections.shuffle(this.clientSessions);
                for (int i = 0; i < this.clientSessions.size(); i++) {
                    Packet startGamePacket = new Packet(Constants.SERVER_START_GAME_PACKET_ID);
                    startGamePacket.addKey(Constants.NUMBER_OF_PLAYERS, String.valueOf(this.clientSessions.size()));
                    startGamePacket.addKey(Constants.NUMBER_OF_QUESTIONS, String.valueOf(this.questionSet.size()));
                    startGamePacket.addKey(Constants.PLAYER_ORDER_NUMBER, String.valueOf(i + 1));
                    sendPacket2SingleClient(startGamePacket, this.clientSessions.get(i));
                }
                this.gameState = Constants.GAME_STARTED;

            }

            case Constants.GAME_STARTED -> {
                // the last player in the queue
                if (this.clientSessions.size() == 1){
                    Packet winningPacket = new Packet(Constants.SERVER_WIN_PACKET_ID);
                    sendPacket2SingleClient(winningPacket, this.clientSessions.get(0));
                    this.isRunning = false;
//                    this.network.
                    return;
                }

                if (!sentQuestion) {
                    // send question to the current player
                    if (questionSet.size() == 0) {
                        Packet winningPacket = new Packet(Constants.SERVER_WIN_PACKET_ID);
                        sendPacket2SingleClient(winningPacket, this.clientSessions.get(this.curPlayerIdx));
                    }

                    if (this.curQuestion == null) {
                        this.curQuestion = this.questionSet.pollRandom(new Random());
                    }

                    // create new packet to send to the current client turn
                    Packet questionPacket = createQuestionPacket();

                    // serverSender.sendPacketToClient(this.clientSessions.get(curPlayerIdx),
                    // questionPacket);
                    sendPacket2SingleClient(questionPacket, this.clientSessions.get(curPlayerIdx));
                    sentQuestion = true;
                } else {
                    // handle received packet
                    // check if the packet is from the current player
                    if (sender.getUsername().equals(this.clientSessions.get(curPlayerIdx).getUsername())) {
                        // check if the packet is an answer packet
                        if (packet.getPacketId() == Constants.CLIENT_ANSWER_PACKET_ID) {

                            if (questionSet.size() == 0){
                                Packet winningPacket = new Packet(Constants.SERVER_WIN_PACKET_ID);
                                sendPacket2SingleClient(winningPacket, sender);

                                Packet losingPacket = new Packet(Constants.SERVER_LOSE_PACKET_ID);
                                losingPacket.addKey(Constants.USERNAME, sender.getUsername());
                                for (ClientSession cs: this.clientSessions){
                                    if (cs.getID() == sender.getID())
                                        continue;
                                    sendPacket2SingleClient(losingPacket, cs);
                                }
                                this.isRunning = false;
//                                this.network.stop();
                                return;
                            }

                            String answer = packet.getKey(Constants.ANSWER);
                            Packet resultPacket = new Packet(Constants.SERVER_QUESTION_RESULT_PACKET_ID);

                            String result = null;
                            if (answer.equals(this.curQuestion.getSolution())) {
                                result = Constants.CORRECT;
                                resultPacket.addKey(Constants.RESULT, Constants.SUCCESS);
                            } else {
                                result = Constants.WRONG;
                                resultPacket.addKey(Constants.RESULT, Constants.WRONG);
                                resultPacket.addKey(Constants.SOLUTION, this.curQuestion.getSolution());
                            }

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

//                            sendPacket2AllClients(resultPacket);
                            sendPacket2SingleClient(resultPacket, sender);
                            if (result.equals(Constants.WRONG)) {
                                Packet losingPacket = new Packet(Constants.SERVER_LOSE_PACKET_ID);
                                sendPacket2SingleClient(losingPacket, sender);
                            }

                        } else if (packet.getPacketId() == Constants.CLIENT_MOVE_TURN_PACKET_ID) {
                            // this is a move turn packet
                            if (this.usedMoveTurn.contains(sender)) {
                                // this player has already used the move turn
                                // send error packet
                                // Packet errorPacket = new Packet(Constants.SERVER_ERROR_PACKET_ID);
                                // errorPacket.addKey(Constants.ERROR, Constants.MOVE_TURN_USED);
                                // serverSender.sendPacketToClient(sender, errorPacket);
                            } else {
                                this.usedMoveTurn.add(sender);
                                this.sentQuestion = false;
                                this.moveToNextPlayer(false);
                                this.nTurns++;
                            }

                            Packet moveTurnResultPacket = new Packet(Constants.SERVER_MOVE_TURN_PACKET_ID);
                            moveTurnResultPacket.addKey(Constants.STATUS, result);
                            moveTurnResultPacket.addKey(Constants.CANDIDATE, this.clientSessions.get(curPlayerIdx).getUsername());

//                            sendPacket2AllClients(moveTurnResultPacket);
                            sendPacket2SingleClient(moveTurnResultPacket, sender);
                        } else if (packet.getPacketId() == Constants.CLIENT_TIMEOUT_PACKET_ID) {
                            Packet resultPacket = new Packet(Constants.SERVER_QUESTION_RESULT_PACKET_ID);

                            resultPacket.addKey(Constants.RESULT, Constants.TIME_OUT);
                            resultPacket.addKey(Constants.SOLUTION, this.curQuestion.getSolution());
                            resultPacket.addKey(Constants.CANDIDATE, this.clientSessions.get(curPlayerIdx).getUsername());

                            this.moveToNextPlayer(true);

                            nTurns++;
                            this.sentQuestion = false;
                            this.curQuestion = null;

//                            sendPacket2AllClients(resultPacket);
                            sendPacket2SingleClient(resultPacket, sender);
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

        if (usedMoveTurn.contains(this.clientSessions.get(curPlayerIdx))) {
            questionPacket.addKey(Constants.MOVE_TURN_USED, Constants.TRUE);
        } else {
            questionPacket.addKey(Constants.MOVE_TURN_USED, Constants.FALSE);
        }
        return questionPacket;
    }

    private void sendPacket2SingleClient(Packet packet, ClientSession sender) {
        new Thread() {
            public void run() {
                serverSender.sendPacketToClient(sender, packet);
            }
        }.start();
    }

    private void sendPacket2AllClients(Packet packet) {
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

    @Override
    public void run() {
        while (isRunning) {
            try {
                this.execute(null, null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
