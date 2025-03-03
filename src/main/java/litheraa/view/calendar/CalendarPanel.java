package litheraa.view.calendar;

import litheraa.controller.ViewController;
import litheraa.data.models.ProdTimeModel;
import litheraa.view.themes.ThemeColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class CalendarPanel extends JPanel {
	private CalendarGrid calendarGrid;
	private ProdTimeModel prodTimeModel;
	private final ViewController controller;
	private final ArrayList<ProgressContainer> days = new ArrayList<>();
	private final JPanel header;
	private final Dimension containerSize;

	public CalendarPanel(ViewController controller, ProdTimeModel prodTimeModel, Dimension containerSize) {
		this.prodTimeModel = prodTimeModel;
		this.controller = controller;
		this.containerSize = containerSize;

		header = new JPanel();

		header.setBackground(((ThemeColors) ViewController.getTheme()).getAccentBackground());
		setHeaderName(prodTimeModel);

		build();
	}

	private JPanel createDayNamesPanel() {
		JPanel dayNamesPanel = new JPanel(new GridLayout(1, 7, 5, 0));
		for (int i = 0; i < 7; i++) {
			JLabel dayName = new JLabel(DayOfWeek.of(i + 1).getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru", "RU")));
			dayName.setOpaque(true);
			dayName.setBackground(((ThemeColors) ViewController.getTheme()).getForeground());
			dayName.setForeground(((ThemeColors) ViewController.getTheme()).getBackgroundDark());
			dayNamesPanel.add(dayName);
		}
		return dayNamesPanel;
	}

	private void setHeaderName(ProdTimeModel prodTimeModel) {
		JLabel headerName = new JLabel();
		headerName.setFont(new Font("Aerial", Font.BOLD, 26));

		LocalDate from = prodTimeModel.getFrom();
		LocalDate to = prodTimeModel.getTo();

//TODO may be in some sort of algorithm
		String format = "dd MMMM yyyy";
		if (from.getMonth() == to.getMonth() &&
				from.getYear() == to.getYear()) {
			headerName.setText(from.format(DateTimeFormatter.ofPattern(format.substring(2))));
		} else {
			headerName.setText(from.format(DateTimeFormatter.ofPattern(format)) +
					" — " +
					to.format(DateTimeFormatter.ofPattern(format)));
		}

		SpringLayout sL = new SpringLayout();
		sL.putConstraint(SpringLayout.HORIZONTAL_CENTER, headerName, 0, SpringLayout.HORIZONTAL_CENTER, header);

		header.setLayout(sL);
		header.add(headerName);
	}

	/// GritLayout in which days located ignores number of columns (days in week) if rows are set. ///
	/// To prevent weeks with 6 days im forced to add some empty days in the end of the month      ///
	private void fillUp(JPanel dayPanel) {
		int i = 1;
		for (Integer integer : prodTimeModel.getDates()) {
			ProgressContainer container = new ProgressContainer(prodTimeModel, integer);
			container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			container.createVerticalProgress(controller);
//			days.add(container);
			calendarGrid.add(container);
		}
	}

	private void setCalendar(ProdTimeModel prodTimeModel) {
		this.prodTimeModel = prodTimeModel;
	}

	private void clearView() {
//		if (!days.isEmpty()) {
//			days.clear();
//		}
		if (!calendarGrid.isEmpty()) {
			calendarGrid.removeAll();
		}
	}

	public void build() {
		JPanel dayNames = createDayNamesPanel();
		calendarGrid = new CalendarGrid(prodTimeModel.getWeeks(), 5, 5,
				prodTimeModel.getFirstDay(), prodTimeModel.getLastDay());

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.WIDTH, header, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, header, 0, SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, header, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, header, 35, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, dayNames, 1, SpringLayout.SOUTH, header);
		layout.putConstraint(SpringLayout.SOUTH, dayNames, 25, SpringLayout.SOUTH, header);
		layout.putConstraint(SpringLayout.WIDTH, dayNames, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.WIDTH, calendarGrid, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.NORTH, calendarGrid, 1, SpringLayout.SOUTH, dayNames);
		layout.putConstraint(SpringLayout.SOUTH, calendarGrid, 0, SpringLayout.SOUTH, this);
		setLayout(layout);

		add(header);
		add(dayNames);
		add(calendarGrid);
		fillUp(null);
		for (Component container : calendarGrid.getComps()) {
			((ProgressContainer) container).adjustInnerComponentsSize(calendarGrid.getDaySize(calculateThisSize()));
		}
	}

	private Dimension calculateThisSize() {
		return new Dimension(containerSize.width - 20, containerSize.height - 121);
	}

	private class ComboBoxListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			clearView();
			//noinspection DataFlowIssue
			setCalendar(/*controller.getDataByPeriod((Integer) yearLabel.getSelectedItem(),
					monthLabel.getSelectedItem().toString())*/controller.getDataByPeriod(Year.now().atDay(1), LocalDate.now()));
			calendarGrid.setRows(prodTimeModel.getWeeks());
			build();
			controller.repaint();
		}
	}
}
