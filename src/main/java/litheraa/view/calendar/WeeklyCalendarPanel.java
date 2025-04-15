package litheraa.view.calendar;

import litheraa.controller.CalendarController;
import litheraa.data.models.CalendarModel;

import javax.swing.*;
import java.time.LocalDate;

public class WeeklyCalendarPanel extends CalendarPanel {
	private final LocalDate date;

	public WeeklyCalendarPanel(CalendarController controller, LocalDate date, CalendarModel calendarModel) {
		super(controller, calendarModel);
		this.date = date;
		build();
	}

	@Override
	public JPanel createGrid() {
		return new CalendarGrid(1, 5, 5,
				getStart(), getEnd());
	}

	@Override
	void setGoal(int goal, Long timeId) {

	}

	@Override
	public LocalDate getStart() {
		return date.minusDays(date.getDayOfWeek().ordinal());
	}

	@Override
	public LocalDate getEnd() {
		return date.plusDays(6 - date.getDayOfWeek().ordinal());
	}
}
