package edu.fandm.atalgatk.quiz2day;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LevelSelect extends AppCompatActivity {

    private Button btnAbe, btnEsl;

    // Declare the level variable to store the user's choice
    private String selectedLevel = "";

    private final String[] abeLevels = {
            "ABE Level 1: Beginning Literacy",
            "ABE Level 2: Beginning Basic Education",
            "ABE Level 3: Low Intermediate Basic",
            "ABE Level 4: High Intermediate Basic",
            "ABE Level 5: Low Adult Secondary",
            "ABE Level 6: High Adult Secondary"
    };

    private final String[] eslLevels = {
            "ESL Level 1: Beginning ESL Literacy",
            "ESL Level 2: Low Beginning ESL",
            "ESL Level 3: High Beginning ESL",
            "ESL Level 4: Low Intermediate ESL",
            "ESL Level 5: High Intermediate ESL",
            "ESL Level 6: Advanced ESL"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        btnAbe = findViewById(R.id.btnAbe);
        btnEsl = findViewById(R.id.btnEsl);

        btnAbe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLevelSelectionDialog("Adult Basic Education", abeLevels);
            }
        });

        btnEsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLevelSelectionDialog("English as a Second Language", eslLevels);
            }
        });
    }

    private void showLevelSelectionDialog(String category, final String[] levels) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select " + category + " Level");

        builder.setItems(levels, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Store the chosen level in our variable
                //selectedLevel = levels[which];
                String displayLevel = levels[which];
                String keyLevel;

                if (category.contains("English")) {
                    keyLevel = "ESL-" + (which + 1);
                } else {
                    keyLevel = "ABE-" + (which + 1);
                }

                getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        .edit()
                        .putString("selected_level_display", displayLevel)
                        .putString("selected_level_key", keyLevel)
                        .apply();

                // Save the selected level to SharedPreferences
                //getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        //.edit()
                        //.putString("selected_level", selectedLevel)
                        //.apply();


                // Create an Intent to navigate to TodayQuiz
                Intent intent = new Intent(LevelSelect.this, TodayQuiz.class);

                // Attach the selected level data to the Intent using a key ("USER_LEVEL")
                intent.putExtra("USER_LEVEL", selectedLevel);

                // Start the new activity
                startActivity(intent);
            }
        });

        builder.show();
    }
}