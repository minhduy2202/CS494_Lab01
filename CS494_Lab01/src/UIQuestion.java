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
            backgroundImage = ImageIO.read(getClass().getResourceAsStream(fileName));
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

class Question1 {
    private String question;
    private String[] answers;
    private int correctAnswerIndex;

    public Question1(String question, String[] answers, int correctAnswerIndex) {
        this.question = question;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}

public class UIQuestion extends JFrame {

    private static final int TIMER_DURATION = 30; // in seconds
    private static final int TOTAL_QUESTIONS = 30;
    private static final int[] PRIZES = {
            100, 150, 200, 250, 300, 400, 500, 600, 700, 800, 900, 1000, 1500, 2000, 2500,
            4000, 5000, 6000, 7000, 8000, 10000, 14000, 16000, 32000, 64000, 100000, 125000, 250000, 500000, 1000000
    };
    private int currentQuestionIndex = 0;
    private int prizeWon = 0;

    private Question1[] questions = {
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of France?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0)
    };

    private JLabel questionIndexLabel, questionContentLabel, answerALabel, answerBLabel, answerCLabel, answerDLabel;
    private JLabel timerLabel, prizeLabel;
    private JButton answerAButton, answerBButton, answerCButton, answerDButton;
    private Timer timer;
    private int timeRemaining;
    private ImagePanel mainPanel;

    private JPanel answerPanel, questionIndexPanel, questionContentPanel, timerPanel,
            timerWrapper, contentAndAnswerPanel, prizePanel;

    public UIQuestion() {
        // Initialize the JLabels
        questionContentLabel = new JLabel();
        answerALabel = new JLabel();
        answerBLabel = new JLabel();
        answerCLabel = new JLabel();
        answerDLabel = new JLabel();
        timerLabel = new JLabel(); // Initialize timerLabel



        // Set up the frame
        setTitle("Who Wants to Be a Millionaire - Question");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setResizable(false);
        setLocationRelativeTo(null);

        mainPanel = new ImagePanel("/Backgrounds/Background00.png");
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

        // Set up the prize label
        prizeLabel = new JLabel("Prize: $" + prizeWon);
        prizeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        prizeLabel.setForeground(Color.WHITE);
        prizeLabel.setHorizontalAlignment(JLabel.RIGHT);

        prizePanel = new JPanel();
        prizePanel.setOpaque(true);
        prizePanel.setBackground(Color.BLACK);
        prizePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        prizeLabel.setHorizontalAlignment(JLabel.CENTER);
        prizePanel.add(prizeLabel);

        JPanel prizeWrapper = new JPanel();
        prizeWrapper.setOpaque(false);
        prizeWrapper.setLayout(new GridBagLayout());
        GridBagConstraints prizeGbc = new GridBagConstraints();
        prizeWrapper.setMaximumSize(new Dimension(1920, 200));
        prizeGbc.gridx = 0;
        prizeGbc.gridy = 0;
        prizeGbc.anchor = GridBagConstraints.NORTHEAST;
        prizeWrapper.add(prizePanel, prizeGbc);
        mainPanel.add(prizeWrapper, BorderLayout.EAST);

        loadQuestion(currentQuestionIndex);

        questionContentPanel = new JPanel();
        questionContentPanel.setOpaque(true);
        questionContentPanel.setBackground(Color.BLACK);
        questionContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        questionContentPanel.setLayout(new GridBagLayout());
        questionContentPanel.add(questionContentLabel);
        questionContentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); // Set the preferred size of the question content panel

        JPanel questionContentWrapper = new JPanel();
        questionContentWrapper.setBackground(Color.BLACK);
        questionContentWrapper.setOpaque(true);
        questionContentWrapper.setLayout(new GridBagLayout());
        questionContentWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        questionContentWrapper.add(questionContentPanel, gbc);

        // Initialize the JButtons
        answerAButton = new JButton();
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


        answerAButton.addActionListener(new AnswerButtonListener(0));
        answerBButton.addActionListener(new AnswerButtonListener(1));
        answerCButton.addActionListener(new AnswerButtonListener(2));
        answerDButton.addActionListener(new AnswerButtonListener(3));





