package edu.fandm.atalgatk.quiz2day;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Manages the user's daily streak and handles resetting daily progress.
 */
public class StreakManager {
    private static final String PREFS_NAME = "StreakPrefs";
    private static final String KEY_STREAK = "streak_count";
    private static final String KEY_LAST_DATE = "last_date";

    /**
     * Retrieves the current streak count and checks if a new day has started.
     */
    public static int getStreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        checkAndResetIfNeeded(context, prefs);
        return prefs.getInt(KEY_STREAK, 0);
    }

    /**
     * Checks if the date has changed since the last app use.
     * If it is a new day, it resets the 4 subject progress dots.
     */
    private static void checkAndResetIfNeeded(Context context, SharedPreferences prefs) {
        // Get today's date in YYYY-MM-DD format
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String lastDate = prefs.getString(KEY_LAST_DATE, "");

        if (lastDate.isEmpty()) return;

        // If today's date is different from the last saved date, reset daily progress dots
        if (!lastDate.equals(today)) {
            ProgressManager.resetDailyProgress(context);

            /* Optional: Add logic here to check if the user missed a day.
               If the gap is > 48 hours, you can set the streak back to 0.
            */
        }
    }

    /**
     * Increments the streak count and saves the current date.
     * This should be called only when ALL 4 subjects are completed.
     */
    public static void incrementStreak(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int currentStreak = prefs.getInt(KEY_STREAK, 0);
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Update the streak count and mark today as the last completion date
        prefs.edit()
                .putInt(KEY_STREAK, currentStreak + 1)
                .putString(KEY_LAST_DATE, today)
                .apply();
    }
}
