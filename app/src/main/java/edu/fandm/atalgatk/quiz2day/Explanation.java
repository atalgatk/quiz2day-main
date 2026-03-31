package edu.fandm.atalgatk.quiz2day;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Explanation extends AppCompatActivity {

    private TextView tvCorrectAnswer, tvExplanation, backButton;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);

        // Initialize UI elements from the XML layout
        tvCorrectAnswer = findViewById(R.id.tvCorrectAnswer);
        tvExplanation = findViewById(R.id.tvExplanation);
        backButton = findViewById(R.id.back_button_explanation);
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

        // Handle the Back button click (top left)
        backButton.setOnClickListener(v -> finish());

        // Handle the Continue button click (bottom)
        // This takes the user back to the Home Dashboard
        btnContinue.setOnClickListener(v -> {
            // finish() closes this screen and returns the user to the previous one
            // If you want to jump directly to TodayQuiz, you could use an Intent here
            finish();
        });
    }
}