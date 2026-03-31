package edu.fandm.atalgatk.quiz2day;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Questions extends AppCompatActivity {

    Button btnA, btnB, btnC, btnD, submit;
    String selectedAnswer ="";
    String correctAnswer ="";

    //to keep track of all colors according to users choice
    ColorStateList defaultColor, selectedColor, correctColor, wrongColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_questions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //my buttons
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);
        submit = findViewById(R.id.submit_button);

        //defining my colors
        defaultColor = ColorStateList.valueOf(Color.parseColor("#3E3D53")); //original gray
        selectedColor = getColorStateList(android.R.color.holo_blue_light); // when clicked
        correctColor = getColorStateList(android.R.color.holo_green_dark);  // correct
        wrongColor = getColorStateList(android.R.color.holo_red_dark);      // wrong

        //get subject info from previous screen
        String subject = getIntent().getStringExtra("subject");

        //harcode Q for now
        if (subject.equals("Math")){
            correctAnswer = "4";
            btnA.setText("4");
            btnB.setText("3");
            btnC.setText("5");
            btnD.setText("6");
        }

        //selection logic
        btnA.setOnClickListener(v->selectAnswer(btnA));
        btnB.setOnClickListener(v->selectAnswer(btnB));
        btnC.setOnClickListener(v -> selectAnswer(btnC));
        btnD.setOnClickListener(v -> selectAnswer(btnD));

        submit.setOnClickListener(v->{
            if (selectedAnswer.equals("")) return;

            if (selectedAnswer.equals(correctAnswer)){
                highlightCorrect();

                //go to why (explanation) screen
                new android.os.Handler().postDelayed(() -> {
                    Intent i = new Intent(this, WhyActivity.class);
                    startActivity(i);
                }, 800);
            }else{
                highlightWrong();
            }
        });

    }

    // when selecting
    private void selectAnswer(Button selected) {
        //reset
        resetButtons();
        selected.setBackgroundTintList(selectedColor);
        selectedAnswer = selected.getText().toString();
    }

    // to reset
    private void resetButtons(){
        btnA.setBackgroundTintList(defaultColor);
        btnB.setBackgroundTintList(defaultColor);
        btnC.setBackgroundTintList(defaultColor);
        btnD.setBackgroundTintList(defaultColor);
    }

    //if answer is correct
    private void highlightCorrect() {
        if (selectedAnswer.equals(btnA.getText().toString()))
            btnA.setBackgroundTintList(correctColor);

        if (selectedAnswer.equals(btnB.getText().toString()))
            btnB.setBackgroundTintList(correctColor);

        if (selectedAnswer.equals(btnC.getText().toString()))
            btnC.setBackgroundTintList(correctColor);

        if (selectedAnswer.equals(btnD.getText().toString()))
            btnD.setBackgroundTintList(correctColor);
    }


    //if answer is wrong
    private void highlightWrong() {
        if (selectedAnswer.equals(btnA.getText().toString()))
            btnA.setBackgroundTintList(wrongColor);

        if (selectedAnswer.equals(btnB.getText().toString()))
            btnB.setBackgroundTintList(wrongColor);

        if (selectedAnswer.equals(btnC.getText().toString()))
            btnC.setBackgroundTintList(wrongColor);

        if (selectedAnswer.equals(btnD.getText().toString()))
            btnD.setBackgroundTintList(wrongColor);
    }

}