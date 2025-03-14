package com.example.livestream_update.Ringme.Utils;


import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.SystemClock;

import com.vtm.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @format: "yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04 AD at 12:08:56 PDT
 * @format: "EEE, MMM d, ''yy" Wed, Jul 4, '01
 * @format: "h:mm a" 12:08 PM
 * @format: "hh 'o''clock' a, zzzz" 12 o'clock PM, Pacific Daylight Time
 * @format: "K:mm a, z" 0:08 PM, PDT
 * @format: "yyyyy.MMMMM.dd GGG hh:mm aaa" 02001.July.04 AD 12:08 PM
 * @format: "EEE, d MMM yyyy HH:mm:ss Z" Wed, 4 Jul 2001 12:08:56 -0700
 * @format: "yyMMddHHmmssZ" 010704120856-0700
 * @format: "yyyy-MM-dd'T'HH:mm:ss.SSSZ" 2001-07-04T12:08:56.235-0700
 */
public class DateTimeUtils {
    /**
     * Giá trị của 1 giây = 1000ms
     */
    public static final long SECOND = 1000;
    /**
     * Giá trị của 1 phút = 1000ms*60s
     */
    public static final long MINUTE = 60 * SECOND;
    /**
     * Giá trị của 1 giờ = 1000ms*60s*60m
     */
    public static final long HOUR = 60 * MINUTE;
    /**
     * Giá trị của 1 ngày = 1000ms*60s*60m*24h
     */
    public static final long DAY = 24 * HOUR;
    /**
     * Giá trị của 1 tuần = 1000ms*60s*60m*24h*7d
     */
    public static final long WEEK = 7 * DAY;
    private static final String TAG = "DateTimeUtils";



    /**
     * @param str    15/10/1990 or 10/05/1990
     * @param format /mm/yyyy or mm/dd/yyyy
     * @return Date
     */
    public static Date stringToDate(String str, String format) {
        String mFormat = format.trim();
        if (mFormat.equals("dd/mm/yyyy"))
            mFormat = "dd/mm/yyyy";
        else if (mFormat.equals("mm/dd/yyyy"))
            mFormat = "MM/dd/yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(mFormat);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public static long stringToSecondsDate(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date currentDate = new Date();
            Date formatDate = sdf.parse(str);
            formatDate.setYear(currentDate.getMonth() >= formatDate.getMonth() ?
                    currentDate.getYear() : currentDate.getYear() - 1);
            return formatDate.getTime();
        } catch (ParseException e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return 0;
    }

    /**
     * @format: dd/mm/yyyy or HH:mm:ss or hh:mm:ss a
     */
    public static String dateToString(Date date, String format) {
        if (format == null || format.equals("dd/mm/yyyy"))
            format = "dd/MM/yyyy";
        else if (format.equals("mm/dd/yyyy"))
            format = "MM/dd/yyyy";
        SimpleDateFormat ts = new SimpleDateFormat(format);
        return ts.format(date);
    }

    public static Date getNextDay(Date date) {
        return new Date(date.getTime() + DAY);
    }

    public static Date getNextDay(Date date, int numberDay) {
        return new Date(date.getTime() + DAY * numberDay);
    }

    public static Date getPreviousDay(Date date) {
        return new Date(date.getTime() - DAY);
    }

    public static Date getPreviousDay(Date date, int numberDay) {
        return new Date(date.getTime() - DAY * numberDay);
    }

    public static String getNow() {
        return dateToString(new Date(), "dd/MM/yyyy HH:mm:ss");
    }

    public static String getToday() {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(new Date());
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return "";
    }

    /**
     * ngay1 after ngay2 return 1.<br>
     * ngay1 before ngay2 return -1.<br>
     * ngay1 == ngay2 return 0.<br>
     */
    public static int compareDate(long lDate1, long lDate2) {
        int flag = -2;
        Date date1 = new Date(lDate1 * 1000);
        Date date2 = new Date(lDate2 * 1000);
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        if (cal1.after(cal2)) {
            flag = 1;
        } else if (cal1.before(cal2)) {
            flag = -1;
        } else if (cal1.equals(cal2)) {
            flag = 0;
        }
        return flag;
    }

