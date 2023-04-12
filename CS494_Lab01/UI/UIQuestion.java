import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.border.Border;

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
            new Question1("What is the capital city of London?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
            new Question1("What is the capital city of Italia?", new String[]{"Paris", "Rome", "London", "Madrid"}, 0),
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

    private JButton skipButton;
    private boolean skipUsed = false;

    private JPanel answerPanel, questionIndexPanel, questionContentPanel, timerPanel,
            timerWrapper, contentAndAnswerPanel, prizePanel, skipButtonPanel, questionContentWrapper;

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
        setResizable(true);
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
        // mainPanel.add(prizeWrapper, BorderLayout.EAST);

        loadQuestion(currentQuestionIndex);

        questionContentPanel = new JPanel();
        questionContentPanel.setOpaque(true);
        questionContentPanel.setBackground(Color.WHITE);
        questionContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        questionContentPanel.setLayout(new GridBagLayout());
        questionContentPanel.add(questionContentLabel);
        questionContentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); // Set the preferred size of the question content panel

        questionContentWrapper = new JPanel();
        questionContentWrapper.setBackground(Color.WHITE);
        questionContentWrapper.setOpaque(true);
        questionContentWrapper.setLayout(new GridBagLayout());
        questionContentWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        questionContentWrapper.add(questionContentPanel, gbc);


        Border roundedBorder = BorderFactory.createLineBorder(Color.BLACK, 5, true);
        // Initialize the JButtons
        answerAButton = new JButton();
        answerAButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerAButton.setPreferredSize(new Dimension(
                480, 80));
        answerAButton.setBorder(roundedBorder);

        answerBButton = new JButton();
        answerBButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerBButton.setPreferredSize(new Dimension(480, 80));
        answerBButton.setBorder(roundedBorder);

        answerCButton = new JButton();
        answerCButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerCButton.setPreferredSize(new Dimension(480, 80));
        answerCButton.setBorder(roundedBorder);

        answerDButton = new JButton();
        answerDButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerDButton.setPreferredSize(new Dimension(480, 80));
        answerDButton.setBorder(roundedBorder);


        answerAButton.addActionListener(new AnswerButtonListener(0));
        answerBButton.addActionListener(new AnswerButtonListener(1));
        answerCButton.addActionListener(new AnswerButtonListener(2));
        answerDButton.addActionListener(new AnswerButtonListener(3));


        // Create a panel for the question and answer content
        contentAndAnswerPanel = new JPanel();
        contentAndAnswerPanel.setLayout(new BorderLayout());
        contentAndAnswerPanel.setOpaque(true);


        answerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        answerPanel.setOpaque(true);
        //answerPanel.setBackground(Color.RED);
        answerPanel.add(answerAButton);
        answerPanel.add(answerBButton);
        answerPanel.add(answerCButton);
        answerPanel.add(answerDButton);
        //answerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        answerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        answerPanel.setBorder(roundedBorder);


        // Create a new panel to hold the question content and answer buttons
        contentAndAnswerPanel = new JPanel();
        contentAndAnswerPanel.setOpaque(true);
        //questionContentPanel.setBackground(Color.WHITE);
        contentAndAnswerPanel.setLayout(new BoxLayout(contentAndAnswerPanel, BoxLayout.Y_AXIS));
        contentAndAnswerPanel.add(Box.createVerticalGlue()); // Add vertical glue to center the question content and answer panel vertically
        questionContentWrapper.setBorder(roundedBorder);

        contentAndAnswerPanel.add(questionContentWrapper);
        contentAndAnswerPanel.add(Box.createVerticalStrut(10)); // Add vertical strut to create a 20-pixel gap between the question content and answer panel
        answerPanel.setBorder(roundedBorder);
        contentAndAnswerPanel.add(answerPanel);
        contentAndAnswerPanel.add(Box.createVerticalGlue()); // Add vertical glue to center the question content and answer panel vertically

        contentAndAnswerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        contentAndAnswerPanel.setBorder(roundedBorder);

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


        // Initialize the skip button
        skipButton = new JButton("Skip Question");
        skipButton.setFont(new Font("Arial", Font.PLAIN, 24));
        skipButton.setPreferredSize(new Dimension(200, 50));
        skipButton.setHorizontalAlignment(JLabel.RIGHT);
        skipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!skipUsed) {
                    int result = JOptionPane.showConfirmDialog(
                            UIQuestion.this,
                            "Are you sure you want to skip this question?",
                            "Skip Question",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (result == JOptionPane.YES_OPTION) {
                        skipUsed = true;
                        // Replace the code below with the logic to load the next question
                        System.out.println("Question skipped!");
                        currentQuestionIndex++;
                        loadQuestion(currentQuestionIndex);
                        resetTimer();
                    }
                    else{

                    }
                }
                else {
                    JOptionPane.showMessageDialog(
                            UIQuestion.this,
                            "You have already used your skip option.",
                            "Skip Unavailable",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });

        skipButtonPanel = new JPanel();
        skipButtonPanel.setOpaque(true);
        skipButtonPanel.setBackground(Color.BLACK);
        skipButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        skipButtonPanel.add(skipButton);

        JPanel skipButtonWrapper = new JPanel();
        skipButtonWrapper.setOpaque(false);
        skipButtonWrapper.setLayout(new GridBagLayout());
        GridBagConstraints skipButtonIndexGbc = new GridBagConstraints();
        skipButtonWrapper.setMaximumSize(new Dimension(200, 50));
        skipButtonIndexGbc.gridx = 0;
        skipButtonIndexGbc.gridy = 0;
        skipButtonIndexGbc.anchor = GridBagConstraints.NORTHEAST;
        skipButtonWrapper.add(skipButtonPanel, skipButtonIndexGbc);
        mainPanel.add(skipButtonWrapper, BorderLayout.EAST);


        // Add the ComponentListener to handle window resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeComponents();
            }
        });

