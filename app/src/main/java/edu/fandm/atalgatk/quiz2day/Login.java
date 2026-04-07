package edu.fandm.atalgatk.quiz2day;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    // Member variables (Class level)
    private FirebaseAuth fba;
    private SharedPreferences prefs;
    private AutoCompleteTextView emailAutocomplete;
    private EditText passwordField;
    ConnectivityManager.NetworkCallback networkCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Handle Window Insets for Edge-to-Edge
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // 1. Initialize SharedPreferences (Assigning to the class-level variable)
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // 2. Initialize Views
        emailAutocomplete = findViewById(R.id.etEmail);
        passwordField = findViewById(R.id.etPassword);
        Button loginBtn = findViewById(R.id.btnLogin);
        Button registerBtn = findViewById(R.id.btnRegister);
        Button reset = findViewById(R.id.forgotpassword);

        // 3. Setup Autocomplete Logic
        setupEmailAutocomplete();

        // 4. Initialize Firebase
        fba = FirebaseAuth.getInstance();

        // 5. Check if user is already logged in
        FirebaseUser currentUser = fba.getCurrentUser();
        if (currentUser != null) {
            navigateToLevelSelect(currentUser.getUid());
        }

        // 6. Click Listeners
        loginBtn.setOnClickListener(v -> {
            String email = emailAutocomplete.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            if (!email.isEmpty() && !password.isEmpty()) {
                if(password.length()>= 6){
                    signIn(email, password);
                }else {
                    Toast.makeText(this, "Incorrect Password", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        registerBtn.setOnClickListener(v -> {
            String email = emailAutocomplete.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            if (!email.isEmpty() && !password.isEmpty()) {
                if(password.length()>= 6){
                    registerNewUser(email, password);
                }else {
                    Toast.makeText(this, "Password must be at least 6 characters ", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailAutocomplete.getText().toString().trim();
                if(email.isEmpty()){
                    Toast.makeText(Login.this, "Please enter your email to reset your password", Toast.LENGTH_SHORT).show();
                }else{
                    resetPassword(email);
                }
            }
        });
    }

    // 2. Start monitoring when the activity becomes visible
    @Override
    protected void onStart() {
        super.onStart();
        registerNetworkMonitoring();
    }

    // 3. Stop monitoring when the activity is no longer visible to save battery
    @Override
    protected void onStop() {
        super.onStop();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }

    private void registerNetworkMonitoring() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                // Note: This runs on a background thread! Use runOnUiThread for UI changes.
                runOnUiThread(() -> {
                    Toast.makeText(Login.this, "Connected to Internet", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onLost(Network network) {
                runOnUiThread(() -> {
                    Toast.makeText(Login.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                });
            }
        };
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        // Get current network capabilities
        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork == null) return false;

        NetworkCapabilities caps = cm.getNetworkCapabilities(activeNetwork);
        return caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    private void setupEmailAutocomplete() {
        String lastEmail = prefs.getString("last_email", "");
        List<String> suggestions = new ArrayList<>();
        if (!lastEmail.isEmpty()) {
            suggestions.add(lastEmail);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                suggestions);

        emailAutocomplete.setAdapter(adapter);
    }

    private void signIn(String email, String password) {
        fba.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        saveEmailToPrefs(email);
                        Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                        navigateToLevelSelect(fba.getCurrentUser().getUid());
                    } else if(!isNetworkAvailable()) {
                        Toast.makeText(getApplicationContext(), "Login Failed. Internet Connection Required", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Login Failed. Please try again.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void registerNewUser(String email, String password) {
        fba.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        saveEmailToPrefs(email);
                        Toast.makeText(getApplicationContext(), "New User Created!", Toast.LENGTH_SHORT).show();
                        navigateToLevelSelect(fba.getCurrentUser().getUid());
                    } else if(!isNetworkAvailable()) {
                        Toast.makeText(getApplicationContext(), "Registration Failed. Internet Connection Required", Toast.LENGTH_LONG).show();
                    }else {
                        Log.e(TAG, "Registration failed", task.getException());
                        Toast.makeText(getApplicationContext(), "Registration Failed. Please Try again.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveEmailToPrefs(String email) {
        // Correctly using the class-level 'prefs' object
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("last_email", email);
        editor.apply();
    }

    private void navigateToLevelSelect(String uid) {
        Intent i = new Intent(Login.this, LevelSelect.class);
        i.putExtra("user_id", uid);
        startActivity(i);
//        finish();
    }

    private void resetPassword(String email) {
        fba.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this,
                                "Reset link has been sent to your email!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Login.this,
                                "Error: Could not send reset email. Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}