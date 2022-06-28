package com.example.wefix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;

    ArrayList<OrderData> list;

    public MyAdapter(Context context, ArrayList<OrderData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemlistproduct,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderData data = list.get(position);
        holder.kategori.setText(data.getKategori());
        holder.harga.setText("Rp " + data.getHarga() );
        holder.tanggal.setText(data.getStart_date());
        String productid = data.getProductid();
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView kategori,harga,tanggal;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            kategori = itemView.findViewById(R.id.kategorilist);
            harga = itemView.findViewById(R.id.statuslist);
            tanggal = itemView.findViewById(R.id.tanggallisst);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}
