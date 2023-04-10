import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SplashScreen extends JFrame {

    private JLabel loadingLabel;
    private JProgressBar progressBar;

    public SplashScreen() {
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
        loadingLabel = new JLabel("Waiting for another players...");
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

        // Set up the timer to close the splash screen after 10 seconds
        Timer timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeSplashScreen();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void closeSplashScreen() {
        // Close the splash screen and dispose of its resources
        setVisible(false);
        dispose();
    }

    public static void main(String[] args) {
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.setVisible(true);
    }
}
