package com.example.auctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogAdminActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etEmail,etParola,etKey;
    private Button btnLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_log_admin);
        initComponents();
    }

    private void initComponents() {
        etEmail = (EditText) findViewById(R.id.iOS_tiet_adminlogin_username);
        etParola = (EditText) findViewById(R.id.iOS_tiet_adminlogin_password);
        etKey = (EditText) findViewById(R.id.iOS_tiet_adminlogin_accesskey);
        btnLogin = (Button) findViewById(R.id.iOS_btn_adminlogin_action);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iOS_btn_adminlogin_action:
                loginAdminAction();
        }
    }

    private void loginAdminAction() {
        final String email = etEmail.getText().toString().trim();
        final String pass = etParola.getText().toString().trim();
        final String key = etKey.getText().toString().trim();

        if(email.isEmpty()){
            etEmail.setError(getString(R.string.adminCredentials));
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError(getString(R.string.adminCredentials));
            etEmail.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            etParola.setError(getString(R.string.adminCredentials));
            etParola.requestFocus();
            return;
        }

        if(pass.length() <6){
            etParola.setError(getString(R.string.adminCredentials));
            etParola.requestFocus();
            return;
        }

        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && email.equals("admin@admin.com") && pass.equals("administrator") && key.equals("12345")){
                    Toast.makeText(LogAdminActivity.this, R.string.loginSuccess, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LogAdminActivity.this,AdminActivity.class));
                }else{
                    Toast.makeText(LogAdminActivity.this, R.string.loginFailed, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}