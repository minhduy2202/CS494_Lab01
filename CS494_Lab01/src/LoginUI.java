import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame {
    private ImagePanel mainPanel;

    public LoginUI() {
        setTitle("Who Wants to Be a Millionaire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setResizable(false);
        setLocationRelativeTo(null);

        mainPanel = new ImagePanel("Backgrounds/Background00.png");
        mainPanel.setLayout(new GridBagLayout()); // Use GridBagLayout here
        setContentPane(mainPanel);

        // Tạo một JPanel mới để chứa các thành phần nhập liệu và nút submit
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setOpaque(false);
        inputPanel.setPreferredSize(new Dimension(960, 150));

        // Tạo một ô nhập liệu (JTextField) để người dùng nhập tên
        HintTextField playerNameField = new HintTextField("Enter Player's Name");
        playerNameField.setFont(new Font("Arial", Font.PLAIN, 32));
        playerNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerNameField.setPreferredSize(new Dimension(960, 75));
        inputPanel.add(playerNameField);

        // Thêm một khoảng trống giữa ô nhập liệu và nút submit
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Tạo một nút submit (JButton)
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 32));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(submitButton);

        // Đặt inputPanel ở giữa và cách điểm dưới màn hình 30 pixel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(0, 0, 30, 0); // Add 30 pixels of padding at the bottom
        mainPanel.add(inputPanel, gbc);

// Thêm sự kiện cho nút submit
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = playerNameField.getText();
                if (!playerName.isEmpty()) {
                    // Xử lý tên người chơi tại đây, chẳng hạn như chuyển sang màn hình chính của trò chơi
                    System.out.println("Player name: " + playerName);

                    // Dispose of the LoginUI frame
                    LoginUI.this.dispose();

                    // Show the splash screen
                    SplashScreen splashScreen = new SplashScreen();
                    splashScreen.setVisible(true);

                    // Set up a timer to close the splash screen after 10 seconds and open UIQuestion
                    Timer timer = new Timer(10000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            splashScreen.closeSplashScreen();
                            UIQuestion uiQuestion = new UIQuestion();
                            uiQuestion.setVisible(true);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();

                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a name", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginUI();
            }
        });
    }
}
