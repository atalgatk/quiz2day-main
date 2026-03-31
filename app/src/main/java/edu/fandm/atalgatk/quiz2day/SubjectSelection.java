package edu.fandm.atalgatk.quiz2day;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SubjectSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subject_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //setting all my buttons
        Button english = findViewById(R.id.english_button);
        english.setOnClickListener(v-> openQuestion("English"));

        Button math = findViewById(R.id.math_button);
        math.setOnClickListener(v->openQuestion("Math"));

        Button science = findViewById(R.id.science_button);
        science.setOnClickListener(v->openQuestion("Science"));

        Button ss = findViewById(R.id.ss_button);
        ss.setOnClickListener(v->openQuestion("Social Studies"));

        ImageButton back = findViewById(R.id.back_button);
        back.setOnClickListener(v->finish());
    }


    //have to change
    private void openQuestion(String subject){
        Intent i = new Intent(this, Questions.class);
        i.putExtra("subject", subject);
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp(){
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}