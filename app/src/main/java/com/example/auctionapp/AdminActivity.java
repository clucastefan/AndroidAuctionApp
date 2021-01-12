package com.example.auctionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    private Button btnAdminProd;
    private Button btnAdminAuct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initComponents();
    }

    private void initComponents() {
        btnAdminProd = (Button) findViewById(R.id.iOS_admin_products_btn);
        btnAdminAuct = (Button) findViewById(R.id.iOS_admin_auction_btn);
        btnAdminProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,AdminProductActivity.class));
                //startActivity(new Intent(AdminActivity.this,TestProductActivity.class));
            }
        });
        //implementare add auction de facut
        //done
        btnAdminAuct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,AuctionProductActivity.class));
            }
        });
    }
}