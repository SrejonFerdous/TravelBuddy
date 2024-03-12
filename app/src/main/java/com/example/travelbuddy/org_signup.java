package com.example.travelbuddy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class org_signup extends AppCompatActivity {

    EditText name_et,username_et,emailorphone_et,password_et;
    Button b1,b2;
    TextView sign_in;
    Animation scaleDown;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_signup);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Travel Buddy");
        Window window=getWindow();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) window.setStatusBarColor(getColor(R.color.black));
        else window.setStatusBarColor(getResources().getColor(R.color.black));
        b1=(Button) findViewById(R.id.org_signup_b1);
        b2=(Button) findViewById(R.id.org_signup_b2);
        progressBar = (ProgressBar) findViewById(R.id.org_signup_pb1);
        sign_in=(TextView) findViewById(R.id.org_signup_tv10);
        name_et=(EditText) findViewById(R.id.org_signup_et1);
        username_et=(EditText) findViewById(R.id.org_signup_et2);
        emailorphone_et=(EditText) findViewById(R.id.org_signup_et3);
        password_et=(EditText) findViewById(R.id.org_signup_et4);

        scaleDown= AnimationUtils.loadAnimation(this, R.anim.scale_down);
        sign_in.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN) sign_in.startAnimation(scaleDown);
                return false;
            }
        });
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
                Intent intent=new Intent(org_signup.this,organization_login.class);
                startActivity(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name_et.getText().toString().trim().isEmpty() || username_et.getText().toString().trim().isEmpty() || emailorphone_et.getText().toString().trim().isEmpty()
                        || password_et.getText().toString().trim().isEmpty()){
                    new AlertDialog.Builder(org_signup.this)
                            .setTitle("Fillup all Boxes")
                            .setMessage("Fillup all boxes to sign up")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                b2.setVisibility(View.GONE);
                String name,username,emailorphone,password;
                name=name_et.getText().toString();
                username=username_et.getText().toString().trim();
                emailorphone=emailorphone_et.getText().toString().trim();
                password=password_et.getText().toString().trim();
                if(Patterns.EMAIL_ADDRESS.matcher(emailorphone).matches()){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailorphone,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    b2.setVisibility(View.VISIBLE);
                                    if(task.isSuccessful()){
                                        FirebaseDatabase.getInstance().getReference("orgUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                                                .setValue(new orgUser(name,username,emailorphone,password))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(Task<Void> task) {
                                                        Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(Exception e) {
                                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        Intent intent=new Intent(org_signup.this,organization_login.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        new AlertDialog.Builder(org_signup.this)
                                                .setTitle("Authentication Failed")
                                                .setMessage("Something went wrong")
                                                .setCancelable(true)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent=new Intent(org_signup.this,log_in.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).show();
                                    }
                                }
                            });
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    b2.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"provide email in correct format",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}