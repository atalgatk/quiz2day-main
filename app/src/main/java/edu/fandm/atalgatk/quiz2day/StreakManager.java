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

    public static void checkAndResetIfNeeded(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String today = getToday();
        String lastDate = prefs.getString(KEY_LAST_DATE, "");

        if (lastDate.isEmpty()) {
            prefs.edit().putString(KEY_LAST_DATE, today).apply();
            return;
        }

        //we check if it is a new day
        if (!lastDate.equals(today)) {

            //1)reset dots: if today is a new day, progressmanager has to make all dots gray
            ProgressManager.resetDailyProgress(context);

            // 2. СБРОС СТРИКА: Если сегодня Четверг, а последний раз тесты делали во Вторник
            //2)reset our streak
            //if they missed the next day, then we 0 the streak
            if (hasMissedDay(context)) {
                prefs.edit().putInt(KEY_STREAK, 0).apply();
            }
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

            return days >= 2;
        } catch (Exception e) {
            return false;
        }
    }

    public static void isTodayCompleted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String today = getToday();
        String lastDate = prefs.getString(KEY_LAST_DATE, "");

        //if we have today's progress, then we don't add anything
        if (today.equals(lastDate)) return;

        int currentStreak = prefs.getInt(KEY_STREAK, 0);

        //save: +1 streak and set today as last date
        prefs.edit()
                .putInt(KEY_STREAK, currentStreak + 1)
                .putString(KEY_LAST_DATE, today)
                .apply();
    }

    public static int getStreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_STREAK, 0);
    }
}