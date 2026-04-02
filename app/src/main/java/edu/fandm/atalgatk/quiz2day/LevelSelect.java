package edu.fandm.atalgatk.quiz2day;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LevelSelect extends AppCompatActivity {

    private Button btnLevel1, btnLevel2, btnLevel3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        btnLevel1 = findViewById(R.id.btnLevel1);
        btnLevel2 = findViewById(R.id.btnLevel2);
        btnLevel3 = findViewById(R.id.btnLevel3);

        btnLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LevelSelect.this, "Beginner Selected", Toast.LENGTH_SHORT).show();
            }
        });

        btnLevel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LevelSelect.this, "Intermediate Selected", Toast.LENGTH_SHORT).show();
            }
        });

        btnLevel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LevelSelect.this, "Advanced Selected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
