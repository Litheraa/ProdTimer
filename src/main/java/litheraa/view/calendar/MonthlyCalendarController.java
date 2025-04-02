package litheraa.view.calendar;

import litheraa.controller.SettingsController;
import litheraa.controller.ViewController;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import litheraa.data.models.CalendarModel;
import litheraa.util.CalendarWrapper;
import litheraa.view.TableDialog;
import litheraa.view.calendar.calendar_settings.CalendarSettings;
import litheraa.view.themes.ThemeColors;
import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.fabric.*;
import org.apache.commons.math3.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;

public class MonthlyCalendarController implements CalendarControllerInterface {
	private JPanel mainPanel;
	private final YearMonth month = YearMonth.of(SettingsController.getPeriod().getYear(), SettingsController.getPeriod().getMonth());
	protected final CalendarModel calendarModel;
	private final Map<LocalDate, AdjustableComponentInterface> dayPanels = new HashMap<>(31);
	protected final FontFactory fontFactory = FontFactory.getInstance();
	private final IconFactory iconFactory = IconFactory.getInstance();
	private final ViewController controller;

	public MonthlyCalendarController(ViewController controller, Pair<List<Time>, List<Text>> dataPair) {
		this.controller = controller;
		this.calendarModel = new CalendarModel(dataPair);
	}

	private AdjustablePanel createHeader(String headerText, String headerIcon) {
		JLabel label = new JLabel(headerText.substring(0, 1).toUpperCase() + headerText.substring(1));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new TableDialog(e.getPoint()).createDayChooserDialog(
						"Укажите интервал",
						Month.of(SettingsController.getMonth())
								.getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")),
						new SettingsController(),
						(sC, string) -> SettingsController.setMonth(string.substring(3, 5)));
			}
		});

		JLabel icon = new JLabel("");
		icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		setLabelPopUp(icon);

		AdjustablePanel panel = AdjustablePanel.adjustablePanelBuilder(LocalDate.of(1970, 1, 1))
				.label((l, step) -> l.setFont(fontFactory
						.getFont("header", step, 8, Font.BOLD)), new Pair<>(label, BorderLayout.CENTER))
				.label((l, step) -> l.setIcon(iconFactory.getIcon(headerIcon, step, 8)), new Pair<>(icon, BorderLayout.EAST))
				.layout(new BorderLayout())
				.build();

		panel.add(label, BorderLayout.CENTER);
		panel.add(icon, BorderLayout.EAST);

		panel.setBackground(((ThemeColors) ViewController.getTheme()).getAccentBackground());
		return panel;
	}

	protected AdjustablePanel createSubHeader() {
		JLabel[] labels = new JLabel[7];
		for (DayOfWeek day : DayOfWeek.values()) {
			JLabel label = new JLabel(day.getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")));
			label.setOpaque(true);
			label.setForeground(((ThemeColors) ViewController.getTheme()).getBackgroundDark());
			label.setBackground(((ThemeColors) ViewController.getTheme()).getForeground());
			label.setHorizontalAlignment(JLabel.CENTER);
			labels[day.getValue() - 1] = label;
		}

		return AdjustablePanel.adjustablePanelBuilder(LocalDate.of(1970, 1, 2))
				.layout(new GridLayout(1, 7, 5, 0))
				.label((l, step) ->
						l.setFont(fontFactory.getFont("subHeader", step, 4, Font.PLAIN)), labels)
				.build();
	}

	public JPanel createGrid(int rows) {
		return new CalendarGrid(rows, 5, 5,
				getStart(), getEnd());
	}

	@Override
	public JPanel build() {
		JPanel dayGrid = createGrid(getRows());

		boolean firstDayPanel = true;

		for (LocalDate id = getStart(); id.isBefore(getEnd().plusDays(1L)); id = id.plusDays(1L)) {
			AdjustablePanel adjustablePanel = AdjustablePanel.dayPanelbuilder(id)
					.dayLabel()
					.labelGroup(calendarModel.getWritten(id), calendarModel.getGoal(id))
					.progressBar(calendarModel.getWritten(id), calendarModel.getGoal(id))
					.build();
			if (firstDayPanel) {
				adjustablePanel.addSizeStepListener(this);
				adjustablePanel.addComponentListener(new AspectRatioAdapter(this));
				firstDayPanel = false;
			}
			dayGrid.add(adjustablePanel);
			dayPanels.put(id, adjustablePanel);
		}

		mainPanel = new JPanel();
		AdjustablePanel header = createHeader(getHeaderText(), "gear.png");
		AdjustablePanel subHeader = createSubHeader();

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.WIDTH, header, 0, SpringLayout.WIDTH, mainPanel);
		layout.putConstraint(SpringLayout.NORTH, header, 0, SpringLayout.NORTH, mainPanel);
		layout.putConstraint(SpringLayout.NORTH, subHeader, 1, SpringLayout.SOUTH, header);
		layout.putConstraint(SpringLayout.WIDTH, subHeader, 0, SpringLayout.WIDTH, mainPanel);
		layout.putConstraint(SpringLayout.WIDTH, dayGrid, 0, SpringLayout.WIDTH, mainPanel);
		layout.putConstraint(SpringLayout.NORTH, dayGrid, 1, SpringLayout.SOUTH, subHeader);
		layout.putConstraint(SpringLayout.SOUTH, dayGrid, 0, SpringLayout.SOUTH, mainPanel);

		mainPanel.setLayout(layout);

		mainPanel.add(header);
		mainPanel.add(subHeader);
		mainPanel.add(dayGrid);

		dayPanels.put(header.getId(), header);
		dayPanels.put(subHeader.getId(), subHeader);
		return mainPanel;
	}

	@Override
	public void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio) {
		dayPanels.values().forEach(dayPanel -> dayPanel.aspectRatioChanged(ratio));
	}

	@Override
	public void sizeChanged(SizeStepAdapter.Step step) {
		dayPanels.values().forEach(dayPanel -> dayPanel.sizeChanged(step));
	}

	@Override
	public JComponent setParent(JComponent parent) {
		return null;
	}

	@Override
	public void setGoal(int goal, Long timeId) {

	}

	@Override
	public void setView(ViewController.CalendarType type) {
		controller.buildCalendar(type);
	}

	@Override
	public void setTextId(String text) {
		calendarModel.setTextId(text);
		mainPanel.validate();
	}

	@Override
	public LocalDate getStart() {
		return month.atDay(1);
	}

	@Override
	public LocalDate getEnd() {
		return month.atEndOfMonth();
	}

	@Override
	public int getRows() {
		return CalendarWrapper.getWeeks(month);
	}

	@Override
	public String getHeaderText() {
		return CalendarWrapper.localeRu(month)
				+ " : "
				+ calendarModel.getTextName();
	}

	private void setLabelPopUp(JLabel label) {
			JDialog dialog = new CalendarSettings(this, CalendarWrapper.localeRu(month), calendarModel.getTextNames());

		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!dialog.isVisible()) {
					Point p = e.getPoint();
					SwingUtilities.convertPointToScreen(p, label);
					dialog.setLocation(p);
					dialog.setVisible(true);
				} else {
					dialog.setVisible(false);
				}
			}
		});
	}

}
