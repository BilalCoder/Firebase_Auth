package com.example.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, email, password, matchPassword, phone;
    private TextView textLogin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Button btnSaveImage;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            progressBar = findViewById(R.id.progressBar);
            btnSaveImage = findViewById(R.id.btnSaveImage);
            name = (EditText) findViewById(R.id.editTextPersonName);
            email = (EditText) findViewById(R.id.editTextPersonEmail);
            phone = (EditText) findViewById(R.id.editTextPersonPhone);
            matchPassword = (EditText) findViewById(R.id.editTextMatchPassword);
            password = (EditText) findViewById(R.id.editTextPersonPassword);
            Button register = (Button) findViewById(R.id.buttonRegister);
            textLogin = findViewById(R.id.textlogin);

            mAuth = FirebaseAuth.getInstance();

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signUp();
                }
            });
            textLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            });
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error - "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//    }

    private void signUp() {
        try {
            String Email = email.getText().toString().trim();
            String Password = password.getText().toString().trim();

            if (Email.isEmpty()) {
                email.setError("Email required");
                email.requestFocus();
                return;
            }

        if(!Email.matches(emailPattern)){
            email.setError("Valid Email required");
            email.requestFocus();
            return;
        }

            if (Password.isEmpty()) {
                password.setError("Valid password required");
                password.requestFocus();
                return;
            }

            if (Password.length() < 6) {
                password.setError("Password should be at least 6 characters long");
                password.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success");
                                Toast.makeText(getApplicationContext(), "Registration successful",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(getApplicationContext(), UploadPicture.class);
                                                           //This is to clear the login/signup actity so that whwn we press back, login activity dont come
                                startActivity(intent);
                                finish();
                            } else {
                                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(getApplicationContext(), "User already exists Login to continue",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error - "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}