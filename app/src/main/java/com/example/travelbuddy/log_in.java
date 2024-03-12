package com.example.travelbuddy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class log_in extends AppCompatActivity {

    Button b1,b2,b3;
    TextView t1;
    Animation scaleUp,scaleDown;
    EditText et1,et2;
    ProgressBar progressBar;
    String pattern="^(\\+880|0)[0-9]{10,11}$";
    private FirebaseAuth mAuth;
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent=new Intent(log_in.this,app_main.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Travel Buddy");
        Window window=getWindow();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) window.setStatusBarColor(getColor(R.color.black));
        else window.setStatusBarColor(getResources().getColor(R.color.black));
        b1=(Button) findViewById(R.id.login_b1);
        b2=(Button) findViewById(R.id.login_b2);
        b3=(Button) findViewById(R.id.login_b3);
        et1=(EditText) findViewById(R.id.login_et1);
        et2=(EditText) findViewById(R.id.login_et2);
        t1=(TextView) findViewById(R.id.login_tv1);
        mAuth=FirebaseAuth.getInstance();
        progressBar=(ProgressBar) findViewById(R.id.login_pb1);
        scaleUp=AnimationUtils.loadAnimation(this,R.anim.scale_up);
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
                Intent intent=new Intent(log_in.this,login_credentials.class);
                startActivity(intent);
                finish();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et1.getText().toString().trim().isEmpty()){
                    new AlertDialog.Builder(log_in.this)
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
                    new AlertDialog.Builder(log_in.this)
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
                String emailorphone,password;
                emailorphone=et1.getText().toString().trim();
                password=et2.getText().toString().trim();
                if(emailorphone.matches(pattern)){
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+880" + emailorphone,
                            60,
                            TimeUnit.SECONDS,
                            log_in.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                    mAuth.signInWithCredential(phoneAuthCredential)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(Task<AuthResult> task) {
                                                    progressBar.setVisibility(view.GONE);
                                                    b2.setVisibility(view.VISIBLE);
                                                    if(task.isSuccessful()){
                                                        Intent intent=new Intent(log_in.this,app_main.class);
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

                                @Override
                                public void onVerificationFailed(FirebaseException e) {
                                    progressBar.setVisibility(view.GONE);
                                    b2.setVisibility(view.VISIBLE);
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(String verificationID, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    progressBar.setVisibility(view.GONE);
                                    b2.setVisibility(view.VISIBLE);
                                    Intent intent=new Intent(log_in.this,otp.class);
                                    intent.putExtra("verificationid",verificationID);
                                    intent.putExtra("mobile",emailorphone);
                                    startActivity(intent);
                                }
                            }
                    );
                }else if(Patterns.EMAIL_ADDRESS.matcher(emailorphone).matches()){
                    mAuth.signInWithEmailAndPassword(emailorphone,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    progressBar.setVisibility(view.GONE);
                                    b2.setVisibility(view.VISIBLE);
                                    if(task.isSuccessful()){
                                        Intent intent=new Intent(log_in.this,app_main.class);
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
                }else{
                    progressBar.setVisibility(view.GONE);
                    b2.setVisibility(view.VISIBLE);
                    Toast.makeText(getApplicationContext(),"provide email or phone number in correct format",Toast.LENGTH_SHORT).show();
                }
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
                Intent intent=new Intent(log_in.this,sign_up.class);
                startActivity(intent);
            }
        });
    }
}
/* <Button
        android:layout_height="50dp"
        android:layout_width="50dp"
        android:id="@+id/org_login_b1"
        app:icon="@drawable/back_arrow"
        app:iconTint="@color/black"
        android:fontFamily="@font/font1"
        android:background="@color/full_transparent"
        />

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="@dimen/_5sdp"
        >

        <View
            android:id="@+id/login_app_shape"
            android:layout_width="@dimen/_80sdp"
            android:layout_height="@dimen/_80sdp"
            android:layout_marginStart="@dimen/_112sdp"
            android:background="@drawable/app_icon_bg" />
        <ImageView
            android:id="@+id/login_app_icon"
            android:layout_width="@dimen/_60sdp"
            android:layout_height="@dimen/_60sdp"
            app:tint="@color/white"
            android:layout_marginStart="@dimen/_123sdp"
            android:layout_marginTop="@dimen/_10sdp"
            android:src="@drawable/appicon"/>

    </RelativeLayout>

    <com.google.android.material.textfield.TextInputLayout
        style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="@dimen/_70sdp"
        android:layout_marginHorizontal="@dimen/_11sdp"
        android:hint="Email"
        >
        <com.google.android.material.textfield.TextInputEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:id="@+id/org_login_et1"
            android:background="@drawable/edittext_bg"
            android:fontFamily="@font/font1"
            android:padding="@dimen/_10sdp"
            android:textSize="@dimen/_16sdp"
            />
    </com.google.android.material.textfield.TextInputLayout>

    <com.google.android.material.textfield.TextInputLayout
        style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="@dimen/_15sdp"
        android:layout_marginHorizontal="@dimen/_11sdp"
        android:hint="Password"
        app:endIconMode="password_toggle"
        app:endIconTint="@color/app_color"
        >
        <com.google.android.material.textfield.TextInputEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@drawable/edittext_bg"
            android:inputType="textPassword"
            android:id="@+id/org_login_et2"
            android:padding="@dimen/_10sdp"
            android:fontFamily="@font/font1"
            android:textSize="@dimen/_16sdp" />
    </com.google.android.material.textfield.TextInputLayout>

    <TextView
        android:id="@+id/org_login_tv1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginStart="@dimen/_85sdp"
        android:layout_marginTop="@dimen/_10sdp"
        android:fontFamily="@font/font1"
        android:text="Forgot password?"
        android:textColor="@drawable/login_text_color"
        android:textSize="@dimen/_14sdp" />

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        <ProgressBar
            android:id="@+id/org_login_pb1"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:max="100"
            android:progress="50"
            android:indeterminateTint="@color/app_color"
            android:visibility="gone"
            />

        <Button
            android:id="@+id/org_login_b2"
            android:layout_width="@dimen/_280sdp"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="@dimen/_15sdp"
            android:background="@drawable/button_bg"
            android:fontFamily="@font/font1"
            android:text="Login"
            android:textSize="@dimen/_13sdp"
            app:backgroundTint="#1E4595" />
    </FrameLayout>

    <Button
        android:id="@+id/org_login_b3"
        android:layout_width="@dimen/_280sdp"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="@dimen/_105sdp"
        android:background="@drawable/button_bg2"
        android:fontFamily="@font/font2"
        android:text="Create new account"
        android:textColor="@drawable/button_text_color"
        android:textSize="@dimen/_12sdp"
        app:backgroundTint="#1E4595" /> */

/*        getSupportActionBar().hide();
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
                Intent intent=new Intent(organization_login.this,sign_up.class);
                startActivity(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(et1.getText().toString().trim(), et2.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
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
        });*/