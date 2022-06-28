package com.example.wefix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Permintaan extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference, referenceproduct;
    private EditText keterangan, phoneEditText, harga;
    private Button order;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date,fullnameuser,productid;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String userID;
    private Spinner toko;
    private Toko tokoid;
    private ImageButton addphoto;
    private Uri imageUri;
    OrderData orderData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        EditText addressEditText = (EditText) findViewById(R.id.alamattoko);
        phoneEditText = (EditText) findViewById(R.id.phonetoko);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String address = userProfile.address;
                    String phone = userProfile.phone;
                    fullnameuser = userProfile.fullname;
                    addressEditText.setText(address, TextView.BufferType.EDITABLE);
                    phoneEditText.setText(phone, TextView.BufferType.EDITABLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Permintaan.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
        String[] arraySpinner = new String[] {
                "Handphone", "Computer", "Television", "Laptop", "Tablet", "Printer"
        };
        Spinner s = (Spinner) findViewById(R.id.spinnerorder);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Permintaan.this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        List<Toko> userList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    String key = dsp.getKey();
                    String fullname = dsp.child("fullname").getValue().toString();
                    Toko toko = new Toko(fullname, key);
                    if(!userID.equals(key)){
                        userList.add(toko);
                    }

                }
                toko = (Spinner) findViewById(R.id.tokospinner);
                ArrayAdapter<Toko> tokoadapter = new ArrayAdapter<Toko> (Permintaan.this, android.R.layout.simple_spinner_item, userList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                toko.setAdapter(tokoadapter);
                tokoid = (Toko) toko.getSelectedItem();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        referenceproduct = FirebaseDatabase.getInstance().getReference().child("Products");
        keterangan = (EditText) findViewById(R.id.keteranganuser);
        harga = (EditText) findViewById(R.id.hargatoko);
        order = (Button) findViewById(R.id.permintaanbtn);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        date = dateFormat.format(calendar.getTime());
        orderData = new OrderData();
        addphoto = (ImageButton) findViewById(R.id.addphoto);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
        final ProgressDialog pd = new ProgressDialog(this);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setTitle("Uploading image...");
                pd.show();
                productid = referenceproduct.push().getKey();

                final StorageReference ref = storageReference.child("image/products/"+productid+".jpg");
                ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        orderData.setAddress(addressEditText.getText().toString());
                        orderData.setKeterangan(keterangan.getText().toString());
                        String value = harga.getText().toString();
                        int finalvalue =Integer.parseInt(value);
                        orderData.setHarga(finalvalue);
                        orderData.setFullname_toko(tokoid.getFullname());
                        orderData.setFullname_user(fullnameuser);
                        orderData.setAddress_toko("");
                        orderData.setPhone_toko("");
                        orderData.setUserid(userID);
                        orderData.setTokoid(tokoid.getId());
                        orderData.setPhone(phoneEditText.getText().toString());
                        orderData.setStatus("Menunggu penawaran");
                        orderData.setKategori(s.getSelectedItem().toString());
                        orderData.setStart_date(date);
                        orderData.setEnd_date("");
                        orderData.setProductid(productid);
                        referenceproduct.child("Toko").child(tokoid.getId()).child(productid).setValue(orderData);
                        referenceproduct.child(userID).child(productid).setValue(orderData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    finish();
                                    Toast.makeText(Permintaan.this, "Order success, please wait", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Permintaan.this, "Failed to order! Try again!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Permintaan.this,"Failed to upload!",Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progress : " + (int) progressPercent + "%");
                    }
                });
            }
        });
    }
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.getAction());
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            addphoto.setImageURI(imageUri);
        }
    }

}