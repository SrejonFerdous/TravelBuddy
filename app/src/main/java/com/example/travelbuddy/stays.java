package com.example.travelbuddy;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class stays extends Fragment {

    View view;
    RecyclerView recyclerView;
    List<hotelData> details;
    myAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stays, container, false);

        recyclerView=(RecyclerView) view.findViewById(R.id.stays_rv1);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_bar);
        AlertDialog dialog=builder.create();
        dialog.show();

        details = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("hotelData")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        details.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            hotelData data = dataSnapshot.getValue(hotelData.class);
                            details.add(data);
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

        adapter = new myAdapter(getActivity(), details);
        recyclerView.setAdapter(adapter);
        dialog.show();

        return view;
    }
}