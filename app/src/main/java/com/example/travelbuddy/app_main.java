package com.example.travelbuddy;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class app_main extends AppCompatActivity {
    AnimatedBottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Travel Buddy");
        Window window=getWindow();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) window.setStatusBarColor(getColor(R.color.another_app_color));
        else window.setStatusBarColor(getResources().getColor(R.color.another_app_color));
        bottomBar = (AnimatedBottomBar) findViewById(R.id.app_main_bottomNevigationView);
        switchFragment(new home(),1);
        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NotNull AnimatedBottomBar.Tab tab1) {
                switch(tab1.getId()){
                    case R.id.home:
                        switchFragment(new home(),1);
                        break;
                    case R.id.search:
                        switchFragment(new search(),1);
                        break;
                    case R.id.favourites:
                        switchFragment(new favourites(),1);
                        break;
                    case R.id.bookings:
                        switchFragment(new bookings(),1);
                        break;
                    case R.id.profile:
                        switchFragment(new profile(),1);
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
    private void switchFragment(Fragment fragment,int flag){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        if(flag==1){
            fragmentTransaction.replace(R.id.fragmentContainerView,fragment).addToBackStack(null).commit();
        }else{
            fragmentTransaction.add(R.id.fragmentContainerView,fragment);
            fragmentManager.popBackStack(R.id.home2,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.addToBackStack("home");
        }
    }
}
/*    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/app_main_bottomNevigationView"
        android:layout_alignParentBottom="true"
        app:menu="@menu/main_app_menu"
        app:itemIconTint="@drawable/bottom_nev_icon_selector"
        app:itemTextColor="@drawable/bottom_nev_text_selector"
        app:itemRippleColor="@color/semi_transparent_black_edited"
        />*/