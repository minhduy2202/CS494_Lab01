import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


class ImagePanel extends JPanel {
    private Image backgroundImage;

    public ImagePanel(String fileName) {
        try {
            backgroundImage = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}



public class UIQuestion extends JFrame {

    private static final int TIMER_DURATION = 30; // in seconds

    private JLabel questionIndexLabel, questionContentLabel, answerALabel, answerBLabel, answerCLabel, answerDLabel;
    private JLabel timerLabel;
    private JButton answerAButton, answerBButton, answerCButton, answerDButton;
    private Timer timer;
    private int timeRemaining;
    private ImagePanel mainPanel;

    private JPanel answerPanel, questionIndexPanel, questionContentPanel, timerPanel,
            timerWrapper, contentAndAnswerPanel;

    public UIQuestion() {
        // Set up the frame
        setTitle("Who Wants to Be a Millionaire - Question");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setResizable(false);
        setLocationRelativeTo(null);

        mainPanel = new ImagePanel("Backgrounds/Background00.png");
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Set up the question index label
        questionIndexLabel = new JLabel("Question 1 / 30");
        questionIndexLabel.setFont(new Font("Arial", Font.BOLD, 24));
        questionIndexLabel.setForeground(Color.WHITE);
        questionIndexLabel.setHorizontalAlignment(JLabel.LEFT);

        questionIndexPanel = new JPanel();
        questionIndexPanel.setOpaque(true);
        questionIndexPanel.setBackground(Color.BLACK);
        questionIndexPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        questionIndexLabel.setHorizontalAlignment(JLabel.CENTER);
        questionIndexPanel.add(questionIndexLabel);

        // Add question index panel to the main panel
        // mainPanel.add(questionIndexPanel, BorderLayout.CENTER);

        JPanel questionIndexWrapper = new JPanel();
        questionIndexWrapper.setOpaque(false);
        questionIndexWrapper.setLayout(new GridBagLayout());
        GridBagConstraints questionIndexGbc = new GridBagConstraints();
        questionIndexWrapper.setMaximumSize(new Dimension(1920, 200));
        questionIndexGbc.gridx = 0;
        questionIndexGbc.gridy = 0;
        questionIndexGbc.anchor = GridBagConstraints.NORTHWEST;
        questionIndexWrapper.add(questionIndexPanel, questionIndexGbc);
        mainPanel.add(questionIndexWrapper, BorderLayout.WEST);

        // Set up the question content label
        questionContentLabel = new JLabel("What is the capital city of France?");
        questionContentLabel.setFont(new Font("Arial", Font.BOLD, 32));
        questionContentLabel.setForeground(Color.WHITE);
        questionContentLabel.setHorizontalAlignment(JLabel.CENTER);
        questionContentLabel.setVerticalAlignment(JLabel.CENTER);

        questionContentPanel = new JPanel();
        questionContentPanel.setOpaque(true);
        questionContentPanel.setBackground(Color.BLACK);
        questionContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        questionContentPanel.setLayout(new GridBagLayout());
        questionContentPanel.add(questionContentLabel);
        questionContentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); // Set the preferred size of the question content panel

        JPanel questionContentWrapper = new JPanel();
        questionContentWrapper.setBackground(Color.black);
        questionContentWrapper.setOpaque(true);
        questionContentWrapper.setLayout(new GridBagLayout());
        questionContentWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        questionContentWrapper.add(questionContentPanel, gbc);
        // mainPanel.add(questionContentWrapper, BorderLayout.CENTER);

        // Set up the answer buttons
        answerALabel = new JLabel("A. Paris");
        answerALabel.setFont(new Font("Arial", Font.PLAIN, 24));
        answerALabel.setHorizontalAlignment(JLabel.LEFT);

        answerBLabel = new JLabel("B. Rome");
        answerBLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        answerBLabel.setHorizontalAlignment(JLabel.LEFT);

        answerCLabel = new JLabel("C. London");
        answerCLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        answerCLabel.setHorizontalAlignment(JLabel.LEFT);

        answerDLabel = new JLabel("D. Madrid");
        answerDLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        answerDLabel.setHorizontalAlignment(JLabel.LEFT);

        answerAButton = new JButton();
        answerAButton.add(answerALabel);
        answerAButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerAButton.setPreferredSize(new Dimension(480, 80));

        answerBButton = new JButton();
        answerBButton.add(answerBLabel);
        answerBButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerBButton.setPreferredSize(new Dimension(480, 80));

        answerCButton = new JButton();
        answerCButton.add(answerCLabel);
        answerCButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerCButton.setPreferredSize(new Dimension(480, 80));

        answerDButton = new JButton();
        answerDButton.add(answerDLabel);
        answerDButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerDButton.setPreferredSize(new Dimension(480, 80));


        answerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        answerPanel.add(answerAButton);
        answerPanel.add(answerBButton);
        answerPanel.add(answerCButton);
        answerPanel.add(answerDButton);
        answerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        answerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));


        // Create a new panel to hold the question content and answer buttons
        contentAndAnswerPanel = new JPanel();
        contentAndAnswerPanel.setOpaque(true);
        questionContentPanel.setBackground(Color.BLACK);
        contentAndAnswerPanel.setLayout(new BoxLayout(contentAndAnswerPanel, BoxLayout.Y_AXIS));
        contentAndAnswerPanel.add(Box.createVerticalGlue()); // Add vertical glue to center the question content and answer panel vertically
        contentAndAnswerPanel.add(questionContentWrapper);
        // contentAndAnswerPanel.add(Box.createVerticalStrut(20)); // Add vertical strut to create a 20-pixel gap between the question content and answer panel
        contentAndAnswerPanel.add(answerPanel);
        contentAndAnswerPanel.add(Box.createVerticalGlue()); // Add vertical glue to center the question content and answer panel vertically

        // Add the content and answer panel to the main panel
        mainPanel.add(contentAndAnswerPanel, BorderLayout.SOUTH);



        // Add timer label
        timerLabel = new JLabel();
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerLabel.setForeground(Color.RED);
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        timerPanel = new JPanel(new BorderLayout());
        timerPanel.setOpaque(true);
        timerPanel.setBackground(Color.BLACK);
        timerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        timerPanel.add(timerLabel, BorderLayout.CENTER);

        timerWrapper = new JPanel();
        timerWrapper.setOpaque(false);
        timerWrapper.setLayout(new GridBagLayout());
        GridBagConstraints timerGbc = new GridBagConstraints();
        timerGbc.gridx = 0;
        timerGbc.gridy = 0;
        timerGbc.gridwidth = 2; // Set the gridwidth to 2 to span two columns
        timerGbc.anchor = GridBagConstraints.NORTHEAST;
        timerWrapper.add(timerPanel, timerGbc);
        mainPanel.add(timerWrapper, BorderLayout.NORTH);

        // Initialize the timer
        timeRemaining = TIMER_DURATION;
        timerLabel.setText("Time: " + timeRemaining + "s");
        timer = new Timer(1000, new TimerListener());
        timer.start();
    }



        private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            timeRemaining--;
            timerLabel.setText("Time: " + timeRemaining + "s");

            if (timeRemaining <= 0) {
                timer.stop();
                // Add your logic here for when the timer runs out
            }
        }
    }


    public static void main(String[] args) {
        UIQuestion UIQuestion = new UIQuestion();
        UIQuestion.setVisible(true);
    }
}
