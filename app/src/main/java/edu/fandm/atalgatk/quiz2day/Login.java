package edu.fandm.atalgatk.quiz2day;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private FirebaseAuth fba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button login = findViewById(R.id.btnLogin);
        Button register = findViewById(R.id.btnRegister);

        fba = FirebaseAuth.getInstance();

        FirebaseUser user = fba.getCurrentUser();
        if (user != null) {
//          go to the next page
            Intent i = new Intent(Login.this, LevelSelect.class);
            String UID = user.getUid();
            i.putExtra("user_id",UID);
            startActivity(i);
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) findViewById(R.id.etEmail)).getText().toString().trim();
                String password = ((EditText) findViewById(R.id.etPassword)).getText().toString().trim();
                signIn(email, password);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) findViewById(R.id.etEmail)).getText().toString().trim();
                String password = ((EditText) findViewById(R.id.etPassword)).getText().toString().trim();
                registerNewUser(email, password);
            }
        });
    }


    private void signIn(String email, String password) {
        Task s = fba.signInWithEmailAndPassword(email, password);
        s.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Task task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = fba.getCurrentUser();
                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(Login.this, LevelSelect.class);
                    String UID = user.getUid();
                    i.putExtra("user_id",UID);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed :(. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registerNewUser(String email, String password) {
        Task<AuthResult> task = fba.createUserWithEmailAndPassword(email,
                password);
        task.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Task task) {
                Log.d(TAG, "task: " + task);
                if (task.isSuccessful()) {
                    FirebaseUser user = fba.getCurrentUser(); // we're now logged in immediately!
                    Toast.makeText(getApplicationContext(), "New User Created!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(Login.this, );
                    String UID = user.getUid();
                    i.putExtra("user_id",UID);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Registration Failed. Please try again :(", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}