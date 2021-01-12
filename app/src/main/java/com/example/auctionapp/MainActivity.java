package com.example.auctionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();

    }

    private void initComponents() {
        ImageButton buttonLogin = (ImageButton) findViewById(R.id.iOS_btn_login);
        ImageButton buttonJoin = (ImageButton) findViewById(R.id.iOS_btn_signup);
        Button buttonLoginAdmin = (Button) findViewById(R.id.iOS_btn_login_admin);

        buttonLogin.setOnClickListener(startOnClickLogin());
        buttonJoin.setOnClickListener(startOnClickJoin());
        buttonLoginAdmin.setOnClickListener(startOnClickLoginAdmin());
    }

    private View.OnClickListener startOnClickLogin() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(view, LoginActivity.class);
            }
        };
    }

    private View.OnClickListener startOnClickJoin() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(view, JoinActivity.class);
            }
        };
    }

    private View.OnClickListener startOnClickLoginAdmin() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(view, LogAdminActivity.class);
            }
        };
    }

    private void startActivity(View view, Class c) {
        Intent intent = new Intent(view.getContext(), c);
        view.getContext().startActivity(intent);
    }


}