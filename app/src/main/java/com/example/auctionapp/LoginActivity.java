package com.example.auctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail,etParola;
    private Button btnLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        initComponents();
    }

    private void initComponents() {
        etEmail = (EditText) findViewById(R.id.iOS_tiet_login_email);
        etParola = (EditText) findViewById(R.id.iOS_tiet_login_password);
        btnLogin = (Button) findViewById(R.id.iOS_btn_login_action);
        TextView termsAndConditions = (TextView) findViewById(R.id.iOS_login_tv_terms);
        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TermsActivity.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iOS_btn_login_action:
                loginAction();
        }
    }

    private void loginAction() {
        final String email = etEmail.getText().toString().trim();
        String pass = etParola.getText().toString().trim();

        if(email.isEmpty()){
            etEmail.setError(getString(R.string.LoginEmail));
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError(getString(R.string.errEmailValid));
            etEmail.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            etParola.setError(getString(R.string.loginPass));
            etParola.requestFocus();
            return;
        }

        if(pass.length() <6){
            etParola.setError(getString(R.string.errPassValid));
            etParola.requestFocus();
            return;
        }

        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, R.string.loginSuccess, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this,UserActivity.class));
                }else{
                    Toast.makeText(LoginActivity.this, R.string.loginFailed, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}