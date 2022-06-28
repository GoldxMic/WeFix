package com.example.wefix;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TokoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TokoFragment extends Fragment {
    private FirebaseUser user;
    RecyclerView recyclerpermintaan, recyclerwait;
    DatabaseReference database, reference;
    MyAdapter myAdapter;
    TokoAdapter tokoAdapter;
    ArrayList<OrderData> waitlist, permintaanlist;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TokoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TokoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TokoFragment newInstance(String param1, String param2) {
        TokoFragment fragment = new TokoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_toko, container, false);
        recyclerpermintaan = view.findViewById(R.id.listbawah);
        recyclerwait = view.findViewById(R.id.listatas);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        database = FirebaseDatabase.getInstance().getReference("Products").child("Toko").child(userID);

        recyclerwait.setHasFixedSize(true);
        recyclerwait.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerpermintaan.setHasFixedSize(true);
        recyclerpermintaan.setLayoutManager(new LinearLayoutManager(getActivity()));

        waitlist = new ArrayList<>();
        myAdapter = new MyAdapter(getActivity(),waitlist);
        recyclerwait.setAdapter(myAdapter);

        permintaanlist = new ArrayList<>();
        tokoAdapter = new TokoAdapter(getActivity(),permintaanlist);
        recyclerpermintaan.setAdapter(tokoAdapter);

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    OrderData data = dataSnapshot.getValue(OrderData.class);
                    String status = data.getStatus();
                    if(status.equals("Menunggu penawaran")){
                        permintaanlist.add(data);
                    }else if(status.equals("Menunggu permintaan")){
                        waitlist.add(data);
                    }
                }
                myAdapter.notifyDataSetChanged();
                tokoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button toko = (Button) view.findViewById(R.id.tokobtn);
        toko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new OrderFragment());
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}