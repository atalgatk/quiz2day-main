package edu.fandm.atalgatk.quiz2day;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetStarted = findViewById(R.id.btnGetStarted);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the next screen (e.g., Login or Select Level)
                // Assuming it goes to SelectLevelActivity for this example
                Intent intent = new Intent(MainActivity.this, SelectLevelActivity.class);
                startActivity(intent);
            }
        });
    }
}