package utils;

public class Constants {
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final int MAX_PLAYER = 10;

    public static final int BUFFER_SIZE = 4096;
    public static final int THREAD_POOL_SIZE = 10;
    public static String SERVER_IP = "127.0.0.1";
    public static int PORT = 8080;

    // server game states constants
    public static final int WAITING_FOR_PLAYERS = 0;
    public static final int GAME_READY = 1;
    public static final int GAME_STARTED = 2;
    public static final int GAME_ENDED = 3;
    public static final int SPLASH_GAME_START = 4;
    public static final int GIVE_ANSWER = 5;

    // server packet id to send to client
    public static final int SERVER_LOGIN_PACKET_ID = 100;
    public static final int SERVER_START_GAME_PACKET_ID = 101;
    public static final int SERVER_QUESTION_PACKET_ID = 102;
    public static final int SERVER_QUESTION_RESULT_PACKET_ID = 103;
    public static final int SERVER_MOVE_TURN_PACKET_ID = 104;
    public static final int SERVER_WIN_PACKET_ID = 105;
    public static final int SERVER_LOSE_PACKET_ID = 106;

    // server attribute in packet
    public static final String PLAYER_ORDER_NUMBER = "order_number";
    public static final String NUMBER_OF_PLAYERS = "n_players";
    public static final String NUMBER_OF_QUESTIONS = "n_questions";

    // client game states constants
    public static final int LOG_IN = 0;
    public static final int WAITING_GAME_START = 1;


    // client packet id to send to server
    public static final int CLIENT_LOGIN_PACKET_ID = 200;
    public static final int CLIENT_ANSWER_PACKET_ID = 201;
    public static final int CLIENT_MOVE_TURN_PACKET_ID = 202;

    // status constants
    public static final String STATUS = "status";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    // question constants
    public static final String QUESTION = "question";
    public static final String ANSWER_A = "a";
    public static final String ANSWER_B = "b";
    public static final String ANSWER_C = "c";
    public static final String ANSWER_D = "d";
    public static final String SOLUTION = "solution";
    public static final String ANSWER = "answer";
    public static final String MOVE_TURN_USED = "move_turn_used";
    public static final String CANDIDATE = "candidate";

    // question result constants
    public static final String RESULT = "result";
    public static final String CORRECT = "correct";
    public static final String WRONG = "wrong";

    // packet key
    public static final String USERNAME = "username";
}
