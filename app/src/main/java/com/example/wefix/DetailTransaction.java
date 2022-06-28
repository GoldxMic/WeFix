package com.example.wefix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailTransaction extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;
    private Button selesai;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    private String userID;
    TextView nama, keteranganuser,kategoritxt, hargatxt, alamatdatabase,phone,statustxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        userID = user.getUid();
        String productid = getIntent().getStringExtra("productid");
        String tokoid = getIntent().getStringExtra("tokoid");
        String addresstoko = getIntent().getStringExtra("addresstoko");
        String addressuser = getIntent().getStringExtra("addressuser");
        String phonetoko = getIntent().getStringExtra("phonetoko");
        String phoneuser = getIntent().getStringExtra("phoneuser");
        String status = getIntent().getStringExtra("status");
        String fullnametoko = getIntent().getStringExtra("fullnametoko");
        String fullnameuser = getIntent().getStringExtra("fullnameuser");
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
        alamatdatabase = (TextView) findViewById(R.id.alamatdatabase);
        phone = (TextView) findViewById(R.id.phone);
        statustxt = (TextView) findViewById(R.id.status);
        nama.setText("Nama Toko: " + fullnametoko);
        keteranganuser.setText(keterangan);
        kategoritxt.setText("Kategori : " + kategori);
        hargatxt.setText("Harga : Rp " + harga);
        alamatdatabase.setText(addresstoko);
        phone.setText("Nomor Telefon : " + phonetoko);
        statustxt.setText("Status : " + status);
        selesai =(Button) findViewById(R.id.selesai);
        if(status.equals("Diproses")){
            selesai.setText("Kirim");
        }
        if(status.equals("Diproses")||status.equals("Perbaikan selesai, dalam pengiriman")){
            selesai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(status.equals("Diproses")){
                        reference.child("Products").child("Toko").child(tokoid).child(productid).child("status").setValue("Dikirim");
                        reference.child("Products").child(userID).child(productid).child("status").setValue("Dikirim");
                        Toast.makeText(DetailTransaction.this,"Success, please wait",Toast.LENGTH_LONG).show();
                        finish();

                    }else if(status.equals("Perbaikan selesai, dalam pengiriman")){
                        reference.child("Products").child("Toko").child(tokoid).child(productid).child("status").setValue("Selesai");
                        reference.child("Products").child(userID).child(productid).child("status").setValue("Selesai");

                        calendar = Calendar.getInstance();
                        dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                        date = dateFormat.format(calendar.getTime());
                        reference.child("Products").child("Toko").child(tokoid).child(productid).child("end_date").setValue(date);
                        reference.child("Products").child(userID).child(productid).child("end_date").setValue(date);
                        Toast.makeText(DetailTransaction.this,"Success, please wait",Toast.LENGTH_LONG).show();
                        finish();

                    }
                }
            });
        }else{
            selesai.setVisibility(View.GONE);
        }
    }
}