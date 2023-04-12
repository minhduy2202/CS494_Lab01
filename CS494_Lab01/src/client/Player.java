package client;

import utils.Question;

import java.beans.PropertyChangeListener;
import java.util.Objects;

public class Player {

    String username = null;

    String loginStatus = null;

    int numberOfPlayers = 0;
    int numberOfQuestions = 0;
    int playerOrder = 0;

    Question question = null;
    String curCandidate = null;
    int curQuestionIdx = -1;

    int receivePacketID = -1;

    String answer = null;
    String solution = null;

    String result = null;

    String moveTurnStatus = null;

    boolean isUsedSkip = false;

    public Player(){
        this.username = "tmp";
    }

    public Player(Player player){
        this.username = player.username;
        this.loginStatus = player.loginStatus;
        this.numberOfPlayers = player.numberOfPlayers;
        this.numberOfQuestions = player.numberOfQuestions;
        this.playerOrder = player.playerOrder;
        this.question = player.question;
        this.curCandidate = player.curCandidate;
        this.curQuestionIdx = player.curQuestionIdx;
        this.answer = player.answer;
        this.solution = player.solution;
        this.result = player.result;
        this.receivePacketID = player.receivePacketID;
        this.moveTurnStatus = player.moveTurnStatus;
        this.isUsedSkip = player.isUsedSkip;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }

        Player p = (Player) o;
        if (!Objects.equals(username, p.username) || !Objects.equals(loginStatus, p.loginStatus) || numberOfPlayers != p.numberOfPlayers
                || curQuestionIdx != p.curQuestionIdx || receivePacketID != p.receivePacketID || !Objects.equals(curCandidate, p.curCandidate)){
            return false;
        }
        return true;
    }


    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public int getPlayerOrder() {
        return playerOrder;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public void setPlayerOrder(int playerOrder) {
        this.playerOrder = playerOrder;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Question getQuestion() {
        return question;
    }

    public String getCurCandidate() {
        return curCandidate;
    }

    public int getCurQuestionIdx() {
        return curQuestionIdx;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setCurCandidate(String curCandidate) {
        this.curCandidate = curCandidate;
    }

    public void setCurQuestionIdx(int curQuestionIdx) {
        this.curQuestionIdx = curQuestionIdx;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAnswer() {
        return answer;
    }

    public String getSolution() {
        return solution;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public int getReceivePacketID() {
        return receivePacketID;
    }

    public void setReceivePacketID(int receivePacketID) {
        this.receivePacketID = receivePacketID;
    }

    public String getMoveTurnStatus() {
        return moveTurnStatus;
    }

    public void setMoveTurnStatus(String moveTurnStatus) {
        this.moveTurnStatus = moveTurnStatus;
    }

    public boolean getUsedSkip() {
        return isUsedSkip;
    }

    public void setUsedSkip(boolean usedSkip) {
        isUsedSkip = usedSkip;
    }
}
