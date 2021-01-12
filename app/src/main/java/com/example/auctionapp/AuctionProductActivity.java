package com.example.auctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.auctionapp.CustomAdapters.AuctionAdapter;
import com.example.auctionapp.CustomAdapters.ProductAdapter;
import com.example.auctionapp.FirebaseDatabase.Tabele.Product;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class AuctionProductActivity extends AppCompatActivity implements AuctionAdapter.OnCardClickListener_nou {

    private RecyclerView recyclerView;
    private AuctionAdapter adapter;
    private DatabaseReference databaseReference;
    private List<Product> productList;

    private FirebaseStorage firebaseStorage;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_product);
        initComponents();
    }

    private void initComponents() {
        recyclerView = findViewById(R.id.iOS_auction_recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        productList = new ArrayList<>();
        //
        adapter = new AuctionAdapter(AuctionProductActivity.this,productList);
        recyclerView.setAdapter(adapter);
        adapter.setOnCardClickListener_nou(AuctionProductActivity.this);
        //
        ///
        firebaseStorage = FirebaseStorage.getInstance();
        ///
        databaseReference = FirebaseDatabase.getInstance().getReference("auctionProduct/");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for(DataSnapshot i : snapshot.getChildren()){
                    Product product = i.getValue(Product.class);
                    //??????????????
                    product.setKey(i.getValue(Product.class).getPrimaryKey());
                    productList.add(product);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AuctionProductActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCardClick(int position) {

    }

    @Override
    public void onBidClick(int position) {

    }
}