        // Create a panel for the question and answer content
        contentAndAnswerPanel = new JPanel();
        contentAndAnswerPanel.setLayout(new BorderLayout());
        contentAndAnswerPanel.setOpaque(false);


        answerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        answerPanel.setBackground(Color.BLACK);
        answerPanel.setOpaque(true);
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

        configureAnswerButtonAndLabel(answerAButton, answerALabel, "A. " + questions[currentQuestionIndex].getAnswers()[0]);
        configureAnswerButtonAndLabel(answerBButton, answerBLabel, "B. " + questions[currentQuestionIndex].getAnswers()[1]);
        configureAnswerButtonAndLabel(answerCButton, answerCLabel, "C. " + questions[currentQuestionIndex].getAnswers()[2]);
        configureAnswerButtonAndLabel(answerDButton, answerDLabel, "D. " + questions[currentQuestionIndex].getAnswers()[3]);
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

    // Create a method to configure the answer buttons and labels
    private void configureAnswerButtonAndLabel(JButton button, JLabel label, String text) {
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.WHITE);
        label.setText(text);
        button.add(label);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.BLACK);
        button.setHorizontalAlignment(JButton.CENTER);
    }

    private void loadQuestion(int index) {
        questionIndexLabel.setText("Question " + (index + 1) + " / 30");
        questionContentLabel.setText(questions[index].getQuestion());
        questionContentLabel.setFont(new Font("Arial", Font.BOLD, 32));
        questionContentLabel.setForeground(Color.WHITE);
        questionContentLabel.setHorizontalAlignment(JLabel.CENTER);
        questionContentLabel.setVerticalAlignment(JLabel.CENTER);


        String[] answers = questions[index].getAnswers();
        answerALabel.setText("A. " + answers[0]);
        answerALabel.setFont(new Font("Arial", Font.BOLD, 24));
        answerALabel.setHorizontalAlignment(JLabel.LEFT);

        answerBLabel.setText("B. " + answers[1]);
        answerBLabel.setFont(new Font("Arial", Font.BOLD, 24));
        answerBLabel.setHorizontalAlignment(JLabel.LEFT);

        answerCLabel.setText("C. " + answers[2]);
        answerCLabel.setFont(new Font("Arial", Font.BOLD, 24));
        answerCLabel.setHorizontalAlignment(JLabel.LEFT);

        answerDLabel.setText("D. " + answers[3]);
        answerDLabel.setFont(new Font("Arial", Font.BOLD, 24));
        answerDLabel.setHorizontalAlignment(JLabel.LEFT);
    }

    private class AnswerButtonListener implements ActionListener {
        private int selectedAnswerIndex;

        public AnswerButtonListener(int selectedAnswerIndex) {
            this.selectedAnswerIndex = selectedAnswerIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            timer.stop();
            boolean isCorrect = checkAnswer(selectedAnswerIndex);
            if (isCorrect) {
                prizeWon = Math.min(currentQuestionIndex, PRIZES.length - 1);
                prizeLabel.setText("Prize: $" + PRIZES[prizeWon]);
                currentQuestionIndex++;

                if (currentQuestionIndex >= TOTAL_QUESTIONS) {
                    JOptionPane.showMessageDialog(null, "Congratulations! You have won $" + PRIZES[prizeWon] + "!", "You Win", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } else {
                    loadQuestion(currentQuestionIndex);
                    resetTimer();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect answer. You lost!", "You Lose", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
    }

    private boolean checkAnswer(int selectedAnswerIndex) {
        return questions[currentQuestionIndex].getCorrectAnswerIndex() == selectedAnswerIndex;
    }

    private void resetTimer() {
        timeRemaining = TIMER_DURATION;
        timerLabel.setText("Time: " + timeRemaining + "s");
        timer.restart();
    }

    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            timeRemaining--;

            if (timeRemaining <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(null, "Time's up! You lost!", "You Lose", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            } else {
                timerLabel.setText("Time: " + timeRemaining + "s");
            }
        }
    }

    public static void main(String[] args) {
        UIQuestion UIQuestion = new UIQuestion();
        UIQuestion.setVisible(true);
    }
}
