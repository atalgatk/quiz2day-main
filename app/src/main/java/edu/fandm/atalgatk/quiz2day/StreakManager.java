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
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());
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

        // If user skipped at least one day → reset streak
        if (isMissedDay(lastDate, today)) {
            prefs.edit().putInt(KEY_STREAK, 0).apply();
        }

        // New day → reset progress dots
        if (!lastDate.equals(today)) {
            ProgressManager.resetDailyProgress(context);
        }
    }

    public static void incrementStreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String today = getToday();
        String lastDate = prefs.getString(KEY_LAST_DATE, "");

        // ❗ Prevent multiple increments in same day
        if (today.equals(lastDate)) return;

        int currentStreak = prefs.getInt(KEY_STREAK, 0);

        prefs.edit()
                .putInt(KEY_STREAK, currentStreak + 1)
                .putString(KEY_LAST_DATE, today)
                .apply();
    }

    // 🔥 CHECK IF USER MISSED A DAY
    private static boolean isMissedDay(String lastDate, String today) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date last = sdf.parse(lastDate);
            Date now = sdf.parse(today);

            long diff = now.getTime() - last.getTime();
            long days = diff / (1000 * 60 * 60 * 24);

            return days > 1; // skipped at least one day
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean hasMissedDay(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String lastDate = prefs.getString(KEY_LAST_DATE, "");
        if (lastDate.isEmpty()) return false;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Date last = sdf.parse(lastDate);
            Date today = sdf.parse(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date()));

            long diff = today.getTime() - last.getTime();
            long days = diff / (1000 * 60 * 60 * 24);

            return days > 1; // user skipped at least 1 full day

        } catch (Exception e) {
            return false;
        }
    }

    public static void resetStreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_STREAK, 0).apply();
    }

    public static boolean isTodayCompleted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String lastDate = prefs.getString(KEY_LAST_DATE, "");
        String today = getToday();

        return today.equals(lastDate);
    }

    public static void markTodayCompleted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        prefs.edit()
                .putString(KEY_LAST_DATE, getToday())
                .apply();
    }
}