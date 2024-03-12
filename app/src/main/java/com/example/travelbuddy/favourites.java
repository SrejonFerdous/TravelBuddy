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

public class favourites extends Fragment {
    View view;
    RecyclerView recyclerView;
    List<hotelData> favDetails;
    myAdapter favAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_favourites, container, false);

        recyclerView=(RecyclerView) view.findViewById(R.id.favourites_rv1);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_bar);
        AlertDialog dialog=builder.create();
        dialog.show();

        favDetails=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("hotelData")
                .orderByChild("favourite").equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        favDetails.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            favDetails.add(dataSnapshot.getValue(hotelData.class));
                        }
                        favAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(getActivity(),"fav error :"+error.getMessage(),Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
        favAdapter=new myAdapter(getActivity(),favDetails);
        recyclerView.setAdapter(favAdapter);
        dialog.show();
        return view;
    }
}