//        GridBagConstraints skipGbc = new GridBagConstraints();
//        skipGbc.gridx = 0;
//        skipGbc.gridy = 0;
//        skipGbc.anchor = GridBagConstraints.EAST;
//        skipGbc.insets = new Insets(0, 0, 0, 0);
//        mainPanel.add(skipButton, BorderLayout.EAST);
//        timerWrapper.add(skipButton, skipGbc);

    }

    // Create a method to configure the answer buttons and labels
    private void configureAnswerButtonAndLabel(JButton button, JLabel label, String text) {
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.BLACK);
        label.setText(text);
        button.add(label);
        button.setFocusPainted(true); // Da sua lai
        button.setContentAreaFilled(true); // Da sua lai
        //button.setBorder(BorderFactory.createEmptyBorder());
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.BLACK);
        button.setHorizontalAlignment(JButton.CENTER);
    }

    private void loadQuestion(int index) {
        questionIndexLabel.setText("Question " + (index + 1) + " / 30");
        questionContentLabel.setText(questions[index].getQuestion());
        questionContentLabel.setFont(new Font("Arial", Font.BOLD, 32));
        questionContentLabel.setForeground(Color.BLACK);
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
        private String choice;

        public AnswerButtonListener(int selectedAnswerIndex) {
            this.selectedAnswerIndex = selectedAnswerIndex;
            if(selectedAnswerIndex == 0) choice = "A";
            if(selectedAnswerIndex == 1) choice = "B";
            if(selectedAnswerIndex == 2) choice = "C";
            if(selectedAnswerIndex == 3) choice = "D";
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int confirmResult = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to choose answer " + choice + "?",
                    "Confirm Answer",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmResult == JOptionPane.YES_OPTION) {
                timer.stop();
                JButton selectedButton = (JButton) e.getSource();
                selectedButton.setFont(selectedButton.getFont().deriveFont(Font.BOLD));



                // Disable all answer buttons
                answerAButton.setEnabled(false);
                answerBButton.setEnabled(false);
                answerCButton.setEnabled(false);
                answerDButton.setEnabled(false);

                // Wait for 2 seconds before checking the answer
                Timer delayTimer = new Timer(2000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {

                        boolean isCorrect = checkAnswer(selectedAnswerIndex);
                        if (isCorrect) {
                            prizeWon = Math.min(currentQuestionIndex, PRIZES.length - 1);
                            prizeLabel.setText("Prize: $" + PRIZES[prizeWon]);
                            currentQuestionIndex++;

                            if (currentQuestionIndex >= TOTAL_QUESTIONS) {
                                UIQuestion.this.dispose();

                                YouWinUI youWinUI = new YouWinUI();
                                youWinUI.setVisible(true);
                            } else {
                                loadQuestion(currentQuestionIndex);
                                resetTimer();
                            }
                        } else {
                            UIQuestion.this.dispose();

                            YouLoseUI youLoseUI = new YouLoseUI();
                            youLoseUI.setVisible(true);
                        }

                        // Re-enable all answer buttons and reset font
                        answerAButton.setEnabled(true);
                        answerBButton.setEnabled(true);
                        answerCButton.setEnabled(true);
                        answerDButton.setEnabled(true);
                        selectedButton.setFont(selectedButton.getFont().deriveFont(Font.PLAIN));
                    }
                });

                delayTimer.setRepeats(false);
                delayTimer.start();
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
//                JOptionPane.showMessageDialog(null, "Time's up! You lost!", "You Lose", JOptionPane.INFORMATION_MESSAGE);
//                System.exit(0);
                UIQuestion.this.dispose();

                YouLoseUI youLoseUI = new YouLoseUI();
                youLoseUI.setVisible(true);
            } else {
                timerLabel.setText("Time: " + timeRemaining + "s");
            }
        }
    }

