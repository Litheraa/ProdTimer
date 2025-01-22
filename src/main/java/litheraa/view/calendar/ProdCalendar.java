package litheraa.view.calendar;

import litheraa.ViewController;
import litheraa.data.calendar.Calendar;
import litheraa.util.ViewType;
import litheraa.view.themes.ThemeColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Objects;

public class ProdCalendar extends JPanel {
	private DaysPanel daysPanel;
	private final ArrayList<ProgressContainer> days = new ArrayList<>();
	private litheraa.data.calendar.Calendar data;
	private final SmoothComboBox<String> monthLabel;
	private final SmoothComboBox<Integer> yearLabel;
	private final ViewController controller;

	public ProdCalendar(ViewController controller, litheraa.data.calendar.Calendar calendar) {
		this.data = calendar;
		this.controller = controller;

		monthLabel = new SmoothComboBox<>(litheraa.data.calendar.Calendar.MONTH_NAME, Calendar.getTodayMonth() - 1);
		monthLabel.addItemListener(new ComboBoxListener());

		yearLabel = new SmoothComboBox<>(controller.getCalendarYears(), - 1);
		yearLabel.addItemListener(new ComboBoxListener());

		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(((ThemeColors) ViewController.getTheme()).getAccentBackground());
		headerPanel.add(monthLabel);
		headerPanel.add(yearLabel);
		add(headerPanel);

		SpringLayout sL = new SpringLayout();
		sL.putConstraint(SpringLayout.EAST, monthLabel, -4, SpringLayout.HORIZONTAL_CENTER, headerPanel);
		sL.putConstraint(SpringLayout.HEIGHT, monthLabel, 0, SpringLayout.HEIGHT, headerPanel);
		sL.putConstraint(SpringLayout.WEST, yearLabel, 4, SpringLayout.HORIZONTAL_CENTER, headerPanel);
		sL.putConstraint(SpringLayout.HEIGHT, yearLabel, 0, SpringLayout.HEIGHT, headerPanel);
		headerPanel.setLayout(sL);

		JPanel dayNamesPanel = new JPanel(new GridLayout(1, 7, 5, 0));
		String[] dayNames = new String[]{"Понедельник", "Вторник", "Среда", " Четверг", "Пятница", "Суббота", "Воскресенье"};
		for (int i = 0; i < 7; i++) {
			JLabel dayName = new JLabel(dayNames[i]);
			dayName.setOpaque(true);
			dayName.setBackground(((ThemeColors) ViewController.getTheme()).getForeground());
			dayName.setForeground(((ThemeColors) ViewController.getTheme()).getBackgroundDark());
			dayNamesPanel.add(dayName);
		}
		add(dayNamesPanel);

		createDaysPanel();

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.WIDTH, headerPanel, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, headerPanel, 0, SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, headerPanel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, headerPanel, 35, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, dayNamesPanel, 1, SpringLayout.SOUTH, headerPanel);
		layout.putConstraint(SpringLayout.SOUTH, dayNamesPanel, 25, SpringLayout.SOUTH, headerPanel);
		layout.putConstraint(SpringLayout.WIDTH, dayNamesPanel, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.WIDTH, daysPanel, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.NORTH, daysPanel, 1, SpringLayout.SOUTH, dayNamesPanel);
		layout.putConstraint(SpringLayout.SOUTH, daysPanel, 0, SpringLayout.SOUTH, this);
		setLayout(layout);

		build();
	}

	private void createDaysPanel() {
		daysPanel = new DaysPanel(data.getWeeks(), 7, 5, 5);
		daysPanel.setSize(getDaysPanelSize(controller.getWindowSize(ViewType.CALENDAR.ordinal())));
		add(daysPanel);
	}

	private void createEmptyDays() {
		for (int i = 1; i < data.getFirstDay(); i++) {
			JPanel emptyDay = new JPanel();
			emptyDay.setVisible(false);
			daysPanel.add(emptyDay);
		}
	}

	private void createDays() {
		for (int day = 1; day <= data.getDays(); day++) {
			ProgressContainer container = new ProgressContainer(data.getProgress(day), data.getDayGoal(day));
			container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			container.createVerticalProgress(day);
			days.add(container);
			daysPanel.add(days.get(day - 1));
		}
	}

	private void setData(litheraa.data.calendar.Calendar data) {
		this.data = data;
	}

	private void clearView() {
		if (!days.isEmpty()) {
			days.clear();
		}
		if (!daysPanel.isEmpty()) {
			daysPanel.clear();
		}
	}

	public void build() {
		createEmptyDays();
		createDays();
		for (ProgressContainer container : days) {
			container.adjustInnerComponentsSize(daysPanel.getDaySize(getDaysPanelSize(controller.getWindowSize(ViewType.CALENDAR.ordinal()))));
		}
	}

	private Dimension getDaysPanelSize(Dimension containerSize) {
		return new Dimension(containerSize.width - 20, containerSize.height - 121);
	}

	private class ComboBoxListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			clearView();
			setData(controller.getCalendarData(Objects.requireNonNull((Integer) yearLabel.getSelectedItem()),
					monthLabel.getSelectedIndex() + 1));
			daysPanel.setRows(data.getWeeks());
			build();
			controller.repaint();
		}
	}
}
