package edu.fandm.atalgatk.quiz2day;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//this class handles the user's daily streak
//it tracks how many days in a row the user has completed the quiz
public class StreakManager {

    private static final String PREFS_NAME = "StreakPrefs";
    private static final String KEY_STREAK = "streak_count";
    private static final String KEY_LAST_DATE = "last_date";
    private static final String KEY_COMPLETED_TODAY = "completed_today";

    //gets today's date as a string in the format "yyyy-MM-dd"
    //this makes it easy to compare days
    private static String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    //checks if a new day has started
    //if yes then:
    //resets daily quiz progress, checks if the streak should be broken
    //updates stored date
    public static void checkAndResetIfNeeded(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String today = getToday();
        String lastDate = prefs.getString(KEY_LAST_DATE, "");

        //first time opening the app - just save today's date
        if (lastDate.isEmpty()) {
            prefs.edit().putString(KEY_LAST_DATE, today).apply();
            return;
        }

        //if it is a new day
        if (!lastDate.equals(today)) {

            //reset quiz progress for the new day
            ProgressManager.resetDailyProgress(context);

            //if user skipped a day - reset streak
            if (hasMissedDay(context)) {
                prefs.edit().putInt(KEY_STREAK, 0).apply();
            }

            //mark today as not completed yet + update date
            prefs.edit()
                    .putBoolean(KEY_COMPLETED_TODAY, false)
                    .putString(KEY_LAST_DATE, today)
                    .apply();
        }
    }

    //checks if the user missed at least one full day
    //if for example last activity was 2+ days ago - streak breaks
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

    //marks today's quizzes as completed
    //only increases streak if it wasn't already counted today
    public static void isTodayCompleted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        boolean alreadyCompleted = prefs.getBoolean(KEY_COMPLETED_TODAY, false);

        //prevent double-counting the same day
        if (alreadyCompleted) return;

        int currentStreak = prefs.getInt(KEY_STREAK, 0);

        prefs.edit()
                .putBoolean(KEY_COMPLETED_TODAY, true)
                .putInt(KEY_STREAK, currentStreak + 1)
                .apply();
    }

    //returns the current streak number
    public static int getStreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_STREAK, 0);
    }
}