package edu.fandm.atalgatk.quiz2day;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Explanation extends AppCompatActivity {

    private TextView tvCorrectAnswer, tvExplanation;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);

        // Initialize UI elements from the XML layout
        tvCorrectAnswer = findViewById(R.id.tvCorrectAnswer);
        tvExplanation = findViewById(R.id.tvExplanation);
        btnContinue = findViewById(R.id.btnDone);

        // 1. Retrieve data passed from the Questions activity
        // These keys ("subject", "answer", "explanation") must match the ones used in Intent.putExtra
        String subject = getIntent().getStringExtra("subject");
        String answer = getIntent().getStringExtra("answer");
        String explanation = getIntent().getStringExtra("explanation");

        // 2. Display the correct answer and the "Why" explanation
        if (answer != null) {
            tvCorrectAnswer.setText("Answer = " + answer);
        }

        if (explanation != null) {
            tvExplanation.setText(explanation);
        }

        // 3. CRITICAL STEP: Mark this subject as completed in the ProgressManager
        // This ensures the dot on the Home Dashboard turns green
        if (subject != null) {
            ProgressManager.setSubjectDone(this, subject);
        }

        // Handle the Continue button click (bottom)
        // This takes the user back to the Home Dashboard
        // Handle the Continue button click (bottom)
        btnContinue.setOnClickListener(v -> {
            //check the progress
            boolean engDone = ProgressManager.isSubjectDone(this, "English");
            boolean matDone = ProgressManager.isSubjectDone(this, "Math");
            boolean sciDone = ProgressManager.isSubjectDone(this, "Science");
            boolean socDone = ProgressManager.isSubjectDone(this, "Social Studies");

            boolean allDone = engDone && matDone && sciDone && socDone;

            //we switch to the TodayQUiz
            Intent intent = new Intent(Explanation.this, TodayQuiz.class);
            // FLAG_ACTIVITY_CLEAR_TOP will close  all old question screens
            // FLAG_ACTIVITY_SINGLE_TOP will not give you to open a new TodayQuiz, if it is already open
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}