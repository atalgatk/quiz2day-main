package edu.fandm.atalgatk.quiz2day;

import android.content.Context;
import android.content.SharedPreferences;

public class ProgressManager {
    private static final String PREFS_NAME = "QuizProgress";


    public static void setSubjectDone(Context context, String subject) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(subject, true).apply();
    }


    public static boolean isSubjectDone(Context context, String subject) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(subject, false);
    }


    public static void resetDailyProgress(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}