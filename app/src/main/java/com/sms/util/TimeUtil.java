package com.sms.util;

/*
 * sms
 * Created by A.Kolchev  26.2.2016
 */

import android.content.Context;
import android.text.format.DateUtils;
import android.text.format.Time;

import com.sms.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    private static final int ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

    /**
     * @param delayInSeconds int in seconds
     * @return time in milliseconds computed as (now + delay)
     */
    public static long getTimeWithDelay(int delayInSeconds) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, delayInSeconds);
        return c.getTimeInMillis();
    }

    public static class Pattern {
        /**
         * "HH:mm"
         */
        public static final String TIME = "HH:mm";

        /**
         * "dd MMMM, yyyy"
         */
        public static final String DATE = "dd MMMM, yyyy";

        /**
         * "dd MMMM, yyyy, HH:mm"
         */
        public static final String DATETIME = DATE + ", " + TIME;

        /**
         * "EEEE, dd MMMM, yyyy" (includes day of week).
         */
        public static final String FULL_DATE = "EEEE, " + DATE;

        /**
         * "EEEE, dd MMMM, yyyy, HH:mm" (includes day of week).
         */
        public static final String FULL_DATETIME = "EEEE, " + DATETIME;

        /**
         * dd MMMM, yyyy, HH:mm" (Data Base Date Format).
         */
        public static final String DB_DATETIME = "yyyy-MM-dd HH:mm:ss";

    }

    /**
     * @return SimpleDateFormat with {@link Pattern#TIME}.
     */
    public static SimpleDateFormat createTimeFormatter() {
        return new SimpleDateFormat(Pattern.TIME, Locale.getDefault());
    }

    /**
     * @return SimpleDateFormat with {@link Pattern#DATE}.
     */
    public static SimpleDateFormat createDateFormatter() {
        return new SimpleDateFormat(Pattern.DATE, Locale.getDefault());
    }

    /**
     * @return SimpleDateFormat with {@link Pattern#DATETIME}.
     */
    public static SimpleDateFormat createDateTimeFormatter() {
        return new SimpleDateFormat(Pattern.DATETIME, Locale.getDefault());
    }

    /**
     * @return SimpleDateFormat with {@link Pattern#FULL_DATE}.
     */
    public static SimpleDateFormat createFullDateFormatter() {
        return new SimpleDateFormat(Pattern.FULL_DATE, Locale.getDefault());
    }

    /**
     * @return SimpleDateFormat with {@link Pattern#FULL_DATETIME}.
     */
    public static SimpleDateFormat createFullDateTimeFormatter() {
        return new SimpleDateFormat(Pattern.FULL_DATETIME, Locale.getDefault());
    }

    /**
     * @return SimpleDateFormat with specified format.
     */
    public static SimpleDateFormat createCustomDateTimeFormatter(String format) {
        return new SimpleDateFormat(format, Locale.getDefault());
    }

    /**
     * @return SimpleDateFormat with {@link Pattern#DB_DATETIME}.
     */
    public static SimpleDateFormat createDBDateTimeFormatter() {
        return new SimpleDateFormat(Pattern.DB_DATETIME, Locale.getDefault());
    }

    /**
     * If date represents today, then "Today" (localized) is returned.<br />
     * If date represents tomorrow, then "Tomorrow" (localized) is returned.<br />
     * In other cases formatter if used to get the result.
     *
     * @param formatter SimpleDateFormat
     * @param context   Context
     * @param date      Date
    */
    public static String format(SimpleDateFormat formatter, Context context, Date date) {
        if (isToday(date)) {
            return context.getString(R.string.today);
        } else if (isTomorrow(date)) {
            return context.getString(R.string.tomorrow);
        } else {
            //return StringUtil.capitalize(formatter.format(date));
            return formatter.format(date);
        }
    }

    /**
     * If date represents today, then "Today" (localized) is returned.<br />
     * If date represents tomorrow, then "Tomorrow" (localized) is returned.<br />
     * In other cases formatter if used to get the result.
     *
     * @param formatter SimpleDateFormat
     * @param date      Date
     */
    public static String format(SimpleDateFormat formatter, Date date) {
        if (date!=null){
            //return StringUtil.capitalize(formatter.format(date));
            return formatter.format(date);
        }
        return "";
    }

    /**
     * @param formatter SimpleDateFormat
     * @param date_string String
     * @return Date.
     */
    public static Date format(SimpleDateFormat formatter, String date_string) {
        Date date = null;
        if (!date_string.equals("")) {
            try {
                date = formatter.parse(date_string);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }


    /**
     * @return true if both d1 and d2 represent the same date, <b>without</b> taking
     * under account the time fraction (hh:mm:ss.sss). False otherwise.
     */
    public static boolean isSameDate(Date d1, Date d2) {
        Time time = new Time();
        time.set(d1.getTime());

        int d1Year = time.year;
        int d1YearDay = time.yearDay;

        time.set(d2.getTime());

        return (d1YearDay == time.yearDay) && (d1Year == time.year);
    }

    public static boolean isToday(Date date) {
        return DateUtils.isToday(date.getTime());
    }


    /**
     * Check if date is tomorrow
     *
     * @param date
     * @return true if given date is tomorrow
     */
    public static boolean isTomorrow(Date date) {
        Time time = new Time();
        time.set(System.currentTimeMillis() + ONE_DAY_IN_MILLIS); // tomorrow

        int tomorrowYear = time.year;
        int tomorrowYearDay = time.yearDay;

        time.set(date.getTime());

        return (tomorrowYearDay == time.yearDay) && (tomorrowYear == time.year);
    }

    /**
     * Checks if given date is yesterday
     *
     * @param date
     * @return
     */
    public static boolean isYesterday(Date date) {
        Time time = new Time();
        time.set(System.currentTimeMillis() - ONE_DAY_IN_MILLIS); // yesterday

        int tomorrowYear = time.year;
        int tomorrowYearDay = time.yearDay;

        time.set(date.getTime());

        return (tomorrowYearDay == time.yearDay) && (tomorrowYear == time.year);
    }

    /**
     * Creates day of week string representation
     *
     * @param context
     * @param date
     * @return formatted day of week string
     */
    public static String getHeaderDayOfweek(Context context, Date date) {
        StringBuilder stringBuilder = new StringBuilder();
        if (isToday(date)) {
            stringBuilder.append(context.getString(R.string.today));
            stringBuilder.append(", ");
        } else if (isTomorrow(date)) {
            stringBuilder.append(context.getString(R.string.tomorrow));
            stringBuilder.append(", ");
        } else if (isYesterday(date)) {
            stringBuilder.append(context.getString(R.string.yesterday));
            stringBuilder.append(", ");
        }
        String formattedDate = new SimpleDateFormat("EEEEEEEEE", Locale.getDefault()).format(date);
        stringBuilder.append(Character.toUpperCase(formattedDate.charAt(0)) + formattedDate.substring(1));
        return stringBuilder.toString();
    }

    /**
     * Calculates no of hours beetween given dates
     *
     * @param startDate
     * @param endDate
     * @return hours beetwen
     */
    public static long hoursBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        Calendar date1 = (Calendar) endDate.clone();
        date1.set(Calendar.MINUTE, 0);
        date1.set(Calendar.SECOND, 0);
        date1.set(Calendar.MILLISECOND, 0);

        if (date.get(Calendar.YEAR) - date1.get(Calendar.YEAR) > 0) {

        }
        int power = 1;
        if (date1.get(Calendar.YEAR) - date.get(Calendar.YEAR) > 0) {
            power = 365 * (date1.get(Calendar.YEAR) - date.get(Calendar.YEAR));
        } else if (date1.get(Calendar.MONTH) - date.get(Calendar.MONTH) > 0) {
            power = 30 * (date1.get(Calendar.MONTH) - date.get(Calendar.MONTH));
        }
        long hoursBetween = 0;
        while (date.before(date1)) {
            date.add(Calendar.HOUR_OF_DAY, power);
            if (date.after(date1)) {
                date.add(Calendar.HOUR_OF_DAY, -power);
                power = Double.valueOf(power / 2).intValue();
                continue;
            }
            hoursBetween = hoursBetween + power;
        }
        return hoursBetween;
    }

    /**
     * Format duration in milliseconds to hh:mi:ss
     * @param millis  Duration in milliseconds
     * @return duration fromatted to in hh:mi:ss or empty string if duration ==0
     */
    public static String durationToString(long millis) {
        //hh:mm:ss
        if (millis > 0) {
            long hours = TimeUnit.MILLISECONDS.toHours(millis);
            if (hours>0){
            return String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)), Locale.getDefault());
            } else {
                return String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millis) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)), Locale.getDefault());
            }
        } else return "";
    }


    public static long truncateToDateStart(long dateInMillis){
        Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(dateInMillis);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        return calendar.getTimeInMillis();
    }

    public static long truncateToDateEnd(long dateInMillis){
        Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(dateInMillis);
        calendar.set(Calendar.MILLISECOND,999);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        return calendar.getTimeInMillis();
    }

}
