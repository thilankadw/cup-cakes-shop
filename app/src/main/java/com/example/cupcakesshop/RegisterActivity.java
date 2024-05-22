package com.example.cupcakesshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText regEmail, regPassword, regUname;
    private Button regbtn;
    private Button loginRedirectButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        regEmail = findViewById(R.id.regemail);
        regPassword = findViewById(R.id.regpassword);
        regbtn = findViewById(R.id.regbtn);
        regUname = findViewById(R.id.reguname);

        loginRedirectButton = findViewById(R.id.regtologinisterbtn);

        loginRedirectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = regEmail.getText().toString().trim();
                String pass = regPassword.getText().toString().trim();
                String uname = regUname.getText().toString().trim();

                if (user.isEmpty()) {
                    regEmail.setError("Email cannot be empty.");
                    return;
                }
                if (pass.isEmpty()) {
                    regPassword.setError("Password cannot be empty.");
                    return;
                }
                if (uname.isEmpty()) {
                    regUname.setError("Username cannot be empty.");
                    return;
                }

                auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = auth.getCurrentUser().getUid();
                            storeUsername(userId, uname);
                            Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Register Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void storeUsername(String userId, String username) {
        databaseReference.child(userId).child("username").setValue(username).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Username stored successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to store username", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
