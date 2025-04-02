package litheraa.view.calendar;

import litheraa.controller.SettingsController;
import litheraa.controller.ViewController;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import litheraa.util.CalendarWrapper;
import org.apache.commons.math3.util.Pair;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class WeeklyCalendarController extends MonthlyCalendarController {
	private final LocalDate date = SettingsController.getPeriod();

	public WeeklyCalendarController(ViewController controller, Pair<List<Time>, List<Text>> dataPair) {
		super(controller, dataPair);
	}

	@Override
	public LocalDate getStart() {
		return date.minusDays(date.getDayOfWeek().ordinal());
	}

	@Override
	public LocalDate getEnd() {
		return date.plusDays(6 - date.getDayOfWeek().ordinal());
	}

	@Override
	public int getRows() {
		return 1;
	}

	@Override
	public String getHeaderText() {
		return (date.getDayOfMonth() / 7) + 1
				+ " неделя " + CalendarWrapper.localeRu(YearMonth.of(date.getYear(), date.getMonth()))
				+ " : "
				+ calendarModel.getTextName();
	}
}
