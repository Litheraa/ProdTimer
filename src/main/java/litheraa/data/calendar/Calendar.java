package litheraa.data.calendar;

import litheraa.SettingsController;
import litheraa.util.CalendarWrapper;
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
	private final int lastDay;
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
		days = calculateDays();
		firstDay = calculateFirstDay();
		lastDay = calculateLastDay();
		weeks = calculateWeeks();
	}

	public Calendar(String date) {
		this(CalendarWrapper.getYear(date), CalendarWrapper.getMonth(date));
	}

	private int calculateFirstDay() {
		int tempFirsDay = (calendar.get(java.util.Calendar.DAY_OF_WEEK) + 6) % 7;
		return tempFirsDay == 0 ? 7 : tempFirsDay;
	}

	private int calculateLastDay() {
		int tempLastDay;
		boolean isSmallMonth = days % 2 == 0;
		if (month != 2) {
			if (isSmallMonth) {
				tempLastDay = firstDay + 1;
			} else {
				tempLastDay = firstDay + 2;
			}
		} else {
			if (isSmallMonth) {
				tempLastDay = firstDay - 1;
			} else {
				tempLastDay = firstDay;
			}
		}
		tempLastDay = tempLastDay % 7;
		return tempLastDay == 0 ? 7 : tempLastDay;
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

	public String getDate(int day) {
		String monthString = month < 10 ? "0" + month : String.valueOf(month);
		return year + "-" + monthString + "-" + day;
	}

	public void setDayGoal(int day, int dayGoal) {
		CalendarDay calendarDay = dayMap.getOrDefault(day, CalendarDay.getDefault());
		dayGoal = dayGoal == 0 ? CalendarDay.defaultGoal : dayGoal;
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
		return dayMap.getOrDefault(day, CalendarDay.getDefault()).getTextNames().replace("/", System.lineSeparator());
	}

	public static int getTodayMonth() {
		return LocalDate.now().getMonthValue();
	}

	public static int getMonthNo(String month) {
		for (int i = 0; i < MONTH_NAME.length; i++) {
			if (MONTH_NAME[i].contentEquals(month)) {
				return i + 1;
			}
		}
		return getTodayMonth();
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
			return new CalendarDay(defaultGoal, 0.0, "Вы не работали!");
		}
	}
}
