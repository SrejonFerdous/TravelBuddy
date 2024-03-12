package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class login_credentials extends AppCompatActivity {
    LinearLayout ll1,ll2;
    Animation fadeIn,bottom;
    Button b1,b2,b3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_credentials);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Travel Buddy");
        ll1=(LinearLayout) findViewById(R.id.login_cred_ll1);
        ll2=(LinearLayout) findViewById(R.id.login_cred_ll2);
        b1=(Button) findViewById(R.id.login_cred_b1);
        b2=(Button) findViewById(R.id.login_cred_b2);
        b3=(Button) findViewById(R.id.login_cred_b3);
        Window window=getWindow();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) window.setStatusBarColor(getColor(R.color.app_color));
        else window.setStatusBarColor(getResources().getColor(R.color.app_color));
        fadeIn= AnimationUtils.loadAnimation(this,R.anim.fade_in);
        bottom=AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        ll1.setAnimation(fadeIn);
        ll2.setAnimation(bottom);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login_credentials.this,log_in.class);
                startActivity(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login_credentials.this,admin_login.class);
                startActivity(intent);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login_credentials.this,organization_login.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed(){
        finishAffinity();
    }
}