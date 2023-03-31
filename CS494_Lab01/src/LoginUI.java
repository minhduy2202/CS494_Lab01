import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class LoginUI extends JFrame {

    private JLabel titleLabel, numPlayersLabel, logoLabel;
    private JTextField numPlayersTextField;
    private JButton startButton;

    private ArrayList<String> playerNames = new ArrayList<>();
    private String[] blockList = {"admin", "root", "superuser"};

    public LoginUI() {
        // Set up the frame
        setTitle("Who Wants to Be a Millionaire - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(null);

        // Set up the logo label
        logoLabel = new JLabel(new ImageIcon("logo.png"));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(logoLabel, BorderLayout.NORTH);

        // Set up the title label
        titleLabel = new JLabel("Who Wants to Be a Millionaire");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.CENTER);

        // Set up the number of players label and text field
        JPanel numPlayersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        numPlayersLabel = new JLabel("Enter number of players:");
        numPlayersLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        numPlayersPanel.add(numPlayersLabel);

        numPlayersTextField = new JTextField(20);
        numPlayersTextField.setFont(new Font("Arial", Font.PLAIN, 24));
        numPlayersPanel.add(numPlayersTextField);

        add(numPlayersPanel, BorderLayout.CENTER);

        // Set up the start button
        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.PLAIN, 24));
        startButton.setBackground(new Color(0, 153, 51));
        startButton.setForeground(Color.WHITE);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> {
            try {
                int numPlayers = Integer.parseInt(numPlayersTextField.getText());
                if (numPlayers <= 0) {
                    JOptionPane.showMessageDialog(this, "Number of players must be greater than 0.");
                    numPlayersTextField.setText("");
                } else {
                    for (int i = 0; i < numPlayers; i++) {
                        String playerName = JOptionPane.showInputDialog(this, "Enter player " + (i+1) + "'s name:");
                        if (playerName == null || playerName.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Input name invalid, please try again.");
                            i--;
                        } else if (playerNames.contains(playerName)) {
                            JOptionPane.showMessageDialog(this, "Name already exists, please try again.");
                            i--;
                        } else if (isNameBlocked(playerName)) {
                            JOptionPane.showMessageDialog(this, "Invalid name, please try again.");
                            i--;
                        } else if (playerName.equalsIgnoreCase("exit")) {
                            // User wants to exit without entering all player names
                            break;
                        } else {
                            playerNames.add(playerName);
                        }
                    }
                    // TODO: Proceed to the main game UI with playerNames list
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Number of players must be an integer.");
                numPlayersTextField.setText("");
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.add(startButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buttonPanel, BorderLayout.SOUTH);

    }
    private boolean isNameBlocked(String name) {
        for (String blockedName : blockList) {
            if (blockedName.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        LoginUI loginUI = new LoginUI();
        loginUI.setVisible(true);
    }
}
