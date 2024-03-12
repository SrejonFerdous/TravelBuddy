package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class org_app_main extends AppCompatActivity {
    AnimatedBottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_app_main);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Travel Buddy");
        Window window=getWindow();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) window.setStatusBarColor(getColor(R.color.another_app_color));
        else window.setStatusBarColor(getResources().getColor(R.color.another_app_color));
        bottomBar = (AnimatedBottomBar) findViewById(R.id.org_app_main_bottomNevigationView);
        switchFragment(new org_home(),1);
        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NotNull AnimatedBottomBar.Tab tab1) {
                switch(tab1.getId()){
                    case R.id.org_home:
                        switchFragment(new home(),1);
                        break;
                    case R.id.org_add_post:
                        switchFragment(new org_add_post(),1);
                        break;
                    case R.id.org_add_car:
                        switchFragment(new org_add_car(),1);
                        break;
                    case R.id.org_profile:
                        switchFragment(new org_profile(),1);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {

            }
        });
    }
    private void switchFragment(Fragment fragment, int flag){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        if(flag==1){
            fragmentTransaction.replace(R.id.fragmentContainerView,fragment).addToBackStack(null).commit();
        }else{
            fragmentTransaction.add(R.id.fragmentContainerView,fragment);
            fragmentManager.popBackStack(R.id.org_home2,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.addToBackStack("org_home");
        }
    }
}