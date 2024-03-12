package com.example.travelbuddy;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class bookings extends Fragment {
    View view;
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_bookings, container, false);
        viewPager=(ViewPager) view.findViewById(R.id.book_viewPager);
        tabLayout=(TabLayout) view.findViewById(R.id.book_tabLayout);
        SectionPagerAdapter sectionPagerAdapter=new SectionPagerAdapter(getChildFragmentManager());
        sectionPagerAdapter.addFragment(new Active(),"Active");
        sectionPagerAdapter.addFragment(new Past(),"Past");
        sectionPagerAdapter.addFragment(new Canceled(),"Canceled");
        viewPager.setAdapter(sectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}