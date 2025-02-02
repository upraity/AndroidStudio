package com.djupraity.githubcommit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText loginUsername, loginPass, loginEmail;
    private Button loginBtn;
    private TextView signupRedirect;
    private SharedPreferences sharedPreferences;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize views
        loginUsername = findViewById(R.id.signin_username);
        loginEmail = findViewById(R.id.signin_email);
        loginPass = findViewById(R.id.signin_pass);
        loginBtn = findViewById(R.id.signin);
        signupRedirect = findViewById(R.id.signupredirect);

        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        if (sharedPreferences.contains("username") && sharedPreferences.contains("email")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Set click listener for login button
        loginBtn.setOnClickListener(view -> {
            String username = loginUsername.getText().toString().trim();
            String email = loginEmail.getText().toString().trim();
            String pass = loginPass.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!validateEmail()) {
                return;
            } else {
                authenticateUser(email, pass, username);
            }
        });

        // Redirect to signup activity
        signupRedirect.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // Handle insets for immersive mode
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private Boolean validateEmail() {
        String val = loginEmail.getText().toString().trim();

        // Regex for a valid email format
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!val.matches(emailPattern)) {
            loginEmail.setError("Invalid email");
            return false;
        } else {
            loginEmail.setError(null);
            return true;
        }
    }

    private void authenticateUser(String email, String pass, String username) {
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            validateUsernameInDatabase(username, email, pass);
                        } else {
                            Toast.makeText(this, "Please verify your email first.", Toast.LENGTH_SHORT).show();
                            firebaseAuth.signOut(); // Logout the user if not verified
                        }
                    } else {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    private void validateUsernameInDatabase(String username, String email, String pass) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//        Query query = reference.orderByChild("username").equalTo(username);
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String emailDb = snapshot.child(username).child("email").getValue(String.class);
//                    String passwordDb = snapshot.child(username).child("pass").getValue(String.class);
//                    if (Objects.equals(emailDb, email) && Objects.equals(passwordDb, pass)) {
//                        // Save user data in SharedPreferences
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("username", username);
//                        editor.putString("email", email);
//                        editor.putString("pass", pass);
//                        editor.apply();
//
//                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Invalid Credentials1", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(LoginActivity.this, "Invalid Credentials2", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(LoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//private void validateUsernameInDatabase(String username, String email, String pass) {
//    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//
//    reference.addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            boolean validCredentials = false;
//
//            // Loop through all children
//            for (DataSnapshot child : snapshot.getChildren()) {
//                String dbUsername = child.child("username").getValue(String.class);
//                String dbEmail = child.child("email").getValue(String.class);
//                String dbPassword = child.child("pass").getValue(String.class);
//
//                // Debugging: Print fetched values
//                System.out.println("Fetched Username: " + dbUsername);
//                System.out.println("Fetched Email: " + dbEmail);
//                System.out.println("Fetched Password: " + dbPassword);
//
//                // Match username, email, and password
//                if (Objects.equals(dbUsername, username) && Objects.equals(dbEmail, email) && Objects.equals(dbPassword, pass)) {
//                    validCredentials = true;
//
//                    // Save user data in SharedPreferences
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("username", username);
//                    editor.putString("email", email);
//                    editor.putString("pass", pass);
//                    editor.apply();
//
//                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                    break;
//                }
//            }
//
//            if (!validCredentials) {
//                Toast.makeText(LoginActivity.this, "Invalid Credentials not valid", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//            Toast.makeText(LoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    });
//}
private void validateUsernameInDatabase(String username, String email, String pass) {
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

    reference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            boolean validCredentials = false;

            // Loop through all children
            for (DataSnapshot child : snapshot.getChildren()) {
                String dbUsername = child.child("username").getValue(String.class);
                String dbEmail = child.child("email").getValue(String.class);
                String dbPassword = child.child("pass").getValue(String.class);

                // Debugging: Print fetched values and inputs
                System.out.println("Fetched Username: " + dbUsername);
                System.out.println("Fetched Email: " + dbEmail);
                System.out.println("Fetched Password: " + dbPassword);

                // Debugging: Print the inputs to see if they match
                System.out.println("Input Username: " + username);
                System.out.println("Input Email: " + email);
                System.out.println("Input Password: " + pass);

                // Match username, email, and password
                if (Objects.equals(dbUsername, username) && Objects.equals(dbEmail, email) && Objects.equals(dbPassword, pass)) {
                    validCredentials = true;

                    // Save user data in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putString("email", email);
                    editor.putString("pass", pass);
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
            }

            if (!validCredentials) {
                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(LoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}

}


//package com.djupraity.githubcommit;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.Objects;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private EditText loginUsername, loginPass, loginEmail;
//    private Button loginBtn;
//    private TextView signupRedirect;
//    private SharedPreferences sharedPreferences;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_login);
//
//        // Initialize views
//        loginUsername = findViewById(R.id.signin_username);
//        loginEmail = findViewById(R.id.signin_email);
//        loginPass = findViewById(R.id.signin_pass);
//        loginBtn = findViewById(R.id.signin);
//        signupRedirect = findViewById(R.id.signupredirect);
//
//        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
//
//        // Check if user is already logged in
//        if (sharedPreferences.contains("username") && sharedPreferences.contains("email")) {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//
//        // Set click listener for login button
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String username = loginUsername.getText().toString().trim();
//                String email = loginEmail.getText().toString().trim();
//                String pass = loginPass.getText().toString().trim();
//
//                if (username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
//                    Toast.makeText(LoginActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (!validateEmail()) {
//                    return;
//                } else {
//                    checkUser(username, email, pass);
//                }
//            }
//        });
//
//        // Redirect to signup activity
//        signupRedirect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        // Handle insets for immersive mode
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//
//    private Boolean validateEmail() {
//        String val = loginEmail.getText().toString().trim();
//
//        // Regex for a valid email format
//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//
//        if (!val.matches(emailPattern)) {
//            loginEmail.setError("Invalid email");
//            return false;
//        } else {
//            loginEmail.setError(null);
//            return true;
//        }
//    }
//
//    private void checkUser(String username, String email, String pass) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//        Query checkUserData = reference.orderByChild("username").equalTo(username);
//
//        checkUserData.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    loginUsername.setError(null);
//                    String emailDb = snapshot.child(username).child("email").getValue(String.class);
//                    String passwordDb = snapshot.child(username).child("pass").getValue(String.class);
//
//                    if (Objects.equals(emailDb, email) && Objects.equals(passwordDb, pass)) {
//                        loginEmail.setError(null);
//                        loginPass.setError(null);
//
//                        // Save user data in SharedPreferences
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("username", username);
//                        editor.putString("email", email);
//                        editor.apply();
//
//                        Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(LoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
