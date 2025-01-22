package litheraa.data.calendar;

import litheraa.SettingsController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class Calendar {
	@Getter
	private final int month;
	@Getter
	private final int year;
	@Getter
	private final int weeks;
	@Getter
	private final int firstDay;
	@Getter
	private final int days;
	private final GregorianCalendar calendar;
	private final Map<Integer, CalendarDay> dayMap = new HashMap<>();
	public static final String[] MONTH_NAME = new String[]
			{"ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ", "ИЮЛЬ", "АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"};

	public Calendar(int year, int month) {
		this.year = year;
		this.month = month;
		//noinspection MagicConstant
		calendar = new GregorianCalendar(year, month - 1, 1);
		firstDay = calculateFirstDay();
		days = calculateDays();
		weeks = calculateWeeks();
	}

	private int calculateFirstDay() {
		int tempFirsDay = (calendar.get(java.util.Calendar.DAY_OF_WEEK) + 6) % 7;
		if (tempFirsDay == 0) {
			tempFirsDay = 7;
		}
		return tempFirsDay;
	}

	private int calculateDays() {
		return calendar.getActualMaximum(java.util.Calendar.DATE);
	}

	private int calculateWeeks() {
		return (int) Math.ceil((days - (8 - firstDay)) / 7.0) + 1;
	}

	public String getMONTH_NAME() {
		return MONTH_NAME[month - 1];
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

	public static int getTodayMonth() {
		return LocalDate.now().getMonthValue();
	}

	public static int getTodayYear() {
		return LocalDate.now().getYear();
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
