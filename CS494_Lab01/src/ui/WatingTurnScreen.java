package ui;

import client.ClientNetwork;
import client.Player;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class WatingTurnScreen extends JFrame implements PropertyChangeListener {

    Player player = null;

    ClientNetwork clientNetwork;

    private JLabel loadingLabel;
    private JProgressBar progressBar;

    public WatingTurnScreen(Player player, ClientNetwork clientNetwork) {
        this.player = player;
        this.clientNetwork = clientNetwork;
        // Set up the frame
        setTitle("Loading");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setResizable(true);
        setLocationRelativeTo(null);

        // Set up the main panel
        JPanel mainPanel = new JPanel();
        mainPanel = new ImagePanel("/Backgrounds/Background01.png");
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        // Set up the loading label
        loadingLabel = new JLabel("Player: " + player.getUsername() + " Waiting for your turn...");
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 40));
        loadingLabel.setForeground(Color.RED);
        loadingLabel.setHorizontalAlignment(JLabel.CENTER);
        loadingLabel.setMinimumSize(new Dimension(1920, 40));
        mainPanel.add(loadingLabel, BorderLayout.CENTER);

        // Tao 1 bien integer de resize lai (Dua theo size hien tai, tinh li le cac thu)
        // Set up the progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setForeground(Color.BLUE);
        progressBar.setPreferredSize(new Dimension(1920, 60));

        // Create a new panel to hold the progress bar with a 50-pixel padding at the bottom
        JPanel progressBarPanel = new JPanel(new BorderLayout());
        progressBarPanel.add(progressBar, BorderLayout.SOUTH);

        mainPanel.add(progressBarPanel, BorderLayout.SOUTH); // Add the progressBarPanel to the mainPanel
    }

    public void closeSplashScreen() {
        // Close the splash screen and dispose of its resources
        setVisible(false);
        dispose();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Go to waiting handle");
        this.setPlayer((Player) evt.getNewValue());
        if (player.getReceivePacketID() == Constants.SERVER_LOSE_PACKET_ID){
            System.out.println("You loose waiting turn");
            clientNetwork.getClientHandlerTmp().removePropertyChangeListener(this);
            this.closeSplashScreen();
            YouLoseUI youLoseUI = new YouLoseUI(player);
            youLoseUI.setVisible(true);
        } else if (player.getReceivePacketID() == Constants.SERVER_QUESTION_PACKET_ID
            && Objects.equals(player.getCurCandidate(), player.getUsername())){
            System.out.println("Go to change to UI question");
            UIQuestion uiQuestion = new UIQuestion(player, clientNetwork);
            clientNetwork.getClientHandlerTmp().addPropertyChangeListener(uiQuestion);
            clientNetwork.getClientHandlerTmp().removePropertyChangeListener(this);
            this.closeSplashScreen();
            uiQuestion.setVisible(true);
        } else if (player.getReceivePacketID() == Constants.SERVER_WIN_PACKET_ID){
            clientNetwork.getClientHandlerTmp().removePropertyChangeListener(this);
            this.closeSplashScreen();
            YouWinUI youWinUI = new YouWinUI(player);
            youWinUI.setVisible(true);
        }
    }

    public void setPlayer(Player player){
        this.player = player;
    }
}
