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
                Uri loc = Uri.parse("https://www.google.com/maps/place/The+Literacy+Council+of+Lancaster-Lebanon/@40.0368308,-76.3121635,17z/data=!3m1!4b1!4m6!3m5!1s0x89c624f21949c7b1:0xcb73af00c60886b7!8m2!3d40.0368308!4d-76.3121635!16s%2Fg%2F1tplr88_?entry=ttu&g_ep=EgoyMDI2MDMzMS4wIKXMDSoASAFQAw%3D%3D");
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