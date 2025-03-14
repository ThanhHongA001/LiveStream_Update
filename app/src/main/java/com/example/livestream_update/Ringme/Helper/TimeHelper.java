package com.example.livestream_update.Ringme.Helper;


import android.content.Context;

import com.vtm.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by thaodv on 7/1/2014.
 */
public class TimeHelper {
    private static final String TAG = TimeHelper.class.getSimpleName();
    //      ngay sinh de mac dinh la ko co j
    public static final long BIRTHDAY_DEFAULT = -1L;
    public static final long BIRTHDAY_DEFAULT_PICKER = 631152000000L;
    public static final long INTERVAL = -30000L;              //-180000L
    //    public static final long INTERVAL_MIN = 300000L;
    public static final long INTERVAL_MIN = 120000L;
    private static final long INTERVAL_MAX = 1800000L;
    private static final long TIME_OUT_LASTON = 5 * 60 * 1000;//(5 phut)
    public static final long ONE_DAY = 86400000L;// nhanh 1 ngay
    private static final long ONE_DAY_ = -86400000L;// cham 1 ngay
    public static final int ONE_HOUR_IN_MILISECOND = 60 * 60 * 1000;
    public static final long FIVE_MIN_IN_MILISECOND = 5 * 60 * 1000;
    private static final int TIME_OUT_STRANGER_MUSIC = 10 * 60 * 1000;           // 10 phut
    private static final long TIME_OUT_ACCEPT_STRANGER_MUSIC = 60 * 1000;    // 1 phuts

    private static final long ONE_MINUTE = 60000;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long SEVEN_DAY = ONE_DAY * 7;
    public static final long ONE_MONTH = ONE_DAY * 30;

    public static final long TIME_UPDATE_PREFIX = 1536944400000L;

//    private static final SimpleDateFormat spfTime = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
//    private static final SimpleDateFormat spfTimeSection = new SimpleDateFormat("HH:mm, dd/MM/yyyy");

    private static final String SDF_IN_DAY = "HH:mm";
    public static final String SDF_IN_YEAR = "dd/MM";
    private static final String SDF_OTH_YEAR = "dd/MM/yyyy";
    private static final String SDF_DURATION = "mm:ss";
    private static final String SDF_EVENT_MSG_OTHER_DAY = "HH:mm, dd/MM/yyyy";


    public static String getDateOfMessage(long time) {
        SimpleDateFormat othYear = new SimpleDateFormat(SDF_OTH_YEAR);
        return othYear.format(time);
    }

    public static String getHourOfMessage(long time) {
        SimpleDateFormat inDay = new SimpleDateFormat(SDF_IN_DAY);
        return inDay.format(time);
    }

    public static String formatTimeEventMessage(long time) {
        SimpleDateFormat inDay = new SimpleDateFormat(SDF_IN_DAY);
        SimpleDateFormat eventMsgOtherDay = new SimpleDateFormat(SDF_EVENT_MSG_OTHER_DAY);
        Calendar currentCal = Calendar.getInstance();
        int currentDay = currentCal.get(Calendar.DAY_OF_YEAR);
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTimeInMillis(time);
        int timeDay = timeCal.get(Calendar.DAY_OF_YEAR);
        if (currentDay == timeDay) {
            return inDay.format(time);
        } else {
            return eventMsgOtherDay.format(time);
        }
    }

    public static long getCurrentTime() {
        Calendar currentCal = Calendar.getInstance();
        return currentCal.getTimeInMillis();
    }

    public static String caculateTimeFeed(Context context, long timeStamp, long deltaTimeServer) {
        SimpleDateFormat othYear = new SimpleDateFormat(SDF_OTH_YEAR);
        SimpleDateFormat sdfDate = new SimpleDateFormat(SDF_IN_YEAR);
        long timeFeedReCal = deltaTimeServer + timeStamp;
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTimeInMillis(System.currentTimeMillis());
        Calendar timeFeed = Calendar.getInstance();
        timeFeed.setTimeInMillis(timeFeedReCal);
        int currentYear = currentCal.get(Calendar.YEAR);
        int timeFeedYear = timeFeed.get(Calendar.YEAR);
        if (timeFeedYear == currentYear) {
            long detal = System.currentTimeMillis() - timeFeedReCal;
            if (detal > 0) {
                if (detal < SEVEN_DAY) {
                    if (detal > ONE_DAY) {
                        int num = (int) (detal / ONE_DAY);
                        if (num == 1) {
                            return context.getString(R.string.yesterday);
                        } else {
                            //return num + " d";
                            return num + " " + context.getString(R.string.feed_time_day);
                        }
                    } else if (detal > ONE_HOUR) {
                        int num = (int) (detal / ONE_HOUR);
                        //return num + " h";
                        if (num == 1) { //TODO thay hour ago => h
                            return num + " " + context.getString(R.string.feed_time_hour);
                        } else {
                            return num + " " + context.getString(R.string.feed_time_hours);
                        }
                    } else if (detal > ONE_MINUTE) {
                        int num = (int) (detal / ONE_MINUTE);
                        //return num + " m";
                        if (num == 1) { //TODO thay minute ago => m
                            return num + " " + context.getString(R.string.feed_time_minute);
                        } else {
                            return num + " " + context.getString(R.string.feed_time_minutes);
                        }
                    } else {
                        return context.getString(R.string.onmedia_time_just_now);
                    }
                } else {
                    return sdfDate.format(timeFeedReCal);
                }
            } else {
                return context.getString(R.string.onmedia_time_just_now);
            }
        } else {
            return othYear.format(timeFeedReCal);
        }
    }
}