package com.example.auctionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Update;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auctionapp.FirebaseDatabase.Tabele.Product;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseRegistrar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class AdminProductActivity extends AppCompatActivity {

    private static final int IMG_KEY=1;
    private Button btnUpload;
    private Button btnSaveProduct;
    private EditText etDenumire;
    private EditText etPret;
    private EditText etDescriere;
    private TextView tvUploads;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri imgUri;
    private static int NOD=1;

    //storage variables
    private StorageReference storageReference;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);
        initComponents();
    }

    private void initComponents() {
        btnUpload = (Button)findViewById(R.id.iOS_admin_uploadimg_btn);
        btnSaveProduct = (Button)findViewById(R.id.iOS_admin_save_btn);
        etDenumire = (EditText)findViewById(R.id.iOS_admin_prodden_tiet);
        etPret = (EditText)findViewById(R.id.iOS_admin_prodpret_tiet);
        etDescriere = (EditText)findViewById(R.id.iOS_admin_proddesc_tiet);
        imageView = (ImageView)findViewById(R.id.iOS_admin_prod_imgView);
        progressBar = (ProgressBar)findViewById(R.id.iOS_admin_prod_progbar);
        tvUploads = (TextView)findViewById(R.id.iOS_admin_tv_uploads);

        storageReference = FirebaseStorage.getInstance().getReference("images/");
        databaseReference = FirebaseDatabase.getInstance().getReference("images");

        //onlick listeners
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChoose();
            }
        });

        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });

        tvUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(AdminProductActivity.this,ProductActivity.class));
                displayProd();
            }
        });
    }

    private void displayProd() {
        startActivity(new Intent(AdminProductActivity.this,productActivity.class));
    }

    //pentru extensia pozei
    private String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void saveProduct() {
        if(imgUri!=null){
            final StorageReference imagine = storageReference.child("Imagine_"+System.currentTimeMillis()+"."+getExtension(imgUri));
            imagine.putFile(imgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return imagine.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        Log.e("TAG", "then: " + downloadUri.toString());


                        Product product = new Product(etDenumire.getText().toString().trim(),
                                                     etPret.getText().toString().trim(),
                                                     etDescriere.getText().toString().trim(),
                                downloadUri.toString());
                        //String imgID = databaseReference.push().getKey();
                        String imgID= String.valueOf(NOD);
                        databaseReference.child(imgID).setValue(product);
                        NOD++;
                        Toast.makeText(AdminProductActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        Toast.makeText(AdminProductActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Please upload image!", Toast.LENGTH_SHORT).show();
        }
    }

    private void imageChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_KEY && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgUri = data.getData();
            Picasso.with(this).load(imgUri).into(imageView);
            //Picasso.get().load(imgUri).into(imageView);
        }
    }
}