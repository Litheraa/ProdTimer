package litheraa.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ProdCalendar extends JPanel {
	private final GregorianCalendar calendar = new GregorianCalendar();
	private final JPanel daysPanel;
	private ArrayList<ProgressContainer> days = new ArrayList<>();

	public ProdCalendar(MainFrame mainFrame) {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		String[] monthName = new String[]{"ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ", "ИЮЛЬ", "АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"};
		JLabel monthLabel = new JLabel(monthName[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR));
		monthLabel.setOpaque(true);
		monthLabel.setBackground(Color.ORANGE);
		add(monthLabel);

		JPanel dayNamesPanel = new JPanel(new GridLayout(1, 7, 5, 0));
		String[] dayNames = new String[]{"Понедельник", "Вторник", "Среда", " Четверг", "Пятница", "Суббота", "Воскресенье"};
		for (int i = 0; i < 7; i++) {
			JLabel dayName = new JLabel(dayNames[i]);
			dayName.setOpaque(true);
			dayName.setBackground(Color.LIGHT_GRAY);
			dayNamesPanel.add(dayName);
		}
		dayNamesPanel.setSize(0, 10);
		add(dayNamesPanel);

		daysPanel = new JPanel(new GridLayout(6, 7, 5, 5));
		for (int i = 0; i < 42; i++) {
			JPanel tempPanel = new JPanel();
			tempPanel.setVisible(false);
			daysPanel.add(tempPanel);
		}
		add(daysPanel);

		layout.putConstraint(SpringLayout.WIDTH, monthLabel, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.NORTH, monthLabel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, monthLabel, 35, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, dayNamesPanel, 1, SpringLayout.SOUTH, monthLabel);
		layout.putConstraint(SpringLayout.SOUTH, dayNamesPanel, 25, SpringLayout.SOUTH, monthLabel);
		layout.putConstraint(SpringLayout.WIDTH, dayNamesPanel, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.WIDTH, daysPanel, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.NORTH, daysPanel, 1, SpringLayout.SOUTH, dayNamesPanel);
		layout.putConstraint(SpringLayout.SOUTH, daysPanel, 0, SpringLayout.SOUTH, this);
	}

	private void createDays() {
		for (int i = 1; i <= calendar.getActualMaximum(Calendar.DATE); i++) {
			ProgressContainer container = new ProgressContainer(i, 100);
			container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			container.createVerticalProgress(i);
			days.add(container);
//			daysPanel.add(container);
		}
	}

	public ProgressContainer getDayContainer(int date) {
		return days.get(date);
	}

	public ArrayList<ProgressContainer> getDayContainers() {
		return days;
	}

	public void adjustDayNoSize() {
		double height = (int) daysPanel.getComponent(0).getSize().getHeight();
		Font font = new Font("Aerial", Font.BOLD, (int) (height * 0.45));
		createDays();
		for (ProgressContainer container : days) {
			container.setFont(font);
		}
		int firstMonthDay = (new GregorianCalendar(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				1).
				get(Calendar.DAY_OF_WEEK) + 6) % 7;
		if (firstMonthDay == 0) {
			firstMonthDay = 7;
		}
		for (int i = 0, j = 0; i < calendar.getActualMaximum(Calendar.DATE) + firstMonthDay - 1; i++) {
			if (i < firstMonthDay) {
				continue;
			}
			daysPanel.remove(i);
			daysPanel.add(days.get(j), i);
		}
	}
}
