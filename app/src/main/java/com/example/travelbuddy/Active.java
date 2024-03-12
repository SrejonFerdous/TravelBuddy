package com.example.travelbuddy;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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

public class Active extends Fragment {

    View view;
    myAdapter activeAdapter;
    RecyclerView recyclerView;
    List<hotelData> activeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_active, container, false);

        recyclerView=(RecyclerView) view.findViewById(R.id.active_rv1);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog dialog=new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setView(R.layout.progress_bar)
                .create();
        dialog.show();

        activeList=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("hotelData")
                .orderByChild("reserved").equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        activeList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            activeList.add(dataSnapshot.getValue(hotelData.class));
                        }
                        activeAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(getActivity(),"Book error :"+error.getMessage(),Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
        activeAdapter=new myAdapter(getContext(), activeList);
        recyclerView.setAdapter(activeAdapter);
        dialog.show();

        return view;
    }
}