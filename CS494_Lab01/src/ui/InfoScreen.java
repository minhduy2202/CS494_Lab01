package ui;

import client.ClientNetwork;
import client.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import javax.swing.*;

public class InfoScreen extends JFrame implements PropertyChangeListener {

    Player player;

    ClientNetwork clientNetwork;
    private JLabel numberOfPlayersLabel, orderLabel, numberOfQuestionsLabel;
    private JButton startGameButton;
    private ImagePanel mainPanel;

    public InfoScreen(Player player, ClientNetwork clientNetwork) {
        this.clientNetwork = clientNetwork;
        this.player = player;
        // Set up the frame
        setTitle("Waiting");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setResizable(true);
        setLocationRelativeTo(null);

        // Set up the main panel
        mainPanel = new ImagePanel("/Backgrounds/Background01.png");
        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();

        // Set up the number of players label
        numberOfPlayersLabel = new JLabel("Number of Players: " + player.getNumberOfPlayers());
        numberOfPlayersLabel.setFont(new Font("Arial", Font.BOLD, 40));
        numberOfPlayersLabel.setForeground(Color.RED);
        numberOfPlayersLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(numberOfPlayersLabel, gbc);

        // Set up the order label
        orderLabel = new JLabel("Order: " + player.getPlayerOrder());
        orderLabel.setFont(new Font("Arial", Font.BOLD, 40));
        orderLabel.setForeground(Color.RED);
        orderLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(orderLabel, gbc);

        // Set up the number of questions label
        numberOfQuestionsLabel = new JLabel("Number of Questions: " + player.getNumberOfQuestions());
        numberOfQuestionsLabel.setFont(new Font("Arial", Font.BOLD, 40));
        numberOfQuestionsLabel.setForeground(Color.RED);
        numberOfQuestionsLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(numberOfQuestionsLabel, gbc);

        // Set up the game start button
        ImageIcon startButtonIcon = new ImageIcon(getClass().getResource("/Backgrounds/startButton.png"));

        // Scale the image
        int fixedWidth = 400;
        int fixedHeight = 200;
        Image scaledImage = startButtonIcon.getImage().getScaledInstance(fixedWidth, fixedHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

//        startGameButton = new JButton(scaledIcon);
//        startGameButton.setBorder(null);
//        startGameButton.setContentAreaFilled(false);
//        startGameButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                closeInfoScreen();
//                UIQuestion uiQuestion = new UIQuestion();
//                uiQuestion.setVisible(true);
//            }
//        });
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        mainPanel.add(startGameButton, gbc);

    }

    public void closeInfoScreen() {
        // Close the splash screen and dispose of its resources
        setVisible(false);
        dispose();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.setPlayer((Player) evt.getNewValue());
        System.out.println(player.getCurQuestionIdx());
        if (Objects.equals(player.getCurCandidate(), player.getUsername())){
            System.out.println("Go to question UI");
            UIQuestion uiQuestion = new UIQuestion(player, clientNetwork);
            clientNetwork.getClientHandlerTmp().addPropertyChangeListener(uiQuestion);
            clientNetwork.getClientHandlerTmp().removePropertyChangeListener(this);
            this.closeInfoScreen();
            uiQuestion.setVisible(true);
        } else {
            WatingTurnScreen watingTurnScreen = new WatingTurnScreen(player, clientNetwork);
            clientNetwork.getClientHandlerTmp().addPropertyChangeListener(watingTurnScreen);
            clientNetwork.getClientHandlerTmp().removePropertyChangeListener(this);
            this.closeInfoScreen();
            watingTurnScreen.setVisible(true);
        }
    }

    public void setPlayer(Player player){
        this.player = player;
    }
}
