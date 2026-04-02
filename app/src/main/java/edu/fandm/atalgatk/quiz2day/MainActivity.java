package edu.fandm.atalgatk.quiz2day;

import android.content.Intent;
import android.net.Uri;
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
                Intent intent = new Intent(MainActivity.this, LevelSelect.class);
                startActivity(intent);
            }
        });

        Button b = findViewById(R.id.btnWhereWeAre);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri loc = Uri.parse("geo:40.0368308,-76.3121635?q=The+Literacy+Council+of+Lancaster-Lebanon");
                Intent i = new Intent(Intent.ACTION_VIEW, loc);
                startActivity(i);
            }
        });

        Button website = findViewById(R.id.btnMoreAboutUs);
        website.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://literacysuccess.org/"));
            startActivity(intent);
        });

    }
}