    /**
     * ngay1 after ngay2 return 1.<br>
     * ngay1 before ngay2 return -1.<br>
     * ngay1 == ngay2 return 0.<br>
     */
    public static int compareDate(String lDate1, String lDate2) {
        int flag = 0;
        try {
            Date date1 = new Date(lDate1);
            Date date2 = new Date(lDate2);
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(date2);
            if (cal1.after(cal2)) {
                flag = 1;
            } else if (cal1.before(cal2)) {
                flag = -1;
            } else if (cal1.equals(cal2)) {
                flag = 0;
            }
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return flag;
    }

    public static boolean checkTime(long time, int hour) {
        Date dte = new Date(time);
//        Log.d("gio hien tai: " + dte.getHours());
        return dte.getHours() == hour;
    }

    public static boolean checkDateTimeOneWeek(long oldTime, long newTime) {
        long timeLength = Math.abs(oldTime - newTime);

        if (timeLength >= WEEK)
            return true;

        try {
            Date startDte = new Date(oldTime + WEEK);
            Date endDte = new Date(newTime);
            if (startDte.getYear() == endDte.getYear() && startDte.getMonth() == endDte.getMonth() && startDte.getDate() == endDte.getDate())
                return true;
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return false;
    }

    /**
     * VD oldTime la ngay 20/03/2016, endTime là 21/03/2016 thi tra ve true;
     * newTime la ngay <= 20/03/2016 thi tra ve false
     * Kiem tram 2 ngay co lien tiep hay khong
     *
     * @param oldTime
     * @param newTime
     * @author namnh40
     */
    public static boolean checkDateConsecutive(long oldTime, long newTime) {
        try {
            Date startDte = new Date(oldTime + DAY);
            Date endDte = new Date(newTime);
            if (startDte.getYear() == endDte.getYear() && startDte.getMonth() == endDte.getMonth() && startDte.getDate() == endDte.getDate())
                return true;
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return false;
    }

    /**
     * VD oldTime la ngay 20/03/2016, endTime là 21/03/2016 thi tra ve true;
     * newTime la ngay <= 20/03/2016 thi tra ve false
     *
     * @param oldTime
     * @param newTime
     * @author namnh40
     */
    public static boolean checkDateTime(long oldTime, long newTime) {
        long timeLength = Math.abs(oldTime - newTime);

        if (timeLength >= DAY)
            return true;

        try {
            Date startDte = new Date(oldTime);
            Date endDte = new Date(newTime);
            if (startDte.getMonth() != endDte.getMonth() || startDte.getDate() != endDte.getDate())
                return true;
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return false;
    }

    public static String getCurrentDateString() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return day + "/" + (month + 1) + "/" + year;
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatHHmm(String timeStr) {
        long time;
        try {
            time = Long.parseLong(timeStr);
        } catch (Exception e) {
            Log.e(TAG, e);
            return timeStr;
        }
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");
        return sdfDate.format(time * 1000);
    }

    public static String formatTelemor(String timeStr) {
        long time;
        try {
            time = Long.parseLong(timeStr);
        } catch (Exception e) {
            Log.e(TAG, e);
            return timeStr;
        }
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        return sdfDate.format(time * 1000);
    }

    /*
      Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
    * */
    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage;

        long currentSeconds = (int) (currentDuration / SECOND);
        long totalSeconds = (int) (totalDuration / SECOND);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public static int progressToTimer(int progress, int totalDuration) {
        return (progress * totalDuration / 100);
    }

    public static int progressToTimer(int progress, long totalDuration) {
        return (int) (progress * totalDuration / 100);
    }

    public static String twoDigit(int d) {
        NumberFormat formatter = new DecimalFormat("#00");
        return formatter.format(d);
    }

    /**
     * Function to convert milliseconds time to Timer Format
     * Hours:Minutes:Seconds
     */
    public static String milliSecondsToTimer(long milliseconds) {
        try {
            int hours = (int) (milliseconds / HOUR);
            int minutes = (int) ((milliseconds % HOUR) / MINUTE);
            int seconds = (int) ((milliseconds % HOUR % MINUTE) / SECOND);

            StringBuilder sb = new StringBuilder();
            if (hours > 0) {
                sb.append(twoDigit(hours)).append(':');
            }
            sb.append(twoDigit(minutes)).append(':');
            sb.append(twoDigit(seconds));
            return sb.toString();
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return "";
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static boolean checkTimeout(long time) {
        Date d = new Date(time);
        Date now = new Date();
        return now.before(d);
    }

    public static boolean checkTimeoutEvent(long time) {
        Date d = new Date(time);
        Date now = new Date();
        now.setMinutes(0);
        now.setHours(0);
        now.setSeconds(0);

        return now.before(d);
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatmmss(long time) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("mm:ss");
        return sdfDate.format(time);
    }

    public static String getCreateAtMovie() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.ENGLISH);
        return sdfDate.format(new Date());

    }

    public static String getReportDate(long currentTime) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(new Date(currentTime));
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return "";
    }

    public static long getEpochTime() {
//        return System.currentTimeMillis();
        return SystemClock.elapsedRealtime();
    }

    @SuppressLint("DefaultLocale")
    public static String formatTimeBarSecond(float time) {

        int hours = (int) (time / 3600);
        int minutes = (int) ((time - hours * 3600) / 60);
        int seconds = (int) (time % 60);
        return (hours > 0) ? String.format("%d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    @SuppressLint("DefaultLocale")
    public static String formatTimeBarMiniSeconds(long time) {
        if (time < 0) {
            return String.format("%02d:%02d", 0, 0);
        }
        time = (time + 500) / 1000;

        int hours = (int) (time / 3600);
        int minutes = (int) ((time - hours * 3600) / 60);
        int seconds = (int) (time % 60);

        return (hours > 0) ? String.format("%d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    @SuppressLint("DefaultLocale")
    public static String formatTimeToTime(String time, DateFormat dfStart, DateFormat dfEnd) {
        if (android.text.TextUtils.isEmpty(time)) return "";

        String timeEnd = "";
        try {
            Date date = dfStart.parse(time);
            timeEnd = dfEnd.format(date);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return timeEnd;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate(String format, long time) {
        Date date = new Date(System.currentTimeMillis() - getEpochTime() + time);
        return new SimpleDateFormat(format).format(date);
//        return new SimpleDateFormat(format).format(new Date(time));
    }

    public static String milliSecondsToTimerSelfcare(long milliseconds) {
        try {
            int hours = (int) (milliseconds / HOUR);
            int minutes = (int) ((milliseconds % HOUR) / MINUTE);
            int seconds = (int) ((milliseconds % HOUR % MINUTE) / SECOND);

            StringBuilder sb = new StringBuilder();
            if (hours > 0)
                sb.append(oneDigit(hours)).append(" hours");
            if (minutes > 0)
                sb.append(' ').append(oneDigit(minutes)).append(" min");
            sb.append(' ').append(oneDigit(seconds)).append(" sec");
            return sb.toString();
        } catch (Exception e) {

        }
        return "";
    }

    private static String oneDigit(int d) {
        NumberFormat formatter = new DecimalFormat("#0");
        return formatter.format(d);
    }

    public static String calculateTime(Resources res, long time) {
        if (res == null || time <= 0) return "";
        long delta = System.currentTimeMillis() - time;
        if (delta > 0 && delta < WEEK) {
            if (delta > DAY) {
                int num = (int) (delta / DAY);
                if (num == 1)
                    return res.getString(R.string.last_day, num);
                return res.getString(R.string.last_days, num);
            } else if (delta > HOUR) {
                int num = (int) (delta / HOUR);
                if (num == 1)
                    return res.getString(R.string.last_hour, num);
                return res.getString(R.string.last_hours, num);
            } else if (delta > MINUTE) {
                int num = (int) (delta / MINUTE);
                if (num == 1)
                    return res.getString(R.string.last_minute, num);
                return res.getString(R.string.last_minutes, num);
            }
            return res.getString(R.string.last_minute, 1);
        } else if (delta < 0) {
            return res.getString(R.string.last_minute, 1);
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        return sdfDate.format(time);
    }

    @SuppressLint("DefaultLocale")
    public static String getDuration(int totalSecs) {
        if (totalSecs > 0) {
            int hours = totalSecs / 3600;
            int minutes = (totalSecs % 3600) / 60;
            int seconds = totalSecs % 60;
            if (hours > 0)
                return String.format("%02d:%02d:%02d", hours, minutes, seconds);
            return String.format("%02d:%02d", minutes, seconds);
        }
        return "";
    }

    @SuppressLint("DefaultLocale")
    public static String getDuration(String duration) {
        int totalSecs = 0;
        try {
            totalSecs = Integer.parseInt(duration);
        } catch (Exception e) {
        }
        return getDuration(totalSecs);
    }

    @SuppressLint("DefaultLocale")
    public static String getDurationV2(int totalSecs) {
        if (totalSecs > 0) {
            int minutes = (totalSecs % 3600) / 60;
            int seconds = totalSecs % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }
        return "";
    }

    @SuppressLint("DefaultLocale")
    public static String getDurationV2(String duration) {
        int totalSecs = 0;
        try {
            totalSecs = Integer.parseInt(duration);
        } catch (Exception e) {
        }
        return getDurationV2(totalSecs);
    }

    public static String calculateTime( long time) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        return sdfDate.format(time);
    }
}