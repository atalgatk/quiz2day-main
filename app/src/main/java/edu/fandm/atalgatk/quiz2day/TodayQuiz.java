package edu.fandm.atalgatk.quiz2day;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TodayQuiz extends AppCompatActivity {

    // UI elements for the dashboard
    private TextView tvStreak, backButton;
    private Button btnStartQuiz;
    private ImageView ivStreakIcon;

    // View objects for the four progress indicators (dots)
    private View dotEnglish, dotMath, dotScience, dotSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_quiz);

        // Linking Java objects to XML layout IDs
        tvStreak = findViewById(R.id.tvStreak);
        backButton = findViewById(R.id.back_button);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);
        ivStreakIcon = findViewById(R.id.ivStreakIcon);

        dotEnglish = findViewById(R.id.dot_english);
        dotMath = findViewById(R.id.dot_math);
        dotScience = findViewById(R.id.dot_science);
        dotSocial = findViewById(R.id.dot_social);

        // Closes the current activity when the back button is clicked
        backButton.setOnClickListener(v -> finish());

        // Navigates to the Subject Selection screen when the start button is clicked
        btnStartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(TodayQuiz.this, SubjectSelection.class);
            startActivity(intent);
        });

        // Initial UI update when the screen is first created
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the UI every time the user returns to this screen (e.g., after finishing a quiz)
        updateUI();
    }

    /**
     * Updates the streak count, progress dots, and the streak icon color
     */
    private void updateUI() {
        // Fetch and display the current user streak from the StreakManager
        int streak = StreakManager.getStreak(this);
        tvStreak.setText(String.valueOf(streak));

        // Check completion status for each subject from the ProgressManager
        boolean engDone = ProgressManager.isSubjectDone(this, "English");
        boolean mathDone = ProgressManager.isSubjectDone(this, "Math");
        boolean sciDone = ProgressManager.isSubjectDone(this, "Science");
        boolean socDone = ProgressManager.isSubjectDone(this, "Social Studies");

        // Update the visual color of each progress dot based on completion
        setDotStatus(dotEnglish, engDone);
        setDotStatus(dotMath, mathDone);
        setDotStatus(dotScience, sciDone);
        setDotStatus(dotSocial, socDone);

        // Logic for the Daily Goal: If all subjects are done, light up the streak icon
        if (engDone && mathDone && sciDone && socDone) {
            // Remove the gray filter to show original icon colors (Daily Dose complete)
            ivStreakIcon.clearColorFilter();
            tvStreak.setTextColor(Color.BLACK);
        } else {
            // Apply a gray filter if the daily goal is not yet met
            ivStreakIcon.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            tvStreak.setTextColor(Color.GRAY);
        }
    }

    /**
     * Changes the background color of a progress dot
     * @param dot The View to change
     * @param isDone Whether the subject is completed
     */
    private void setDotStatus(View dot, boolean isDone) {
        if (isDone) {
            dot.setBackgroundColor(Color.parseColor("#4CAF50")); // Green for Completed
        } else {
            dot.setBackgroundColor(Color.parseColor("#888888")); // Gray for Incomplete
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}