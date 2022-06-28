package com.example.wefix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class DetailPenawaran extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;
    private Button bayar;

    private String userID;
    TextView nama, keteranganuser,kategoritxt, hargatxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_penawaran);user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        userID = user.getUid();
        String productid = getIntent().getStringExtra("productid");
        String idpenjual = getIntent().getStringExtra("userid");
        String fullname = getIntent().getStringExtra("fullname");
        String keterangan = getIntent().getStringExtra("keterangan");
        String kategori = getIntent().getStringExtra("kategori");
        Integer harga = getIntent().getIntExtra("harga",0);
        ImageView imageView = (ImageView) findViewById(R.id.addphoto);
        FirebaseStorage storage;
        StorageReference storageReference;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        try {
            final File localfile = File.createTempFile(productid,"jpg");
            storageReference.child("image/products/"+productid+".jpg").getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e){
            e.printStackTrace();
        }
        nama = (TextView) findViewById(R.id.nama);
        keteranganuser = (TextView) findViewById(R.id.keteranganuser);
        kategoritxt = (TextView) findViewById(R.id.kategoritxt);
        hargatxt = (TextView) findViewById(R.id.hargatxt);
        nama.setText("Nama Toko: " + fullname);
        keteranganuser.setText(keterangan);
        kategoritxt.setText("Kategori : " + kategori);
        hargatxt.setText("Harga : Rp " + harga);
        String[] arraySpinner = new String[] {
                "Cash"
        };
        Spinner s = (Spinner) findViewById(R.id.spinnermetode);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetailPenawaran.this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        bayar =(Button) findViewById(R.id.bayar);
        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("Products").child("Toko").child(idpenjual).child(productid).child("status").setValue("Diproses");
                reference.child("Products").child(userID).child(productid).child("status").setValue("Diproses");
                Toast.makeText(DetailPenawaran.this,"Order success, please wait",Toast.LENGTH_LONG).show();
                finish();
                
            }
        });
    }
}