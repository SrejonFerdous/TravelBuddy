package com.example.travelbuddy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity {
    private EditText[] editTexts;
    private Button[] buttons;
    private TextView resendOtp,textView;
    private ProgressBar progressBar;
    private String verificationID;
    private LinearLayout linearLayout;
    String name,username,emailormobile,password;
    Animation scaleDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Travel Buddy");
        Window window=getWindow();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) window.setStatusBarColor(getColor(R.color.black));
        else window.setStatusBarColor(getResources().getColor(R.color.black));
        editTexts = new EditText[6];
        buttons = new Button[11];
        scaleDown=AnimationUtils.loadAnimation(this,R.anim.scale_down);
        textView=(TextView) findViewById(R.id.otp_tv1);
        resendOtp=(TextView) findViewById(R.id.otp_resendOtp);
        progressBar=(ProgressBar) findViewById(R.id.otp_pb1);
        linearLayout=(LinearLayout) findViewById(R.id.otp_ll1);
        editTexts[0]=(EditText) findViewById(R.id.otp_et1);
        editTexts[1]=(EditText) findViewById(R.id.otp_et2);
        editTexts[2]=(EditText) findViewById(R.id.otp_et3);
        editTexts[3]=(EditText) findViewById(R.id.otp_et4);
        editTexts[4]=(EditText) findViewById(R.id.otp_et5);
        editTexts[5]=(EditText) findViewById(R.id.otp_et6);
        buttons[0]=(Button) findViewById(R.id.otp_b0);
        buttons[1]=(Button) findViewById(R.id.otp_b1);
        buttons[2]=(Button) findViewById(R.id.otp_b2);
        buttons[3]=(Button) findViewById(R.id.otp_b3);
        buttons[4]=(Button) findViewById(R.id.otp_b4);
        buttons[5]=(Button) findViewById(R.id.otp_b5);
        buttons[6]=(Button) findViewById(R.id.otp_b6);
        buttons[7]=(Button) findViewById(R.id.otp_b7);
        buttons[8]=(Button) findViewById(R.id.otp_b8);
        buttons[9]=(Button) findViewById(R.id.otp_b9);
        buttons[10]=(Button) findViewById(R.id.otp_bcross);
        verificationID=getIntent().getStringExtra("verificationid");
        name=getIntent().getStringExtra("name");
        username=getIntent().getStringExtra("username");
        emailormobile=getIntent().getStringExtra("mobile");
        password=getIntent().getStringExtra("password");
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+880" + getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        otp.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(String newVerificationID, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationID=newVerificationID;
                                Toast.makeText(getApplicationContext(), "OTP sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
        textView.setText("We sent you a one-time password to the following number :" + getIntent().getStringExtra("mobile"));
        buttons[0].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) buttons[0].startAnimation(scaleDown);
                return false;
            }
        });
        buttons[1].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) buttons[1].startAnimation(scaleDown);
                return false;
            }
        });
        buttons[2].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) buttons[2].startAnimation(scaleDown);
                return false;
            }
        });
        buttons[3].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) buttons[3].startAnimation(scaleDown);
                return false;
            }
        });
        buttons[4].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) buttons[4].startAnimation(scaleDown);
                return false;
            }
        });
        buttons[5].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) buttons[5].startAnimation(scaleDown);
                return false;
            }
        });
        buttons[6].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) buttons[6].startAnimation(scaleDown);
                return false;
            }
        });
        buttons[7].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) buttons[7].startAnimation(scaleDown);
                return false;
            }
        });
        buttons[8].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) buttons[8].startAnimation(scaleDown);
                return false;
            }
        });
        buttons[9].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) buttons[9].startAnimation(scaleDown);
                return false;
            }
        });
        buttons[10].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) buttons[10].startAnimation(scaleDown);
                return false;
            }
        });
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTexts[0].hasFocus()) editTexts[0].setText("0");
                else if(editTexts[1].hasFocus()) editTexts[1].setText("0");
                else if(editTexts[2].hasFocus()) editTexts[2].setText("0");
                else if(editTexts[3].hasFocus()) editTexts[3].setText("0");
                else if(editTexts[4].hasFocus()) editTexts[4].setText("0");
                else if(editTexts[5].hasFocus()) editTexts[5].setText("0");
            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTexts[0].hasFocus()) editTexts[0].setText("1");
                else if(editTexts[1].hasFocus()) editTexts[1].setText("1");
                else if(editTexts[2].hasFocus()) editTexts[2].setText("1");
                else if(editTexts[3].hasFocus()) editTexts[3].setText("1");
                else if(editTexts[4].hasFocus()) editTexts[4].setText("1");
                else if(editTexts[5].hasFocus()) editTexts[5].setText("1");
            }
        });
        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTexts[0].hasFocus()) editTexts[0].setText("2");
                else if(editTexts[1].hasFocus()) editTexts[1].setText("2");
                else if(editTexts[2].hasFocus()) editTexts[2].setText("2");
                else if(editTexts[3].hasFocus()) editTexts[3].setText("2");
                else if(editTexts[4].hasFocus()) editTexts[4].setText("2");
                else if(editTexts[5].hasFocus()) editTexts[5].setText("2");
            }
        });
        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTexts[0].hasFocus()) editTexts[0].setText("3");
                else if(editTexts[1].hasFocus()) editTexts[1].setText("3");
                else if(editTexts[2].hasFocus()) editTexts[2].setText("3");
                else if(editTexts[3].hasFocus()) editTexts[3].setText("3");
                else if(editTexts[4].hasFocus()) editTexts[4].setText("3");
                else if(editTexts[5].hasFocus()) editTexts[5].setText("3");
            }
        });
        buttons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTexts[0].hasFocus()) editTexts[0].setText("4");
                else if(editTexts[1].hasFocus()) editTexts[1].setText("4");
                else if(editTexts[2].hasFocus()) editTexts[2].setText("4");
                else if(editTexts[3].hasFocus()) editTexts[3].setText("4");
                else if(editTexts[4].hasFocus()) editTexts[4].setText("4");
                else if(editTexts[5].hasFocus()) editTexts[5].setText("4");
            }
        });
        buttons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTexts[0].hasFocus()) editTexts[0].setText("5");
                else if(editTexts[1].hasFocus()) editTexts[1].setText("5");
                else if(editTexts[2].hasFocus()) editTexts[2].setText("5");
                else if(editTexts[3].hasFocus()) editTexts[3].setText("5");
                else if(editTexts[4].hasFocus()) editTexts[4].setText("5");
                else if(editTexts[5].hasFocus()) editTexts[5].setText("5");
            }
        });
        buttons[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTexts[0].hasFocus()) editTexts[0].setText("6");
                else if(editTexts[1].hasFocus()) editTexts[1].setText("6");
                else if(editTexts[2].hasFocus()) editTexts[2].setText("6");
                else if(editTexts[3].hasFocus()) editTexts[3].setText("6");
                else if(editTexts[4].hasFocus()) editTexts[4].setText("6");
                else if(editTexts[5].hasFocus()) editTexts[5].setText("6");
            }
        });
        buttons[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTexts[0].hasFocus()) editTexts[0].setText("7");
                else if(editTexts[1].hasFocus()) editTexts[1].setText("7");
                else if(editTexts[2].hasFocus()) editTexts[2].setText("7");
                else if(editTexts[3].hasFocus()) editTexts[3].setText("7");
                else if(editTexts[4].hasFocus()) editTexts[4].setText("7");
                else if(editTexts[5].hasFocus()) editTexts[5].setText("7");
            }
        });
        buttons[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTexts[0].hasFocus()) editTexts[0].setText("8");
                else if(editTexts[1].hasFocus()) editTexts[1].setText("8");
                else if(editTexts[2].hasFocus()) editTexts[2].setText("8");
                else if(editTexts[3].hasFocus()) editTexts[3].setText("8");
                else if(editTexts[4].hasFocus()) editTexts[4].setText("8");
                else if(editTexts[5].hasFocus()) editTexts[5].setText("8");
            }
        });
        buttons[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTexts[0].hasFocus()) editTexts[0].setText("9");
                else if(editTexts[1].hasFocus()) editTexts[1].setText("9");
                else if(editTexts[2].hasFocus()) editTexts[2].setText("9");
                else if(editTexts[3].hasFocus()) editTexts[3].setText("9");
                else if(editTexts[4].hasFocus()) editTexts[4].setText("9");
                else if(editTexts[5].hasFocus()) editTexts[5].setText("9");
            }
        });
        buttons[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTexts[0].hasFocus()) {
                    editTexts[0].getText().clear();
                    editTexts[0].requestFocus();
                }
                else if(editTexts[1].hasFocus()){
                    editTexts[1].getText().clear();
                    editTexts[0].requestFocus();
                }
                else if(editTexts[2].hasFocus()){
                    editTexts[2].getText().clear();
                    editTexts[1].requestFocus();
                }
                else if(editTexts[3].hasFocus()){
                    editTexts[3].getText().clear();
                    editTexts[2].requestFocus();
                }else if(editTexts[4].hasFocus()){
                    editTexts[4].getText().clear();
                    editTexts[3].requestFocus();
                }else if(editTexts[5].hasFocus()){
                    editTexts[5].getText().clear();
                    editTexts[4].requestFocus();
                }
            }
        });
        editTexts[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editTexts[0].getText().toString().length()==1) editTexts[1].requestFocus();
                else if(editTexts[0].getText().toString().length()==0) editTexts[0].requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editTexts[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editTexts[1].getText().toString().length()==1) editTexts[2].requestFocus();
                else if(editTexts[1].getText().toString().length()==0) editTexts[0].requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editTexts[2].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editTexts[2].getText().toString().length()==1) editTexts[3].requestFocus();
                else if(editTexts[2].getText().toString().length()==0) editTexts[1].requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editTexts[3].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editTexts[3].getText().toString().length()==1) editTexts[4].requestFocus();
                else if(editTexts[3].getText().toString().length()==0) editTexts[2].requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editTexts[4].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editTexts[4].getText().toString().length()==1) editTexts[5].requestFocus();
                else if(editTexts[4].getText().toString().length()==0) editTexts[3].requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editTexts[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editTexts[5].getText().toString().length()==1
                        && editTexts[4].getText().toString().length()==1
                        && editTexts[3].getText().toString().length()==1
                        && editTexts[2].getText().toString().length()==1
                        && editTexts[1].getText().toString().length()==1
                        && editTexts[0].getText().toString().length()==1){
                    if(verificationID!=null){
                        progressBar.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
                        String code=editTexts[0].getText().toString()+editTexts[1].getText().toString()+editTexts[2].getText().toString()+
                                editTexts[3].getText().toString()+editTexts[4].getText().toString()+editTexts[5].getText().toString();
                        PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(
                                verificationID,code
                        );
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        linearLayout.setVisibility(View.VISIBLE);
                                        if(task.isSuccessful()){
                                            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                                                    .setValue(new User(name,username,emailormobile,password,null))
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(Exception e) {
                                                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                            Intent intent=new Intent(otp.this,log_in.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }else{
                                            new AlertDialog.Builder(otp.this)
                                                    .setTitle("Wrong OTP")
                                                    .setMessage("The OTP you entered is wrong. \n Enter correct OTP to create account")
                                                    .setCancelable(true)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                        }
                                                    }).show();
                                        }
                                    }
                                });
                    }
                }
                else if(editTexts[5].getText().toString().length()==0) editTexts[4].requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}