package litheraa.view.calendar;

import litheraa.controller.ViewController;
import litheraa.data.models.ProdTimeModel;
import litheraa.view.themes.ThemeColors;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class CalendarPanel extends JPanel {
	private CalendarGrid calendarGrid;
	private ProdTimeModel prodTimeModel;
	private final ViewController controller;
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
		headerName.setBackground(((ThemeColors) ViewController.getTheme()).getAccentBackground());

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

	private void setCalendar(ProdTimeModel prodTimeModel) {
		this.prodTimeModel = prodTimeModel;
	}

	private void clearView() {
		if (!calendarGrid.isEmpty()) {
			calendarGrid.removeAll();
		}
	}

	public void build() {
		JPanel dayNames = createDayNamesPanel();
		calendarGrid = new CalendarGrid(prodTimeModel.getWeeks(), 5, 5,
				prodTimeModel.getFirstDay(), prodTimeModel.getLastDay());

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, header, 0, SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.WIDTH, header, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.NORTH, header, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, header, 35, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, dayNames, 1, SpringLayout.SOUTH, header);
		layout.putConstraint(SpringLayout.SOUTH, dayNames, 25, SpringLayout.SOUTH, header);
		layout.putConstraint(SpringLayout.WIDTH, dayNames, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.WIDTH, calendarGrid, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.NORTH, calendarGrid, 1, SpringLayout.SOUTH, dayNames);
		layout.putConstraint(SpringLayout.SOUTH, calendarGrid, 0, SpringLayout.SOUTH, this);
		setLayout(layout);


		DayPanelController dayPanelController = new DayPanelController(prodTimeModel);
		dayPanelController.fullDayPanel();

		add(header);
		add(dayNames);
		add(calendarGrid);
	}

	private Dimension calculateSize() {
		return new Dimension(containerSize.width - 20, containerSize.height - 121);
	}
}
