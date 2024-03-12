package com.example.travelbuddy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class organization_login extends AppCompatActivity {

    Button b1,b2,b3;
    TextView tv1;
    Animation scaleUp,scaleDown;
    EditText et1,et2;
    ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent=new Intent(organization_login.this, org_app_main.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_login);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Travel Buddy");
        Window window=getWindow();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) window.setStatusBarColor(getColor(R.color.black));
        else window.setStatusBarColor(getResources().getColor(R.color.black));
        b1=(Button) findViewById(R.id.org_login_b1);
        b2=(Button) findViewById(R.id.org_login_b2);
        b3=(Button) findViewById(R.id.org_login_b3);
        et1=(EditText) findViewById(R.id.org_login_et1);
        et2=(EditText) findViewById(R.id.org_login_et2);
        tv1=(TextView) findViewById(R.id.org_login_tv1);
        progressBar=(ProgressBar) findViewById(R.id.org_login_pb1);
        scaleUp= AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown= AnimationUtils.loadAnimation(this,R.anim.scale_down);
        b1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) b1.startAnimation(scaleDown);
                return false;
            }
        });
        b3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) b3.startAnimation(scaleDown);
                return false;
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(organization_login.this,login_credentials.class);
                startActivity(intent);
                finish();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(organization_login.this,org_signup.class);
                startActivity(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et1.getText().toString().trim().isEmpty()){
                    new AlertDialog.Builder(organization_login.this)
                            .setTitle("Email or mobile number required")
                            .setMessage("Enter your email or mobile number to continue.")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                    et1.requestFocus();
                    return;
                }
                else if(et2.getText().toString().trim().isEmpty()){
                    new AlertDialog.Builder(organization_login.this)
                            .setTitle("Password Required")
                            .setMessage("Enter your password to continue.")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                    et2.requestFocus();
                    return;
                }
                progressBar.setVisibility(view.VISIBLE);
                b2.setVisibility(view.GONE);
                FirebaseAuth.getInstance().signInWithEmailAndPassword(et1.getText().toString().trim(), et2.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                progressBar.setVisibility(view.GONE);
                                b2.setVisibility(view.VISIBLE);
                                if(task.isSuccessful()){
                                    Intent intent=new Intent(organization_login.this, org_app_main.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(), "wrong email or password", Toast.LENGTH_SHORT).show();
                                    et1.getText().clear();
                                    et2.getText().clear();
                                    et1.requestFocus();
                                }
                            }
                        });
            }
        });
    }
}