//    private void resizeComponents() {
//        int width = getWidth();
//        int height = getHeight();
//
//        float widthRatio = (float) width / 1920;
//        float heightRatio = (float) height / 1080;
//
//        int newFontSize = Math.min(Math.round(24 * widthRatio), Math.round(24 * heightRatio));
//        int newBoldFontSize = Math.min(Math.round(24 * widthRatio), Math.round(24 * heightRatio));
//
//        questionIndexLabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));
//        prizeLabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));
//        timerLabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));
//        questionContentLabel.setFont(new Font("Arial", Font.PLAIN, newFontSize));
//        answerAButton.setFont(new Font("Arial", Font.PLAIN, newFontSize));
//        answerBButton.setFont(new Font("Arial", Font.PLAIN, newFontSize));
//        answerCButton.setFont(new Font("Arial", Font.PLAIN, newFontSize));
//        answerDButton.setFont(new Font("Arial", Font.PLAIN, newFontSize));
//        skipButton.setFont(new Font("Arial", Font.PLAIN, newFontSize));
//
//        int newButtonWidth = Math.round(480 * widthRatio);
//        int newButtonHeight = Math.round(80 * heightRatio);
//
//        answerAButton.setPreferredSize(new Dimension(newButtonWidth, newButtonHeight));
//        answerBButton.setPreferredSize(new Dimension(newButtonWidth, newButtonHeight));
//        answerCButton.setPreferredSize(new Dimension(newButtonWidth, newButtonHeight));
//        answerDButton.setPreferredSize(new Dimension(newButtonWidth, newButtonHeight));
//
//        questionIndexPanel.setBorder(BorderFactory.createEmptyBorder(
//                Math.round(10 * heightRatio),
//                Math.round(20 * widthRatio),
//                Math.round(10 * heightRatio),
//                Math.round(20 * widthRatio)
//        ));
//
//        prizePanel.setBorder(BorderFactory.createEmptyBorder(
//                Math.round(10 * heightRatio),
//                Math.round(20 * widthRatio),
//                Math.round(10 * heightRatio),
//                Math.round(20 * widthRatio)
//        ));
//
//        timerPanel.setBorder(BorderFactory.createEmptyBorder(
//                Math.round(10 * heightRatio),
//                Math.round(20 * widthRatio),
//                Math.round(10 * heightRatio),
//                Math.round(20 * widthRatio)
//        ));
//
//        questionContentPanel.setPreferredSize(new Dimension(width, Math.round(200 * heightRatio)));
//        questionContentPanel.setBorder(BorderFactory.createEmptyBorder(
//                Math.round(20 * heightRatio),
//                Math.round(20 * widthRatio),
//                Math.round(20 * heightRatio),
//                Math.round(20 * widthRatio)
//        ));
//
//        answerPanel.setBorder(BorderFactory.createEmptyBorder(
//                Math.round(20 * heightRatio),
//                Math.round(20 * widthRatio),
//                Math.round(20 * heightRatio),
//                Math.round(20 * widthRatio)
//        ));
//        answerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Math.round(80 * heightRatio)));
//
//        skipButtonPanel.setBorder(BorderFactory.createEmptyBorder(
//                0,
//                0,
//                0,
//                Math.round(20 * widthRatio)
//        ));
//
//        questionContentWrapper.setPreferredSize(new Dimension((int) (Integer.MAX_VALUE * widthRatio), Math.round(400 * heightRatio)));
//        contentAndAnswerPanel.setPreferredSize(new Dimension((int) (1920 * widthRatio), Math.round(200 * heightRatio)));
//
//        revalidate();
//        repaint();
//
//    }

    private void resizeComponents() {
            int width = getWidth();
            int height = getHeight();

            float widthRatio = (float) width / 1920;
            float heightRatio = (float) height / 1080;

            int newFontSize = Math.min(Math.round(20 * widthRatio), Math.round(22 * heightRatio));
            int newBoldFontSize = Math.min(Math.round(20 * widthRatio), Math.round(22 * heightRatio));

            questionIndexLabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));
            prizeLabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));
            timerLabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));

            int newContentWidth = Math.round(width * 0.6f);
            int newContentHeight = Math.round(height * 0.4f);
            questionContentPanel.setPreferredSize(new Dimension(newContentWidth, newContentHeight));
            answerPanel.setPreferredSize(new Dimension(newContentWidth, Math.round(height * 0.4f)));

            int newButtonWidth = Math.round(newContentWidth * 0.4f);
            int newButtonHeight = Math.round(newContentHeight * 0.15f);

            answerAButton.setPreferredSize(new Dimension(newButtonWidth, newButtonHeight));
            answerBButton.setPreferredSize(new Dimension(newButtonWidth, newButtonHeight));
            answerCButton.setPreferredSize(new Dimension(newButtonWidth, newButtonHeight));
            answerDButton.setPreferredSize(new Dimension(newButtonWidth, newButtonHeight));

            questionIndexLabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));
            prizeLabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));
            timerLabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));
            questionContentLabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));

            answerALabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));
            answerBLabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));
            answerCLabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));
            answerDLabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));

            answerAButton.setFont(new Font("Arial", Font.PLAIN, newFontSize));
            answerBButton.setFont(new Font("Arial", Font.PLAIN, newFontSize));
            answerCButton.setFont(new Font("Arial", Font.PLAIN, newFontSize));
            answerDButton.setFont(new Font("Arial", Font.PLAIN, newFontSize));


            questionIndexPanel.setBorder(BorderFactory.createEmptyBorder(Math.round(height * 0.02f), Math.round(width * 0.02f), Math.round(height * 0.02f), Math.round(width * 0.02f)));
            prizePanel.setBorder(BorderFactory.createEmptyBorder(Math.round(height * 0.02f), Math.round(width * 0.02f), Math.round(height * 0.02f), Math.round(width * 0.02f)));
            timerPanel.setBorder(BorderFactory.createEmptyBorder(Math.round(height * 0.02f), Math.round(width * 0.02f), Math.round(height * 0.02f), Math.round(width * 0.02f)));
            questionContentPanel.setBorder(BorderFactory.createEmptyBorder(Math.round(height * 0.02f), Math.round(width * 0.02f), Math.round(height * 0.02f), Math.round(width * 0.02f)));
            answerPanel.setBorder(BorderFactory.createEmptyBorder(Math.round(height * 0.02f), Math.round(width * 0.02f), Math.round(height * 0.02f), Math.round(width * 0.02f)));
            skipButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, Math.round(width * 0.02f)));



            questionContentWrapper.setPreferredSize(new Dimension((int) (Integer.MAX_VALUE * widthRatio), Math.round(height * 0.4f)));
            contentAndAnswerPanel.setPreferredSize(new Dimension((int) (width), Math.round(height * 0.4f)));

            revalidate();
            repaint();
    }

    public static void main(String[] args) {
        UIQuestion UIQuestion = new UIQuestion();
        UIQuestion.setVisible(true);
    }
}
