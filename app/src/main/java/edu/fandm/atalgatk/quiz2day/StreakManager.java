package edu.fandm.atalgatk.quiz2day;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StreakManager {

    private static final String PREFS_NAME = "StreakPrefs";
    private static final String KEY_STREAK = "streak_count";
    private static final String KEY_LAST_DATE = "last_date";

    private static String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public static int getStreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        //check missed day and return the number of streak
        checkAndResetIfNeeded(context);
        return prefs.getInt(KEY_STREAK, 0);
    }

    public static void checkAndResetIfNeeded(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String today = getToday();
        String lastDate = prefs.getString(KEY_LAST_DATE, "");

        // if it's the user's first time ever, just save today's date and stop
        if (lastDate.isEmpty()) {
            prefs.edit().putString(KEY_LAST_DATE, today).apply();
            return;
        }

        // IF THE DATES ARE DIFFERENT, IT'S A NEW DAY
        if (!lastDate.equals(today)) {

            // 1)check if they missed a day to reset the streak number
            if (hasMissedDay(context)) {
                prefs.edit().putInt(KEY_STREAK, 0).apply();
            }

            // 2)CRITICAL: Reset the dots (ProgressManager)
            ProgressManager.resetDailyProgress(context);

            // 3) update last_date to today so this doesn't run again until tomorrow
            prefs.edit().putString(KEY_LAST_DATE, today).apply();
        }
    }

    public static boolean hasMissedDay(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String lastDate = prefs.getString(KEY_LAST_DATE, "");
        if (lastDate.isEmpty()) return false;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date last = sdf.parse(lastDate);
            Date today = sdf.parse(getToday());

            long diff = today.getTime() - last.getTime();
            long days = diff / (1000 * 60 * 60 * 24);

            // if days == 1, means they worked yesterday (all good)
            // if days > 1, means they missed yesterday (reset)
            return days > 1;
        } catch (Exception e) {
            return false;
        }
    }

    public static void isTodayCompleted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String today = getToday();
        String lastDate = prefs.getString(KEY_LAST_DATE, "");

        // if today we have already completed, do nothing
        if (today.equals(lastDate)) return;

        //if we finished everything for the day for the first time, +1 to the streak
        int currentStreak = prefs.getInt(KEY_STREAK, 0);
        prefs.edit()
                .putInt(KEY_STREAK, currentStreak + 1)
                .putString(KEY_LAST_DATE, today)
                .apply();
    }

    public static void resetStreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_STREAK, 0).apply();
    }
}