package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class admin_login extends AppCompatActivity {
    Button b1,b2,b3;
    EditText et1,et2,et3;
    TextView t1;
    Animation scaleDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Travel Buddy");
        Window window=getWindow();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) window.setStatusBarColor(getColor(R.color.black));
        else window.setStatusBarColor(getResources().getColor(R.color.black));
        b1=(Button) findViewById(R.id.adminlogin_b1);
        b2=(Button) findViewById(R.id.adminlogin_b2);
        b3=(Button) findViewById(R.id.adminlogin_b3);
        scaleDown= AnimationUtils.loadAnimation(this,R.anim.scale_down);
        b1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) b1.startAnimation(scaleDown);
                return false;
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_login.this,login_credentials.class);
                startActivity(intent);
            }
        });
        b3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) b3.startAnimation(scaleDown);
                return false;
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(admin_login.this,sign_up.class);
                startActivity(intent);
            }
        });
    }
}