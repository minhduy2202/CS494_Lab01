package ui;

import client.Player;

import javax.swing.*;
import java.awt.*;

public class YouWinUI extends JFrame {

    private JLabel winLabel;
    private ImagePanel mainPanel;

    public YouWinUI(Player player) {
        // Set up the frame
        setTitle("Win Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setResizable(true);
        setLocationRelativeTo(null);

        mainPanel = new ImagePanel("/Backgrounds/Background01.png");
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Set up the win label
        winLabel = new JLabel("Congratulations " + player.getUsername() + "! You have won the game!");
        winLabel.setFont(new Font("Arial", Font.BOLD, 40));
        winLabel.setHorizontalAlignment(JLabel.CENTER);
        winLabel.setForeground(Color.GREEN);
        mainPanel.add(winLabel, BorderLayout.CENTER);
    }
}
