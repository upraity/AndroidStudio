package com.djupraity.githubcommit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText signupName, signupUsername, signupPass, signupEmail;
    private TextView loginRedirect;
    private Button signupBtn;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge display
        setContentView(R.layout.activity_signup);

        // Initialize UI elements
        signupName = findViewById(R.id.signup_name);
        signupUsername = findViewById(R.id.signup_username);
        signupEmail = findViewById(R.id.signup_email);
        signupPass = findViewById(R.id.signup_pass);
        signupBtn = findViewById(R.id.signup);
        loginRedirect = findViewById(R.id.loginredirect);

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        // Handle Signup Button Click
        signupBtn.setOnClickListener(view -> handleSignup());

        // Handle Login Redirect
        loginRedirect.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    // Handle Signup Logic
    private void handleSignup() {
        String name = signupName.getText().toString().trim();
        String username = signupUsername.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String pass = signupPass.getText().toString().trim();

        if (!validateInputs(name, username, email, pass)) return;

        // Create Firebase User
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Send Email Verification
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            // Save User Data to Firebase Realtime Database
                            saveUserDataToDatabase(name, username, email,pass, user.getUid());

                            // Send email verification
                            user.sendEmailVerification()
                                    .addOnCompleteListener(emailTask -> {
                                        if (emailTask.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this, "Signup successful! Please verify your email.", Toast.LENGTH_LONG).show();
                                            navigateToLogin();
                                        } else {
                                            Toast.makeText(SignupActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                            Log.e("SignupActivity", "Email Verification Error: ", emailTask.getException());
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Signup failed. Try again.", Toast.LENGTH_SHORT).show();
                        Log.e("SignupActivity", "Firebase Error: ", task.getException());
                    }
                });
    }

    // Save User Data to Firebase Realtime Database
    private void saveUserDataToDatabase(String name, String username, String email, String pass, String userId) {
        User user = new User(name, username, email, pass);
        usersRef.child(userId).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("SignupActivity", "User data saved successfully.");
                    } else {
                        Log.e("SignupActivity", "Failed to save user data: ", task.getException());
                    }
                });
    }

    // Validate User Inputs
    private boolean validateInputs(String name, String username, String email, String pass) {
        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email address.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Helper Method to Validate Email
    private boolean isValidEmail(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Navigate to Login Activity
    private void navigateToLogin() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

class User {
    private String name;
    private String username;
    private String email;
    private String pass;

    public User(String name, String username, String email, String pass) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.pass = pass;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String email) {
        this.pass = pass;
    }
}



//package com.djupraity.githubcommit;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class SignupActivity extends AppCompatActivity {
//
//    private EditText signupName, signupUsername, signupPass, signupEmail;
//    private TextView loginRedirect;
//    private Button signupBtn;
//    private FirebaseDatabase database;
//    private DatabaseReference reference;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this); // Enable edge-to-edge display
//        setContentView(R.layout.activity_signup);
//
//        // Initialize UI elements
//        signupName = findViewById(R.id.signup_name);
//        signupUsername = findViewById(R.id.signup_username);
//        signupEmail = findViewById(R.id.signup_email);
//        signupPass = findViewById(R.id.signup_pass);
//        signupBtn = findViewById(R.id.signup);
//        loginRedirect = findViewById(R.id.loginredirect);
//
//        // Initialize Firebase
//        database = FirebaseDatabase.getInstance();
//        reference = database.getReference("Users");
//
//        // Handle Signup Button Click
//        signupBtn.setOnClickListener(view -> handleSignup());
//
//        // Handle Login Redirect
//        loginRedirect.setOnClickListener(view -> {
//            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//            startActivity(intent);
//        });
//
//        // Adjust for system insets
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//
//    // Handle Signup Logic
//    private void handleSignup() {
//        String name = signupName.getText().toString().trim();
//        String username = signupUsername.getText().toString().trim();
//        String email = signupEmail.getText().toString().trim();
//        String pass = signupPass.getText().toString().trim();
//
//        if (!validateInputs(name, username, email, pass)) return;
//
//        Helper helper = new Helper(name, email, username, pass);
//        reference.child(username).setValue(helper).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
//                navigateToLogin();
//            } else {
//                Toast.makeText(SignupActivity.this, "Signup failed. Try again.", Toast.LENGTH_SHORT).show();
//                Log.e("SignupActivity", "Firebase Error: ", task.getException());
//            }
//        });
//    }
//
//    // Validate User Inputs
//    private boolean validateInputs(String name, String username, String email, String pass) {
//        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
//            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (!isValidEmail(email)) {
//            Toast.makeText(this, "Invalid email address.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (pass.length() < 6) {
//            Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//    // Helper Method to Validate Email
//    private boolean isValidEmail(CharSequence email) {
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
//
//    // Navigate to Login Activity
//    private void navigateToLogin() {
//        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();
//    }
//}
