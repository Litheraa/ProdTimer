package litheraa.util;

import litheraa.data.entities.Time;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CalendarWrapper {
    private static final String SEPARATOR = "-";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public static Date wrapToDate(@NotNull String dateString) {
        String[] date = dateString.split(SEPARATOR);
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DATE, Integer.parseInt(date[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(date[2]));
        return calendar.getTime();
    }

    public static java.sql.Date wrapToSQLDate(String dateString) {
        String[] date = dateString.split(SEPARATOR);
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DATE, Integer.parseInt(date[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(date[2]));
        return new java.sql.Date(calendar.getTimeInMillis());
    }

    public static String wrapToString(@NotNull Date date) {
        return DATE_FORMAT.format(date);
    }

    public static int getYear(String date) {
        return Integer.parseInt(date.split(SEPARATOR)[0]);
    }

    public static boolean isSameMonth(Time time, YearMonth month) {
        LocalDate date = time.getModified();
        return YearMonth.of(date.getYear(), date.getMonth()).equals(month);
    }

    public static String localeRu(Month month) {
        return month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru"));
    }

    public static String localeRu(YearMonth yearMonth) {
        return localeRu(yearMonth.getMonth()) + " " + yearMonth.getYear();
    }

    public static String localeRu(LocalDate date) {
        return date.getDayOfMonth() + " " + localeRu(YearMonth.of(date.getYear(),date.getMonth()));
    }

    public static String localeRu(Date date) {
        LocalDate d = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
        if (d.getDayOfMonth() < 10) {
            return 0 + localeRu(d);
        }
        return localeRu(d);
    }

    public static int getWeeks(YearMonth month) {
        int weeks = 1;
	    for (int i  = 1; i < month.lengthOfMonth() + (7 - month.atEndOfMonth().getDayOfWeek().ordinal()); i += 7) {
            weeks++;
        }
        return --weeks;
    }

    public static Month deLocaleMonth(String month) {
        String m = month.trim().toLowerCase();
        return switch (m) {
            case "январь" -> Month.JANUARY;
            case "февраль" -> Month.FEBRUARY;
            case "март" -> Month.MARCH;
            case "апрель" -> Month.APRIL;
            case "май" -> Month.MAY;
            case "июнь" -> Month.JUNE;
            case "июль" -> Month.JULY;
            case "август" -> Month.AUGUST;
            case "сентябрь" -> Month.SEPTEMBER;
            case "октябрь" -> Month.OCTOBER;
            case "ноябрь" -> Month.NOVEMBER;
            case "декабрь" -> Month.DECEMBER;
	        default -> throw new IllegalStateException("Unexpected value: " + month);
        };
    }
}
