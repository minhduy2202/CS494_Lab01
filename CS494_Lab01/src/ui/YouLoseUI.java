package ui;

import client.Player;

import javax.swing.*;
import java.awt.*;

public class YouLoseUI extends JFrame {

    private JLabel LoseLabel;
    private ImagePanel mainPanel;

    public YouLoseUI(Player player) {
        // Set up the frame
        setTitle("Lose Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setResizable(true);
        setLocationRelativeTo(null);

        // Set up the main panel
        mainPanel = new ImagePanel("/Backgrounds/Background01.png");
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        // Set up the win label
        LoseLabel = new JLabel("Sorry " + player.getUsername() + ", the answer is " + player.getSolution() + ", you lose!");
        LoseLabel.setFont(new Font("Arial", Font.BOLD, 40));
        LoseLabel.setHorizontalAlignment(JLabel.CENTER);
        LoseLabel.setForeground(Color.RED);
        mainPanel.add(LoseLabel, BorderLayout.CENTER);
    }
}
