package com.example.wefix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView registerUser, haveAccount;
    private EditText editText_email, editText_password, editText_FullName;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.signupbtn);
        registerUser.setOnClickListener(this);
        haveAccount = (TextView) findViewById(R.id.haveaccount_btn);
        haveAccount.setOnClickListener(this);

        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password = (EditText) findViewById(R.id.editText_password);
        editText_FullName = (EditText) findViewById(R.id.editText_FullName);

        progressBar = (ProgressBar) findViewById(R.id.progressBar_signup);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.haveaccount_btn:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.signupbtn:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editText_email.getText().toString().trim();
        String password = editText_password.getText().toString().trim();
        String fullname = editText_FullName.getText().toString().trim();
        String phone = "";
        String address = "";

        if(email.isEmpty()){
            editText_email.setError("Email is required!");
            editText_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editText_email.setError("Please provide valid email!");
            editText_email.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editText_password.setError("Password is required!");
            editText_password.requestFocus();
            return;
        }

        if(password.length() < 6){
            editText_password.setError("Min password length should be 6 characters!");
            editText_password.requestFocus();
            return;
        }

        if(fullname.isEmpty()){
            editText_FullName.setError("Full name is required!");
            editText_FullName.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            User user = new User(fullname, email, phone, address);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                finish();
                                            } else {
                                                Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                            }else{
                            Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}