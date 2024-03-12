package com.example.travelbuddy;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class car_rental extends Fragment {

    View view;
    RecyclerView recyclerView;
    List<carData> carDetails;
    carAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_car_rental, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.car_rental_rv1);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_bar);
        AlertDialog dialog=builder.create();
        dialog.show();
        carDetails=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("carData")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        carDetails.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            carDetails.add(dataSnapshot.getValue(carData.class));
                        }
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
        adapter=new carAdapter(getActivity(), carDetails);
        recyclerView.setAdapter(adapter);
        dialog.show();

        return view;
    }
}