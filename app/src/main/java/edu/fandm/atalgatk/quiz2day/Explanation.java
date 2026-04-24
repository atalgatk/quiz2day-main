package edu.fandm.atalgatk.quiz2day;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//this screen shows the correct answer + explanation
//after the user answers a question

public class Explanation extends AppCompatActivity {

    private TextView tvCorrectAnswer, tvExplanation;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);

        //connect java variables to UI elements from XML
        tvCorrectAnswer = findViewById(R.id.tvCorrectAnswer);
        tvExplanation = findViewById(R.id.tvExplanation);
        btnContinue = findViewById(R.id.btnDone);

        //get the data passed from the Questions activity
        //these keys ("subject", "answer", "explanation") must match the ones used in Intent.putExtra
        String subject = getIntent().getStringExtra("subject");
        String correctAnswer = getIntent().getStringExtra("answer");
        String explanation = getIntent().getStringExtra("explanation");



        //show correct answer
        if (correctAnswer != null) {
            tvCorrectAnswer.setText("Answer = " + correctAnswer);
        }

        //show explanation (the "why")
        if (explanation != null) {
            tvExplanation.setText(explanation);
        }

        //mark this subject as completed
        //this is what turns the dot green on the main screen
        if (subject != null) {
            ProgressManager.setSubjectDone(this, subject);
        }

        //marking subjects as completed in the ProgressManager
        //this tell us that the dot on the TodayQuiz page turns green
        if (subject != null) {
            ProgressManager.setSubjectDone(this, subject);
        }

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