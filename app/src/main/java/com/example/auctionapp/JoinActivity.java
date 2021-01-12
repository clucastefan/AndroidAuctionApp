package com.example.auctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.auctionapp.FirebaseDatabase.Tabele.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUser,etEmail,etParola;
    private FirebaseAuth auth;
    private Button btnJoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        //firebase
        auth = FirebaseAuth.getInstance();

        initComponents();

    }

    private void initComponents() {
        etUser = (EditText) findViewById(R.id.iOS_tiet_join_username);
        etEmail = (EditText) findViewById(R.id.iOS_tiet_join_email);
        etParola = (EditText) findViewById(R.id.iOS_tiet_join_password);
        btnJoin = (Button) findViewById(R.id.iOS_btn_join_action);
        btnJoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iOS_btn_join_action:
                joinAction();
        }
    }

    private void joinAction() {
        final String username = etUser.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        String pass = etParola.getText().toString().trim();

        if(username.isEmpty()){
            etUser.setError(getString(R.string.errUser));
            etUser.requestFocus();
            return;
        }

        if(email.isEmpty()){
            etEmail.setError(getString(R.string.errEmail));
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError(getString(R.string.errEmailValid));
            etEmail.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            etParola.setError(getString(R.string.errPass));
            etParola.requestFocus();
            return;
        }

        if(pass.length() <6){
            etParola.setError(getString(R.string.errPassValid));
            etParola.requestFocus();
            return;
        }

        auth.createUserWithEmailAndPassword(email,pass).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(username,email);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(JoinActivity.this, R.string.userCreated,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);
                            }else
                            {
                                Toast.makeText(JoinActivity.this,getString(R.string.errJoin),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else
                {
                    Toast.makeText(JoinActivity.this,getString(R.string.errJoin),Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}