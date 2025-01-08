package litheraa.util;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarWrapper {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public static Date wrapToDate(@NotNull String dateString) {
        String[] date = dateString.split("-");
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DATE, Integer.parseInt(date[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(date[2]));
        return calendar.getTime();
    }

    public static java.sql.Date wrapToSQLDate(String dateString) {
        String[] date = dateString.split("-");
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DATE, Integer.parseInt(date[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(date[2]));
        return new java.sql.Date(calendar.getTimeInMillis());
    }

    public static String wrapToString(@NotNull Date date) {
        return DATE_FORMAT.format(date);
    }
}
