package com.example.travelbuddy;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.constraintlayout.motion.widget.Key;
import androidx.constraintlayout.motion.widget.KeyTimeCycle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import nl.joery.animatedbottombar.AnimatedBottomBar;


public class home extends Fragment {

    View view;
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_home, container, false);
        viewPager=(ViewPager) view.findViewById(R.id.viewPager);
        tabLayout=(TabLayout) view.findViewById(R.id.tabLayout);
        //switchFragment(new stays(),1);
        SectionPagerAdapter sectionPagerAdapter=new SectionPagerAdapter(getChildFragmentManager());
        sectionPagerAdapter.addFragment(new stays(),"stays");
        sectionPagerAdapter.addFragment(new car_rental(),"car_rental");
        sectionPagerAdapter.addFragment(new attractions(),"attractions");
        viewPager.setAdapter(sectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(isEnabled()){
                    Toast.makeText(getActivity(), "Exit", Toast.LENGTH_SHORT).show();
                    setEnabled(false);
                    getActivity().finishAffinity();
                }
            }
        });
        return view;
    }


}