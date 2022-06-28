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
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register, forgotPassword;
    private EditText editText_Email, editText_Password;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        register = (TextView) findViewById(R.id.signup_btn);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.loginbtn);
        signIn.setOnClickListener(this);

        editText_Email = (EditText) findViewById(R.id.editText_email);
        editText_Password = (EditText) findViewById(R.id.editText_password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar_login);

        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.forgotpassword_btn);
        forgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_btn:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            case R.id.loginbtn:
                userLogin();
                break;
            case R.id.forgotpassword_btn:
                startActivity(new Intent(this,ForgotPassword.class));
                break;
        }
    }

    private void userLogin(){
        String email = editText_Email.getText().toString().trim();
        String password = editText_Password.getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editText_Email.setError("Please provide valid email!");
            editText_Email.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editText_Password.setError("Password is required!");
            editText_Password.requestFocus();
            return;
        }

        if(password.length() < 6){
            editText_Password.setError("Min password length should be 6 characters!");
            editText_Password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, Home.class));
                        progressBar.setVisibility(View.GONE);
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }else{
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}