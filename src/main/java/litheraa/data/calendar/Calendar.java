package litheraa.data.calendar;

import litheraa.SettingsController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class Calendar {
	@Getter
	private final int month;
	@Getter
	private final int year;
	private Map<Integer, CalendarDay> dayMap = new HashMap<>();
	private final String[] monthName = new String[]
			{"ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ", "ИЮЛЬ", "АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"};

	public Calendar(int year, int month) {
		this.year = year;
		this.month = month;
	}

	public String getMonthName() {
		return monthName[month - 1];
	}

	public void setDayMap(int day, int dayGoal, double dayProgress, String textNames) {
		dayMap.put(day, new CalendarDay(dayGoal, dayProgress, textNames));
	}

	public void setDayGoal(int day, int dayGoal) {
		CalendarDay calendarDay = dayMap.getOrDefault(day, CalendarDay.getDefault());
		calendarDay.setGoal(dayGoal);
		dayMap.put(day, calendarDay);
	}

	public void setDayProgress(int day, double progress) {
		CalendarDay calendarDay = dayMap.getOrDefault(day, CalendarDay.getDefault());
		calendarDay.setProgress(progress);
		dayMap.put(day, calendarDay);
	}

	public void setTextNames(int day, String textNames) {
		CalendarDay calendarDay = dayMap.getOrDefault(day, CalendarDay.getDefault());
		calendarDay.setTextNames(textNames);
		dayMap.put(day, calendarDay);
	}

	public int getDayGoal(int day) {
		return dayMap.getOrDefault(day, CalendarDay.getDefault()).getGoal();
	}

	public double getProgress(int day) {
		return dayMap.getOrDefault(day, CalendarDay.getDefault()).getProgress();
	}

	public String getTextNames(int day) {
		return dayMap.getOrDefault(day, CalendarDay.getDefault()).getTextNames();
	}

	@Getter(AccessLevel.PRIVATE)
	private static class CalendarDay {
		@Setter(AccessLevel.PRIVATE)
		private int goal;
		@Setter(AccessLevel.PRIVATE)
		private double progress;
		@Setter(AccessLevel.PRIVATE)
		private String textNames;
		private static final int defaultGoal = SettingsController.getProdVolume();

		private CalendarDay(int goal, double progress, String textNames) {
			this.goal = goal;
			this.progress = progress;
			this.textNames = textNames;
		}

		private static CalendarDay getDefault() {
			return new CalendarDay(defaultGoal, 0.0, "");
		}
	}
}
