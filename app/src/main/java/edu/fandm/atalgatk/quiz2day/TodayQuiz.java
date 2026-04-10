package edu.fandm.atalgatk.quiz2day;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class TodayQuiz extends AppCompatActivity {


    private TextView tvStreak, tvUserLevel;
    private Button btnStartQuiz;
    private ImageView ivStreakIcon;

    private View dotEnglish, dotMath, dotScience, dotSocial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_quiz);

        //initialization
        tvStreak = findViewById(R.id.tvStreak);
        tvUserLevel = findViewById(R.id.tvUserLevel);
        ivStreakIcon = findViewById(R.id.ivStreakIcon);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);

        dotEnglish = findViewById(R.id.dot_english);
        dotMath = findViewById(R.id.dot_math);
        dotScience = findViewById(R.id.dot_science);
        dotSocial = findViewById(R.id.dot_social);

        //switching to SubjectSelection.class
        btnStartQuiz.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

            //get firebase key (for loading correct questions)
            String levelKey = prefs.getString("selected_level_key", "ESL-1");

            //move to SubjectSelection.class
            Intent intent = new Intent(TodayQuiz.this, SubjectSelection.class);

            //pass level key to next screen
            intent.putExtra("level", levelKey);
            startActivity(intent);
        });

        updateUI();

        Button logoutBtn = findViewById(R.id.btnLogout);

        logoutBtn.setOnClickListener(v -> {
            // 1. Sign out from Firebase
            FirebaseAuth.getInstance().signOut();

            // 2. Redirect to Login Activity
            Intent intent = new Intent(TodayQuiz.this, Login.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            finish();
        });

        //ProgressManager.resetDailyProgress(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //we update the screen all the time when the user comes back
        updateUI();
    }

    private void updateUI() {
        // we check: is it a new day? did we miss anything?
        //it is going to reset dots and streak in memory before we "draw" them
        StreakManager.checkAndResetIfNeeded(this);

        //SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        //String level = prefs.getString("selected_level", "NOT SELECTED");
        //if (tvUserLevel != null) tvUserLevel.setText(level);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        //get display-friendly level (for UI only)
        String displayLevel = prefs.getString("selected_level_display", "NOT SELECTED");
        //show level on screen
        tvUserLevel.setText(displayLevel);

        //now we count the progress (after the reset)
        boolean engDone = ProgressManager.isSubjectDone(this, "English");
        boolean mathDone = ProgressManager.isSubjectDone(this, "Math");
        boolean sciDone = ProgressManager.isSubjectDone(this, "Science");
        boolean socDone = ProgressManager.isSubjectDone(this, "Social Studies");

        setDotStatus(dotEnglish, engDone);
        setDotStatus(dotMath, mathDone);
        setDotStatus(dotScience, sciDone);
        setDotStatus(dotSocial, socDone);

        boolean allDone = engDone && mathDone && sciDone && socDone;

        if (allDone) {
            //count the day
            StreakManager.isTodayCompleted(this);

            //int currentStreak = StreakManager.getStreak(this);
            //tvStreak.setText(String.valueOf(currentStreak));

            ivStreakIcon.setColorFilter(null);
            ivStreakIcon.setAlpha(1.0f);
            tvStreak.setTextColor(Color.BLACK);
        } else {
            //gray the icon
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ivStreakIcon.setColorFilter(new ColorMatrixColorFilter(matrix));
            ivStreakIcon.setAlpha(0.5f);
            tvStreak.setTextColor(Color.GRAY);
        }

        //take the actual number of streak from memory
        int streak = StreakManager.getStreak(this);
        tvStreak.setText(String.valueOf(streak));
    }

    private void setDotStatus(View dot, boolean isDone) {
        //make dots green if it's done and gray if not
        int color = isDone ? Color.parseColor("#4CAF50") : Color.parseColor("#D1D5DB");
        if (dot != null && dot.getBackground() != null) {
            dot.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}