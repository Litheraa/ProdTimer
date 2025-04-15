package litheraa.view.calendar;

import litheraa.controller.CalendarController;
import litheraa.data.models.CalendarModel;
import litheraa.util.CalendarWrapper;

import javax.swing.*;
import java.time.LocalDate;
import java.time.YearMonth;

public class MonthlyCalendarPanel extends CalendarPanel {
	private final YearMonth month;

	public MonthlyCalendarPanel(CalendarController controller, LocalDate period, CalendarModel calendarModel) {
		super(controller, calendarModel);
		month = YearMonth.of(period.getYear(), period.getMonth());
		build();
	}

	protected JPanel createGrid() {
		return new CalendarGrid(CalendarWrapper.getWeeks(month), 5, 5,
				getStart(), getEnd());
	}

	@Override
	public void setGoal(int goal, Long timeId) {

	}

	@Override
	public LocalDate getStart() {
		return month.atDay(1);
	}

	@Override
	public LocalDate getEnd() {
		return month.atEndOfMonth();
	}
}
