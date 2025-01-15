package litheraa.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ProdCalendar extends JPanel {
	private final GregorianCalendar calendar;
	private final DaysPanel daysPanel;
	private ArrayList<ProgressContainer> days = new ArrayList<>();
	private final litheraa.data.calendar.Calendar data;

	public ProdCalendar(MainFrame mainFrame, litheraa.data.calendar.Calendar data) {
		this.data = data;
		this.calendar = new GregorianCalendar(data.getYear(), data.getMonth() - 1, 1);
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		JLabel monthLabel = new JLabel(data.getMonthName() + " " + data.getYear());
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

		int weeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
		//there is some strange bug in util.Calendar getActualMaximum(week_of_month) for November and December months. So, quick solution
		if (data.getMonth() >= 11) {
			weeks = weeks + 1;
		}
		daysPanel = new DaysPanel(weeks, 7, 5, 5);
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

	private void createEmptyDays() {
		int firsDay = (calendar.get(Calendar.DAY_OF_WEEK) + 6) % 7;
		if (firsDay == 0) {
			firsDay = 7;
		}
		for (int i = 1; i < firsDay; i++) {
			JPanel emptyDay = new JPanel();
			emptyDay.setVisible(false);
			daysPanel.add(emptyDay);
		}
	}

	private void createDays() {
		for (int day = 1; day <= calendar.getActualMaximum(Calendar.DATE); day++) {
			ProgressContainer container = new ProgressContainer(data.getProgress(day), data.getDayGoal(day));
			container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			container.createVerticalProgress(day);
			days.add(container);
			daysPanel.add(days.get(day - 1));
		}
	}

	public ProgressContainer getDayContainer(int date) {
		return days.get(date);
	}

	public ArrayList<ProgressContainer> getDayContainers() {
		return days;
	}

	public void adjustDaySize() {
		createEmptyDays();
		createDays();
		for (ProgressContainer container : days) {
			container.adjustInnerComponentsSize(daysPanel.getDaySize());
		}
	}
}
