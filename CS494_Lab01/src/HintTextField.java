import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class HintTextField extends JTextField implements FocusListener {
    private String hintText;

    public HintTextField(String hintText) {
        super();
        this.hintText = hintText;
        addFocusListener(this);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getText().isEmpty() && !hasFocus()) {
            int height = getHeight();
            int textBottom = (height - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();

            // Change the color to light gray or white
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Arial", Font.ITALIC, 32));
            g.drawString(hintText, 4, textBottom);
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        repaint();
    }
}