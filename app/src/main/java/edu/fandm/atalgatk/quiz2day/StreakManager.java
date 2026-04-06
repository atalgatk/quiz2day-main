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
        checkAndResetIfNeeded(context, prefs);
        return prefs.getInt(KEY_STREAK, 0);
    }

    private static void checkAndResetIfNeeded(Context context, SharedPreferences prefs) {
        String today = getToday();
        String lastDate = prefs.getString(KEY_LAST_DATE, "");

        if (lastDate.isEmpty()) return;

        //if the user missed the day, we reset the streak
        if (hasMissedDay(context)) {
            prefs.edit().putInt(KEY_STREAK, 0).apply();
        }

        //if it is a new day, we reset the daily progress
        if (!lastDate.equals(today)) {
            ProgressManager.resetDailyProgress(context);
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

            return days > 1;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isTodayCompleted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String today = getToday();
        String lastDate = prefs.getString(KEY_LAST_DATE, "");

        //if we are done with today
        if (today.equals(lastDate)) return true;

        //if we finished today's dose for the first time, we increment the streak
        int currentStreak = prefs.getInt(KEY_STREAK, 0);
        prefs.edit()
                .putInt(KEY_STREAK, currentStreak + 1)
                .putString(KEY_LAST_DATE, today)
                .apply();

        return true;
    }

    public static void resetStreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_STREAK, 0).apply();
    }
}