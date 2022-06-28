package com.example.wefix;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TokoTransactionAdapter extends RecyclerView.Adapter<TokoTransactionAdapter.MyViewHolder> {

    Context context;

    ArrayList<OrderData> list;

    public TokoTransactionAdapter(Context context, ArrayList<OrderData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemlisttransaction,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderData data = list.get(position);
        String productid = data.getProductid();
        String userid = data.getUserid();
        String addresstoko = data.getAddress_toko();
        String addressuser = data.getAddress();
        String phonetoko = data.getPhone_toko();
        String status = data.getStatus();
        String phoneuser = data.getPhone();
        String fullnametoko = data.getFullname_toko();
        String fullnameuser = data.getFullname_user();
        String keterangan = data.getKeterangan();
        String kategori = data.getKategori();
        Integer harga = data.getHarga();
        holder.kategori.setText(data.getKategori());
        holder.status.setText(data.getStatus());
        holder.harga.setText("Rp " + data.getHarga() );
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
                    holder.imageView.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e){
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(),DetailTokoTransaction.class);
                intent.putExtra("productid",productid);
                intent.putExtra("addressuser",addressuser);
                intent.putExtra("addresstoko",addresstoko);
                intent.putExtra("phoneuser",phoneuser);
                intent.putExtra("phonetoko",phonetoko);
                intent.putExtra("status",status);
                intent.putExtra("idpembeli",userid);
                intent.putExtra("fullnametoko",fullnametoko);
                intent.putExtra("fullnameuser",fullnameuser);
                intent.putExtra("keterangan",keterangan);
                intent.putExtra("kategori",kategori);
                intent.putExtra("harga",harga);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView kategori,status, harga;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            kategori = itemView.findViewById(R.id.kategorilist);
            status = itemView.findViewById(R.id.statuslist);
            harga = itemView.findViewById(R.id.hargalist);
            imageView = itemView.findViewById(R.id.imageView);


        }
    }
}
