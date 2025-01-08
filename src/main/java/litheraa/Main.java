package litheraa;

import litheraa.view.MainFrame;
import litheraa.view.ProgressContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Main {
	public static void main(String[] args) {
        new ProdTimerController();
		MainFrame mainFrame = new MainFrame();
		SpringLayout layout = new SpringLayout();
		JPanel panel = new JPanel(layout);

		GregorianCalendar calendar = new GregorianCalendar();
		GregorianCalendar temp = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);

		int tempDay = temp.get(Calendar.DAY_OF_WEEK);
		int firstMonthDay = (tempDay + 6) % 7;
		if (firstMonthDay == 0) {
			firstMonthDay = 7;
		}

		JPanel dayNamesPanel = new JPanel(new GridLayout(1, 7, 5, 0));
		for (int i = 0; i < 7; i++) {
			dayNamesPanel.add(new JLabel(String.valueOf(i)));
		}

		GridLayout gridLayout = new GridLayout(6, 7);
		gridLayout.setHgap(5);
		gridLayout.setVgap(5);
		JPanel daysPanel = new JPanel(gridLayout);

		JLabel monthLabel = new JLabel(calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.YEAR));
//		ProgressContainer container = new ProgressContainer(24, 100).createVerticalProgress(24);
//		daysPanel.add(container);
		for (int i = 0; i < calendar.getActualMaximum(Calendar.DATE) + firstMonthDay - 1; i++) {
			ProgressContainer container = new ProgressContainer(i, 100).createVerticalProgress(24);
			container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			if (i < firstMonthDay - 1) {
				container.setVisible(false);
			}
			daysPanel.add(container);
		}

		layout.putConstraint(SpringLayout.WIDTH, monthLabel, 0, SpringLayout.WIDTH, panel);
		layout.putConstraint(SpringLayout.WIDTH, daysPanel, 0, SpringLayout.WIDTH, panel);
		layout.putConstraint(SpringLayout.NORTH, dayNamesPanel, 1, SpringLayout.SOUTH, monthLabel);
		layout.putConstraint(SpringLayout.HEIGHT, dayNamesPanel, 0, SpringLayout.HEIGHT, mainFrame);
		layout.putConstraint(SpringLayout.WIDTH, dayNamesPanel, 0, SpringLayout.WIDTH, panel);
		layout.putConstraint(SpringLayout.NORTH, daysPanel, 1, SpringLayout.SOUTH, dayNamesPanel);
		layout.putConstraint(SpringLayout.SOUTH, daysPanel, 1, SpringLayout.SOUTH, panel);

		panel.add(monthLabel);
		panel.add(dayNamesPanel);
		panel.add(daysPanel);


		mainFrame.setMainComponent(panel);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setSize(500, 500);
		mainFrame.setVisible(true);

		JLabel label = (JLabel) ((ProgressContainer)daysPanel.getComponent(15)).getComponent(1);
		double height = (int) daysPanel.getComponent(15).getSize().getHeight();
		System.out.println(height);
		System.out.println(label.getFont().getSize());
		Font font = new Font("Aerial", Font.BOLD, (int) (height * 0.45));
		label.setFont(font);
		System.out.println(label.getFont().getSize());

		JLabel jLabel = (JLabel) ((ProgressContainer)daysPanel.getComponent(15)).getComponent(2);
		jLabel.setSize(100, 100);
	}

	private Image getScaledImage(Image srcImg, int w, int h){
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();

		return resizedImg;
	}
}
