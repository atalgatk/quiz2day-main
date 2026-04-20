package edu.fandm.atalgatk.quiz2day;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SubjectSelection extends AppCompatActivity {

    Button english, math, science, ss; // 4 buttons for each subject
    String level; //stores the level passed from the previous screen

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

        //setting all my buttons from the xml
        english = findViewById(R.id.english_button);
        english.setOnClickListener(v-> openQuestion("English"));

        math = findViewById(R.id.math_button);
        math.setOnClickListener(v->openQuestion("Math"));

        science = findViewById(R.id.science_button);
        science.setOnClickListener(v->openQuestion("Science"));

        ss = findViewById(R.id.ss_button);
        ss.setOnClickListener(v->openQuestion("Social Studies"));

        level = getIntent().getStringExtra("level"); //get level from prev screen

    }

    @Override
    protected void onResume() {
        super.onResume();

        //get saved data from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("Quiz2Day", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        //get last saved date
        String today = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault())
                .format(new java.util.Date());
        String lastDate = prefs.getString("last_date", "");

        // if new day we RESET everything (all progress)
        if (!today.equals(lastDate)) {
            editor.clear(); // clears all "_done"
            editor.putString("last_date", today); // save new day date
            editor.apply();

            //reloading prefs after reset
            prefs = getSharedPreferences("Quiz2Day", MODE_PRIVATE);
        }

        //update each subject button (checks if completed or not)
        updateDoneState(english, "English", prefs);
        updateDoneState(math, "Math", prefs);
        updateDoneState(science, "Science", prefs);
        updateDoneState(ss, "Social Studies", prefs);

        TextView title = findViewById(R.id.titleText);
        TextView subtitle = findViewById(R.id.subtitleText);
        if (allCompleted(prefs)) { // if all 4 subjects completed show completion message
            title.setText("Daily Quiz Completed 🎉");
            subtitle.setText("Come back tomorrow!");

        }
    }

    private void updateDoneState(Button button, String subject, SharedPreferences prefs) {
        boolean done = prefs.getBoolean(subject + "_done", false); // updates button appearance if completed

        if (done) {
            button.setText(subject + " ✓"); //adding a checkmark to completed subject buttons
            button.setAlpha(0.6f); //make button slightly faded
            Log.d("DEBUG", subject + "_done = " + done);

            // adding a strike-through effect
            button.setPaintFlags(button.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }


    //opens question screen once subject is selected
    private void openQuestion(String subject){
        SharedPreferences prefs = getSharedPreferences("Quiz2Day", MODE_PRIVATE);

        boolean done = prefs.getBoolean(subject + "_done", false); //checking if subject completed

        if (done) {
            //prevents user from redoing quetsion, by displaying msg
            Toast.makeText(this, "You already completed today's quiz for " + subject, Toast.LENGTH_SHORT).show();
            return;
        }
        // open question screen
        Intent i = new Intent(this, Questions.class);

        //passing through subject and level
        i.putExtra("subject", subject);
        i.putExtra("level", level);
        startActivity(i);
    }


    //handles back arrow in top bar
    @Override
    public boolean onSupportNavigateUp(){
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }


    // helps check if all 4 subjects are completed
    private boolean allCompleted(SharedPreferences prefs) {
        return prefs.getBoolean("English_done", false) &&
                prefs.getBoolean("Math_done", false) &&
                prefs.getBoolean("Science_done", false) &&
                prefs.getBoolean("Social Studies_done", false);
    }
}