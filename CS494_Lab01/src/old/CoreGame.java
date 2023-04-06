package old;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class CoreGame {
    public static void main(String[] args) throws IOException {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        Integer nUsers = -1;
        System.out.print("Enter number of user: ");
        try {
            nUsers = Integer.parseInt(stdIn.readLine());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> userNames = new ArrayList<>();
        HashSet<String> checkExists = new HashSet<>();
        for (int i = 0; i < nUsers; i++) {
            String userName;
            do {
                System.out.print("Enter user name " + (i + 1) + ": ");
                userName = stdIn.readLine();
            } while (checkExists.contains(userName));
            userNames.add(userName);
            checkExists.add(userName);
        }

        List<Question> questions = Loader.loadCSV("src/questions.csv");
        RandomSet<Question> questionSet = new RandomSet<>();
        for (Question question : questions) {
            questionSet.add(question);
        }

        HashSet<String> usedMoveTurn = new HashSet<>();
        ArrayList<String> currentPlayers = new ArrayList<>(userNames);
        Integer currentTurnNo = 0;
        Question currentQuestion = null;
        while (currentPlayers.size() >= 1 && questionSet.size() > 0) {
            Integer playerIdx = currentTurnNo % currentPlayers.size();
            String currentPlayerName = currentPlayers.get(playerIdx);
            System.out.println("Turn " + currentTurnNo + " of player " + currentPlayerName);

            if (currentQuestion == null) {
                currentQuestion = questionSet.pollRandom(new Random());
            }

            System.out.println(currentQuestion.getQuestion());
            System.out.println("a. " + currentQuestion.getAnswers().get("a"));
            System.out.println("b. " + currentQuestion.getAnswers().get("b"));
            System.out.println("c. " + currentQuestion.getAnswers().get("c"));
            System.out.println("d. " + currentQuestion.getAnswers().get("d"));

            String answer = null;
            Boolean useMoveTurn = false;
            if (usedMoveTurn.contains(currentPlayers.get(playerIdx))) {
                System.out.println("You have used your move turn. You need to answer the question");
                System.out.print("Enter your answer: ");
                answer = stdIn.readLine();
            } else {
                System.out.println("Do you want to use your move turn to skip this question? (Y/n)");
                String moveTurn = stdIn.readLine();
                if (moveTurn.toLowerCase().equals("y") || moveTurn.length() == 0) {
                    usedMoveTurn.add(currentPlayers.get(playerIdx));
                    System.out.println("You have used your move turn");
                    useMoveTurn = true;
                } else {
                    System.out.print("Enter your answer: ");
                    answer = stdIn.readLine();
                }
            }

            if (useMoveTurn) {
                usedMoveTurn.add(currentPlayerName);
            } else {
                if (currentPlayers.size() == 1) {
                    break;
                }

                if (answer.toLowerCase().equals(currentQuestion.getSolution())) {
                    System.out.println("Correct answer");
                } else {
                    System.out.println("Wrong answer");
                    currentPlayers.remove(currentPlayerName);
                }
                currentQuestion = null;
            }

            currentTurnNo++;
            System.out.println("###################\n");

        }

        if (currentPlayers.size() == 1) {
            System.out.println("Player " + currentPlayers.get(0) + " wins");
        } else {
            System.out.println("Draw");
        }

    }
}
