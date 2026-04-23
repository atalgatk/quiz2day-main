package edu.fandm.atalgatk.quiz2day;

import android.content.Context;
import android.content.SharedPreferences;

//this class keeps track of which quiz subjects
//the user has already completed for the day

public class ProgressManager {
    private static final String PREFS_NAME = "QuizProgress";


    //marking a subject as completed
    //if we finish Math, we store it as done
    public static void setSubjectDone(Context context, String subject) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(subject, true).apply();
    }


    //checking if a subject is completed
    //returns true if done, false if not
    public static boolean isSubjectDone(Context context, String subject) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(subject, false);
    }


    //clears all saved progress
    //this can be used at the start of a new day
    //to reset everything
    public static void resetDailyProgress(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }


}