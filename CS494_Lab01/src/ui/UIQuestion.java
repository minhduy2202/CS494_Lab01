package ui;

import client.ClientNetwork;
import client.Player;
import packet.Packet;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
public class UIQuestion extends JFrame implements PropertyChangeListener {

    private static final int TIMER_DURATION = 30; // in seconds

    Player player;
    ClientNetwork clientNetwork;
    private JLabel questionIndexLabel, questionContentLabel, answerALabel, answerBLabel, answerCLabel, answerDLabel;
    private JLabel timerLabel, prizeLabel;
    private JButton answerAButton, answerBButton, answerCButton, answerDButton;
    private Timer timer;
    private int timeRemaining;
    private ImagePanel mainPanel;

    private JButton skipButton;
    private boolean skipUsed = false;

    private JPanel answerPanel, questionIndexPanel, questionContentPanel, timerPanel,
            timerWrapper, contentAndAnswerPanel, skipButtonPanel, questionContentWrapper;

    public UIQuestion(Player player, ClientNetwork clientNetwork) {
        this.player = player;
        this.clientNetwork = clientNetwork;
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

        loadQuestion();

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

        // Initialize the JButtons
        answerAButton = new JButton();
        answerAButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerAButton.setPreferredSize(new Dimension(
                480, 80));

        answerBButton = new JButton();
        answerBButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerBButton.setPreferredSize(new Dimension(480, 80));

        answerCButton = new JButton();
        answerCButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerCButton.setPreferredSize(new Dimension(480, 80));

        answerDButton = new JButton();
        answerDButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerDButton.setPreferredSize(new Dimension(480, 80));


        answerAButton.addActionListener(new AnswerButtonListener("A"));
        answerBButton.addActionListener(new AnswerButtonListener("B"));
        answerCButton.addActionListener(new AnswerButtonListener("C"));
        answerDButton.addActionListener(new AnswerButtonListener("D"));


        // Create a panel for the question and answer content
        contentAndAnswerPanel = new JPanel();
        contentAndAnswerPanel.setLayout(new BorderLayout());
        contentAndAnswerPanel.setOpaque(true);


        answerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        answerPanel.setOpaque(true);
        answerPanel.setBackground(Color.RED);
        answerPanel.add(answerAButton);
        answerPanel.add(answerBButton);
        answerPanel.add(answerCButton);
        answerPanel.add(answerDButton);
        answerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        answerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));


        // Create a new panel to hold the question content and answer buttons
        contentAndAnswerPanel = new JPanel();
        contentAndAnswerPanel.setOpaque(true);
        //questionContentPanel.setBackground(Color.WHITE);
        contentAndAnswerPanel.setLayout(new BoxLayout(contentAndAnswerPanel, BoxLayout.Y_AXIS));
        contentAndAnswerPanel.add(Box.createVerticalGlue()); // Add vertical glue to center the question content and answer panel vertically
        contentAndAnswerPanel.add(questionContentWrapper);
        // contentAndAnswerPanel.add(Box.createVerticalStrut(20)); // Add vertical strut to create a 20-pixel gap between the question content and answer panel
        contentAndAnswerPanel.add(answerPanel);
        contentAndAnswerPanel.add(Box.createVerticalGlue()); // Add vertical glue to center the question content and answer panel vertically

        contentAndAnswerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        Map<String, String> answers = player.getQuestion().getAnswers();

        configureAnswerButtonAndLabel(answerAButton, answerALabel, "A. " + answers.get(Constants.ANSWER_A));
        configureAnswerButtonAndLabel(answerBButton, answerBLabel, "B. " + answers.get(Constants.ANSWER_B));
        configureAnswerButtonAndLabel(answerCButton, answerCLabel, "C. " + answers.get(Constants.ANSWER_C));
        configureAnswerButtonAndLabel(answerDButton, answerDLabel, "D. " + answers.get(Constants.ANSWER_D));
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
                if (!player.getUsedSkip()) {
                    int result = JOptionPane.showConfirmDialog(
                            UIQuestion.this,
                            "Are you sure you want to skip this question?",
                            "Skip Question",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (result == JOptionPane.YES_OPTION) {
                        Packet packet = new Packet(Constants.CLIENT_MOVE_TURN_PACKET_ID);
                        clientNetwork.sendPacket2Server(packet);
                    }
                } else {
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
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.BLACK);
        button.setHorizontalAlignment(JButton.CENTER);
    }

    private void loadQuestion() {
        questionIndexLabel.setText("Question " + (player.getCurQuestionIdx()) + " / 30");
        questionContentLabel.setText(player.getQuestion().getQuestion());
        questionContentLabel.setFont(new Font("Arial", Font.BOLD, 32));
        questionContentLabel.setForeground(Color.BLACK);
        questionContentLabel.setHorizontalAlignment(JLabel.CENTER);
        questionContentLabel.setVerticalAlignment(JLabel.CENTER);

        Map<String, String> answers = player.getQuestion().getAnswers();

        answerALabel.setText("A. " + answers.get(Constants.ANSWER_A));
        answerALabel.setFont(new Font("Arial", Font.BOLD, 24));
        answerALabel.setHorizontalAlignment(JLabel.LEFT);

        answerBLabel.setText("B. " + answers.get(Constants.ANSWER_B));
        answerBLabel.setFont(new Font("Arial", Font.BOLD, 24));
        answerBLabel.setHorizontalAlignment(JLabel.LEFT);

        answerCLabel.setText("C. " + answers.get(Constants.ANSWER_C));
        answerCLabel.setFont(new Font("Arial", Font.BOLD, 24));
        answerCLabel.setHorizontalAlignment(JLabel.LEFT);

        answerDLabel.setText("D. " + answers.get(Constants.ANSWER_D));
        answerDLabel.setFont(new Font("Arial", Font.BOLD, 24));
        answerDLabel.setHorizontalAlignment(JLabel.LEFT);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.setPlayer((Player) evt.getNewValue());
        if (player.getReceivePacketID() == Constants.SERVER_QUESTION_RESULT_PACKET_ID
                && Objects.equals(player.getResult(), Constants.WRONG)){
            clientNetwork.getClientHandlerTmp().removePropertyChangeListener(this);
            UIQuestion.this.dispose();
            YouLoseUI youLoseUI = new YouLoseUI();
            youLoseUI.setVisible(true);
        } else if (player.getReceivePacketID() == Constants.SERVER_MOVE_TURN_PACKET_ID && Objects.equals(player.getMoveTurnStatus(), Constants.SUCCESS)){
            WatingTurnScreen watingTurnScreen = new WatingTurnScreen(player, clientNetwork);
            clientNetwork.getClientHandlerTmp().addPropertyChangeListener(watingTurnScreen);
            clientNetwork.getClientHandlerTmp().removePropertyChangeListener(this);
            UIQuestion.this.dispose();
            watingTurnScreen.setVisible(true);
        } else if (player.getReceivePacketID() == Constants.SERVER_QUESTION_PACKET_ID
                && Objects.equals(player.getCurCandidate(), player.getUsername())){
            loadQuestion();
            resetTimer();
        } else if (player.getReceivePacketID() == Constants.SERVER_WIN_PACKET_ID){
            clientNetwork.getClientHandlerTmp().removePropertyChangeListener(this);
            UIQuestion.this.dispose();
            YouWinUI youWinUI = new YouWinUI();
            youWinUI.setVisible(true);
        }

    }

    public void setPlayer(Player player){
        this.player = player;
    }


    private class AnswerButtonListener implements ActionListener {
        private String selectedAnswer;

        public AnswerButtonListener(String selectedAnswer) {
            this.selectedAnswer = selectedAnswer;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int confirmResult = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to choose this answer?",
                    "Confirm Answer",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmResult == JOptionPane.YES_OPTION) {
                timer.stop();
//                boolean isCorrect = checkAnswer(selectedAnswerIndex);
                Packet packet = new Packet(Constants.CLIENT_ANSWER_PACKET_ID);
                packet.addKey(Constants.ANSWER, selectedAnswer);
                clientNetwork.sendPacket2Server(packet);
            } else {
                resetTimer();
            }
        }
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


    private void resizeComponents() {
        int width = getWidth();
        int height = getHeight();

        float widthRatio = (float) width / 1920;
        float heightRatio = (float) height / 1080;

        int newFontSize = Math.min(Math.round(24 * widthRatio), Math.round(24 * heightRatio));
        int newBoldFontSize = Math.min(Math.round(24 * widthRatio), Math.round(24 * heightRatio));

        questionIndexLabel.setFont(new Font("Arial", Font.BOLD, newBoldFontSize));
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

        questionIndexPanel.setBorder(BorderFactory.createEmptyBorder(Math.round(height * 0.02f), Math.round(width * 0.02f), Math.round(height * 0.02f), Math.round(width * 0.02f)));
        timerPanel.setBorder(BorderFactory.createEmptyBorder(Math.round(height * 0.02f), Math.round(width * 0.02f), Math.round(height * 0.02f), Math.round(width * 0.02f)));
        questionContentPanel.setBorder(BorderFactory.createEmptyBorder(Math.round(height * 0.02f), Math.round(width * 0.02f), Math.round(height * 0.02f), Math.round(width * 0.02f)));
        answerPanel.setBorder(BorderFactory.createEmptyBorder(Math.round(height * 0.02f), Math.round(width * 0.02f), Math.round(height * 0.02f), Math.round(width * 0.02f)));
        skipButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, Math.round(width * 0.02f)));

        questionContentWrapper.setPreferredSize(new Dimension((int) (Integer.MAX_VALUE * widthRatio), Math.round(height * 0.4f)));
        contentAndAnswerPanel.setPreferredSize(new Dimension((int) (width), Math.round(height * 0.4f)));

        revalidate();
        repaint();
    }
}
