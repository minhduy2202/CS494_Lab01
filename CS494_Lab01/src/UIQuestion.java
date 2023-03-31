import javax.swing.*;
import java.awt.*;

public class UIQuestion extends JFrame {

    private JLabel questionIndexLabel, questionContentLabel, answerALabel, answerBLabel, answerCLabel, answerDLabel;
    private JButton answerAButton, answerBButton, answerCButton, answerDButton;

    public UIQuestion() {
        // Set up the frame
        setTitle("Who Wants to Be a Millionaire - Question");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        // Set up the question index label
        questionIndexLabel = new JLabel("Question 1 / 30");
        questionIndexLabel.setFont(new Font("Arial", Font.BOLD, 24));
        questionIndexLabel.setHorizontalAlignment(JLabel.LEFT);
        questionIndexLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        add(questionIndexLabel, BorderLayout.NORTH);

        // Set up the question content label
        questionContentLabel = new JLabel("What is the capital city of France?");
        questionContentLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        questionContentLabel.setHorizontalAlignment(JLabel.CENTER);
        questionContentLabel.setVerticalAlignment(JLabel.CENTER);
        add(questionContentLabel, BorderLayout.CENTER);

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
        answerAButton.setPreferredSize(new Dimension(300, 80));

        answerBButton = new JButton();
        answerBButton.add(answerBLabel);
        answerBButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerBButton.setPreferredSize(new Dimension(300, 80));

        answerCButton = new JButton();
        answerCButton.add(answerCLabel);
        answerCButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerCButton.setPreferredSize(new Dimension(300, 80));

        answerDButton = new JButton();
        answerDButton.add(answerDLabel);
        answerDButton.setFont(new Font("Arial", Font.PLAIN, 24));
        answerDButton.setPreferredSize(new Dimension(300, 80));

        JPanel answerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        answerPanel.add(answerAButton);
        answerPanel.add(answerBButton);
        answerPanel.add(answerCButton);
        answerPanel.add(answerDButton);
        answerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(answerPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        UIQuestion UIQuestion = new UIQuestion();
        UIQuestion.setVisible(true);
    }
}
