package litheraa.view;

import javax.swing.*;
import java.awt.*;

public class ToastMessage extends JFrame {

    JPanel panel;
    JWindow window;

    public ToastMessage(String message) {

        panel = new JPanel() {
            public void paintComponent(Graphics graphics) {
                int width = graphics.getFontMetrics().stringWidth(message);
                int height = graphics.getFontMetrics().getHeight();

                graphics.setColor(Color.white);
                graphics.fillRect(10, 10, width + 30, height + 20);
                graphics.setColor(Color.white);
                graphics.drawRect(10, 10, width + 30, height + 20);
                graphics.setColor(Color.black);
                graphics.drawString(message, 25, 32);
                graphics.setColor(Color.black);
                graphics.drawRect(10, 10, width + 30, height + 20);
            }
        };

        window = new JWindow();
        window.setBackground(new Color(0, 0, 0, 0));
        window.add(panel);
        window.setSize(300, 300);
    }

    public void showToast(Point point, int showTime) {
        try {
            window.setLocation(point);
            window.setOpacity(1);
            window.setVisible(true);

            Thread.sleep(showTime);

            for (double d = 1.0; d > 0.2; d -= 1) {
                Thread.sleep(100);
                window.setOpacity((float) d);
            }
            window.setVisible(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
