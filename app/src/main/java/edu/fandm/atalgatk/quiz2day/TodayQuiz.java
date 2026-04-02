package edu.fandm.atalgatk.quiz2day;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TodayQuiz extends AppCompatActivity {


    private TextView tvStreak;
    private Button btnStartQuiz;
    private ImageView ivStreakIcon;

    private View dotEnglish, dotMath, dotScience, dotSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_quiz);

        //initialization
        tvStreak = findViewById(R.id.tvStreak);
        ivStreakIcon = findViewById(R.id.ivStreakIcon);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);

        dotEnglish = findViewById(R.id.dot_english);
        dotMath = findViewById(R.id.dot_math);
        dotScience = findViewById(R.id.dot_science);
        dotSocial = findViewById(R.id.dot_social);

        //switching to SubjectSelection.class
        btnStartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(TodayQuiz.this, SubjectSelection.class);
            startActivity(intent);
        });

        updateUI();

        //to check if the code is working

        //ProgressManager.setSubjectDone(this, "English");
        //ProgressManager.setSubjectDone(this, "Math");
        //ProgressManager.setSubjectDone(this, "Science");
        //ProgressManager.setSubjectDone(this, "Social Studies");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //we update the screen all the time when the user comes back
        updateUI();
    }

    private void updateUI() {

        boolean engDone = ProgressManager.isSubjectDone(this, "English");
        boolean mathDone = ProgressManager.isSubjectDone(this, "Math");
        boolean sciDone = ProgressManager.isSubjectDone(this, "Science");
        boolean socDone = ProgressManager.isSubjectDone(this, "Social Studies");

        // check for missing day
        if (StreakManager.hasMissedDay(this)) {
            StreakManager.resetStreak(this);
        }

        setDotStatus(dotEnglish, engDone);
        setDotStatus(dotMath, mathDone);
        setDotStatus(dotScience, sciDone);
        setDotStatus(dotSocial, socDone);

        boolean allDone = engDone && mathDone && sciDone && socDone;

        if (allDone) {

            if (!StreakManager.isTodayCompleted(this)) {
                StreakManager.incrementStreak(this);
                StreakManager.markTodayCompleted(this);
            }

            // streak is active
            ivStreakIcon.setColorFilter(null);
            ivStreakIcon.setAlpha(1.0f);
            tvStreak.setTextColor(Color.BLACK);

        } else {
            //gray color streak
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ivStreakIcon.setColorFilter(new ColorMatrixColorFilter(matrix));
            ivStreakIcon.setAlpha(0.5f);
            tvStreak.setTextColor(Color.GRAY);
        }

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