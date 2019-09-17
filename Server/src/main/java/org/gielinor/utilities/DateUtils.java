package org.gielinor.utilities;

import java.util.Calendar;
import java.util.Locale;

/**
 * A utility for date operations.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DateUtils {

    /**
     * Gets a formattable time. (Jan(uary) 1, 2016 12:00 AM)
     *
     * @param timeMillis The time in millis.
     * @return The time.
     */
    public static String getTime(long timeMillis, boolean fullMonthName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        String monthName = calendar.getDisplayName(Calendar.MONTH, fullMonthName ? Calendar.LONG : Calendar.SHORT, Locale.US);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String time = String.format("%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)) + " " + (calendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM");
        return monthName + " " + day + ", " + year + " " + time;
    }
}
