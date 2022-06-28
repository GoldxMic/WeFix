package com.example.wefix;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class DetailPermintaan extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;
    private Button penawaranbtn;

    private String userID;
    TextView nama, keteranganuser,kategoritxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.penawaran);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        userID = user.getUid();
        String productid = getIntent().getStringExtra("productid");
        String idpembeli = getIntent().getStringExtra("userid");
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
        EditText hargatoko = (EditText) findViewById(R.id.hargatoko);
        EditText addressEditText = (EditText) findViewById(R.id.alamattoko);
        EditText phoneEditText = (EditText) findViewById(R.id.phonetoko);
        nama.setText("Nama Pemesan: " + fullname);
        keteranganuser.setText(keterangan);
        kategoritxt.setText("Kategori : " + kategori);
        hargatoko.setText(""+harga,TextView.BufferType.EDITABLE);
        reference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String address = userProfile.address;
                    String phone = userProfile.phone;
                    addressEditText.setText(address, TextView.BufferType.EDITABLE);
                    phoneEditText.setText(phone, TextView.BufferType.EDITABLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailPermintaan.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
        penawaranbtn =(Button) findViewById(R.id.penwarannbtn);
        penawaranbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("Products").child("Toko").child(userID).child(productid).child("address_toko").setValue(addressEditText.getText().toString());
                reference.child("Products").child(idpembeli).child(productid).child("address_toko").setValue(addressEditText.getText().toString());

                reference.child("Products").child("Toko").child(userID).child(productid).child("phone_toko").setValue(phoneEditText.getText().toString());
                reference.child("Products").child(idpembeli).child(productid).child("phone_toko").setValue(phoneEditText.getText().toString());

                String value = hargatoko.getText().toString();
                int finalvalue =Integer.parseInt(value);
                reference.child("Products").child("Toko").child(userID).child(productid).child("harga").setValue(finalvalue);
                reference.child("Products").child(idpembeli).child(productid).child("harga").setValue(finalvalue);

                reference.child("Products").child("Toko").child(userID).child(productid).child("status").setValue("Menunggu permintaan");
                reference.child("Products").child(idpembeli).child(productid).child("status").setValue("Menunggu permintaan");
                finish();
                
                Toast.makeText(DetailPermintaan.this,"Order success, please wait",Toast.LENGTH_LONG).show();
            }
        });
    }
}