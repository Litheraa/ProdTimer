package litheraa.view.calendar;

import litheraa.controller.SettingsController;
import litheraa.controller.ViewController;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import litheraa.util.CalendarWrapper;
import litheraa.view.themes.ThemeColors;
import org.apache.commons.math3.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class DailyCalendarController extends MonthlyCalendarController{
	private final LocalDate date = SettingsController.getPeriod();

	public DailyCalendarController(ViewController controller, Pair<List<Time>, List<Text>> dataPair) {
		super(controller, dataPair);
	}

	@Override
	public void setGoal(int goal, Long timeId) {

	}

	@Override
	public LocalDate getStart() {
		return date;
	}

	@Override
	public LocalDate getEnd() {
		return date;
	}

	@Override
	public int getRows() {
		return 1;
	}

	@Override
	public String getHeaderText() {
		return CalendarWrapper.localeRu(date)
				+ " : "
				+ calendarModel.getTextName();
	}

	@Override
	public JPanel createGrid(int rows) {
		return new JPanel(new GridLayout());
	}

	@Override
	protected AdjustablePanel createSubHeader() {
		JLabel label = new JLabel(date.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")));
			label.setOpaque(true);
			label.setForeground(((ThemeColors) ViewController.getTheme()).getBackgroundDark());
			label.setBackground(((ThemeColors) ViewController.getTheme()).getForeground());
			label.setHorizontalAlignment(JLabel.CENTER);

		AdjustablePanel panel = AdjustablePanel.adjustablePanelBuilder(LocalDate.of(1970, 1, 2))
				.label((l, step) ->
						l.setFont(fontFactory.getFont("subHeader", step, 4, Font.PLAIN)), new Pair<>(label, BorderLayout.CENTER))
				.layout(new BorderLayout())
				.build();
		panel.add(label, BorderLayout.CENTER);

		return panel;
	}
}
