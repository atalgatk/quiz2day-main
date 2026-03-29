package edu.fandm.atalgatk.quiz2day;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

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
    }


    private void signIn(String email, String password){
        Task s = fba.signInWithEmailAndPassword(email, password);
        s.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Task task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = fba.getCurrentUser();
                    Toast.makeText(getApplicationContext(), "Login
                            Successful!", Toast.LENGTH_LONG).show();
                    updateUI(user);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to
                            login :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void registerNewUser(String email, String password){
        Task<AuthResult> task = fba.createUserWithEmailAndPassword(email,
                password);
        task.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Task task) {
                Log.d(TAG, "task: " + task);
                if (task.isSuccessful()) {
                    FirebaseUser user = fba.getCurrentUser(); // we're now
                    logged in immediately!
                            Toast.makeText(getApplicationContext(), "New User
                                    Created!", Toast.LENGTH_LONG).show();
                    updateUI(user);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to
                            create new user :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Display the user's UID
    private void updateUI(FirebaseUser user){
        TextView tv = (TextView) findViewById(R.id.fb_login_uid_tv);
        tv.setText("Login Successful!\nUID: " + user.getUid());
    }}