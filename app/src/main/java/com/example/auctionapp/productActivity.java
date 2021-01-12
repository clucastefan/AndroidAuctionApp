package com.example.auctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auctionapp.CustomAdapters.ProductAdapter;
import com.example.auctionapp.FirebaseDatabase.Tabele.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class productActivity extends AppCompatActivity implements ProductAdapter.OnCardClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private DatabaseReference databaseReference;
    private List<Product> productList;
    private List<Product> productList_nou;
    private ProgressBar progressBar;
    private static int NOD=1;
    //private Button btnUpdate2;

    //
    private FirebaseStorage firebaseStorage;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        //btnUpdate2 = findViewById(R.id.ios_updateprod_btn);
        initComponents();
    }

    private void initComponents() {
        recyclerView = findViewById(R.id.iOS_product_recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        productList = new ArrayList<>();
        //
        adapter = new ProductAdapter(productActivity.this,productList);
        recyclerView.setAdapter(adapter);
        adapter.setOnCardClickListener(productActivity.this);
        //
        progressBar = findViewById(R.id.progressBar);
        ///
        firebaseStorage = FirebaseStorage.getInstance();
        ///
        databaseReference = FirebaseDatabase.getInstance().getReference("images/");
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
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(productActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

    @Override
    public void onCardClick(int position) {
       // Toast.makeText(this, "Normal Click" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuctionClick(int position) {
        //Toast.makeText(this, "Auction Click"+ position, Toast.LENGTH_SHORT).show();
        Log.e("TAG", "then: " + productList.size());
        final Product product = productList.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference("auctionProduct");
        String imgID = databaseReference.push().getKey();
        Product product_nou = new Product(product.getDenumire(),product.getPret(),product.getDescriere(),product.getImgUrl(),imgID);
        Log.e("TAG", "then: " + product_nou.getPrimaryKey());
        //Log.e("TAG", "then: " + productList.get(position-1).getPrimaryKey());

        //String imgID = databaseReference.push().getKey();
        databaseReference.child(String.valueOf(imgID)).setValue(product_nou);
        //NOD++;
        Toast.makeText(this, R.string.auctionstart, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(final int position) {
        final Product product = productList.get(position);
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/");
        DatabaseReference imgreference = FirebaseDatabase.getInstance().getReference().child("images").child(String.valueOf(position+1));
        imgreference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //storageReference.child(productList.get(position+1).getImgUrl()).delete();
                databaseReference.child(String.valueOf(position+1)).removeValue();

                Toast.makeText(productActivity.this, R.string.deletedsuccess, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(productActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUpdateClick(int position) {
        //Toast.makeText(this, "Update Click"+ position, Toast.LENGTH_SHORT).show();
        final Product product = productList.get(position);
        //productList.get(position).
        updateProdus(String.valueOf(position+1),product.getDenumire(),product.getPret(),product.getDescriere(),product.getImgUrl());

    }

    private void updateProdus(final String key, String denumire, final String pret, final String descriere, final String imgUrl) {
        final EditText etDen,etPret,etDesc;
        Button btnUpdate;
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog,null ,false);
        adb.setView(view);
        adb.setTitle("Update" + " " + denumire);
        etDen = (EditText) view.findViewById(R.id.iOS_tiet_update_den);
        etPret = (EditText) view.findViewById(R.id.iOS_tiet_update_pret);
        etDesc = (EditText) view.findViewById(R.id.iOS_tiet_update_descriere);
        btnUpdate = (Button) view.findViewById(R.id.ios_updateprod_btn);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String den = etDen.getText().toString().trim();
                String pret = etPret.getText().toString().trim();
                String desc = etDesc.getText().toString().trim();
                updateAction(key,den,pret,desc,imgUrl);
            }
        });

        AlertDialog alertDialog = adb.create();
        alertDialog.show();

    }


    private void updateAction(String key, String denumire, String Pret, String Descriere,String imgUrl) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("images").child(key);
        Product product = new Product(denumire,Pret,Descriere,imgUrl);
        databaseReference.setValue(product);
        Toast.makeText(this, R.string.updatedsuccess, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);

    }
}