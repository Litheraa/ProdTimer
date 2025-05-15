package litheraa.view.calendar;

import litheraa.controller.CalendarController;
import litheraa.data.models.CalendarModel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class DailyCalendarPanel extends CalendarPanel {
	private final LocalDate date;

	public DailyCalendarPanel(CalendarController controller, LocalDate period, CalendarModel calendarModel) {
		super(controller, calendarModel);
		date = period;
		build();
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
	public JPanel createGrid() {
		return new JPanel(new GridLayout());
	}

	@Override
	protected AdjustablePanel createSubHeader() {
		JLabel label = new JLabel(date.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")));
		label.setOpaque(true);
//		label.setForeground(colors.getBackgroundDark());
//		label.setBackground(colors.getForeground());
		label.setHorizontalAlignment(JLabel.CENTER);

		//		panel.add(label, BorderLayout.CENTER);

		return AdjustablePanel.adjustablePanelBuilder(LocalDate.of(1970, 1, 2))
				.label((l, step) ->
						l.setFont(fontFactory.getFont("subHeader", step, 4, Font.PLAIN)), label, BorderLayout.CENTER)
				.layout(new BorderLayout())
				.build();
	}
}
