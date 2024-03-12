package com.example.travelbuddy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class sign_up extends AppCompatActivity {

    private  static final int RQ_GOOGLE_SIGN_IN=1;
    private  static final String TAG="GOOGLEAUTH";
    Button b1,b2;
    EditText name_et,username_et,emailorphone_et,password_et;
    RelativeLayout google, facebook;
    //LoginButton facebook;
    TextView sign_in;
    ProgressBar progressBar;
    private GraphRequest graphRequest;
    Animation scaleDown;
    GoogleSignInClient googleSignInClient;
    CallbackManager callbackManager;
    Dialog dialog;
    String pattern="^(\\+880|0)[0-9]{10,11}$";
    private String fName, fEmail, fPassword, fImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Travel Buddy");
        Window window=getWindow();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) window.setStatusBarColor(getColor(R.color.black));
        else window.setStatusBarColor(getResources().getColor(R.color.black));
        b1=(Button) findViewById(R.id.signup_b1);
        b2=(Button) findViewById(R.id.signup_b2);
        progressBar = (ProgressBar) findViewById(R.id.signup_pb1);
        sign_in=(TextView) findViewById(R.id.signup_tv10);
        name_et=(EditText) findViewById(R.id.signup_et1);
        username_et=(EditText) findViewById(R.id.signup_et2);
        emailorphone_et=(EditText) findViewById(R.id.signup_et3);
        password_et=(EditText) findViewById(R.id.signup_et4);
        google=(RelativeLayout) findViewById(R.id.signup_rl1);
        facebook=(RelativeLayout) findViewById(R.id.signup_rl2);
        scaleDown=(Animation) AnimationUtils.loadAnimation(this,R.anim.scale_down);
        callbackManager=CallbackManager.Factory.create();
        googleSignInClient= GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        );
        dialog=new Dialog(sign_up.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.google_progress_bar);
        dialog.setCanceledOnTouchOutside(false);
        google.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) google.startAnimation(scaleDown);
                return false;
            }
        });
        facebook.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) facebook.startAnimation(scaleDown);
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
        sign_in.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN) b1.startAnimation(scaleDown);
                return false;
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(googleSignInClient.getSignInIntent(),RQ_GOOGLE_SIGN_IN);
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(sign_up.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                handleFacebookAccessToken(loginResult.getAccessToken());
                            }

                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onError(@NotNull FacebookException e) {

                            }
                        });
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(sign_up.this,log_in.class);
                startActivity(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name_et.getText().toString().trim().isEmpty() || username_et.getText().toString().trim().isEmpty() || emailorphone_et.getText().toString().trim().isEmpty()
                        || password_et.getText().toString().trim().isEmpty()){
                    new AlertDialog.Builder(sign_up.this)
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
                progressBar.setVisibility(view.VISIBLE);
                b2.setVisibility(view.GONE);
                String name,username,emailorphone,password;
                name=name_et.getText().toString();
                username=username_et.getText().toString().trim();
                emailorphone=emailorphone_et.getText().toString().trim();
                password=password_et.getText().toString().trim();
                if(emailorphone.matches(pattern)){
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+880" + emailorphone,
                            60,
                            TimeUnit.SECONDS,
                            sign_up.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                    progressBar.setVisibility(view.GONE);
                                    b2.setVisibility(view.VISIBLE);
                                }

                                @Override
                                public void onVerificationFailed(FirebaseException e) {
                                    progressBar.setVisibility(view.GONE);
                                    b2.setVisibility(view.VISIBLE);
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCodeSent(String verificationID, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    Intent intent=new Intent(sign_up.this,otp.class);
                                    intent.putExtra("verificationid",verificationID);
                                    intent.putExtra("name",name);
                                    intent.putExtra("username",username);
                                    intent.putExtra("mobile",emailorphone);
                                    intent.putExtra("password",password);
                                    startActivity(intent);
                                }
                            }
                    );
                }else if(Patterns.EMAIL_ADDRESS.matcher(emailorphone).matches()){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailorphone,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    progressBar.setVisibility(view.GONE);
                                    b2.setVisibility(view.VISIBLE);
                                    if(task.isSuccessful()){
                                        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                                                .setValue(new User(name,username,emailorphone,password,null))
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
                                        Intent intent=new Intent(sign_up.this,log_in.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        new AlertDialog.Builder(sign_up.this)
                                                .setTitle("Authentication Failed")
                                                .setMessage("Something went wrong")
                                                .setCancelable(true)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent=new Intent(sign_up.this,log_in.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).show();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RQ_GOOGLE_SIGN_IN){
            dialog.show();
            Task<GoogleSignInAccount> googleSignInAccountTask=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount googleSignInAccount=googleSignInAccountTask.getResult(ApiException.class);
                FireBaseAuthWithGoogle(googleSignInAccount.getIdToken(),
                        googleSignInAccount.getDisplayName(),
                        googleSignInAccount.getId(),
                        googleSignInAccount.getEmail(),
                        googleSignInAccount.getPhotoUrl().toString());
            }catch (ApiException e){
                Toast.makeText(getApplicationContext(),"Google Sign in exception :"+e.getMessage(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }else{ //if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()){
            callbackManager.onActivityResult(requestCode,resultCode,data);
            GraphRequest graphRequest= GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                            try{
                                fName=jsonObject.getString("name");
                                fPassword=jsonObject.getString("id");
                                fEmail=jsonObject.getString("email");
                                fImageUrl=jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                            }catch (JSONException e){
                                new AlertDialog.Builder(sign_up.this)
                                        .setTitle("Fetching data from Graph exception (Facebook)")
                                        .setMessage(e.getMessage())
                                        .setCancelable(true)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent=new Intent(sign_up.this,log_in.class);
                                                startActivity(intent);
                                            }
                                        }).show();
                            }
                        }
                    });
        }

    }
    private void FireBaseAuthWithGoogle(String idToken, String userName, String password, String email, String imageUrl){
        FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(idToken,null))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                                    .setValue(new User(userName,userName,email,password,imageUrl))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(),"Sign Up Completed !",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    new AlertDialog.Builder(sign_up.this)
                                            .setTitle("Google Sign up saving on database")
                                            .setMessage(e.getMessage())
                                            .setCancelable(true)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent=new Intent(sign_up.this,log_in.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }).show();
                                }
                            });
                            Intent intent=new Intent(sign_up.this,log_in.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                new AlertDialog.Builder(sign_up.this)
                        .setTitle("Google Authentication Failed")
                        .setMessage(e.getMessage())
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(sign_up.this,log_in.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }).show();
            }
        });
    }
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                                    .setValue(new User(fName,fName,fEmail,fPassword,fImageUrl))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(),"Sign Up Completed !",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    new AlertDialog.Builder(sign_up.this)
                                            .setTitle("Facebook Sign up saving on database")
                                            .setMessage(e.getMessage())
                                            .setCancelable(true)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent=new Intent(sign_up.this,log_in.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }).show();
                                }
                            });
                            Intent intent=new Intent(sign_up.this,log_in.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                new AlertDialog.Builder(sign_up.this)
                        .setTitle("Facebook Authentication Failed")
                        .setMessage(e.getMessage())
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(sign_up.this,log_in.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        });
    }
}
/*    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="@dimen/_45sdp"
        android:id="@+id/signup_rl2"
        android:layout_marginTop="@dimen/_8sdp"
        android:layout_marginStart="@dimen/_12sdp"
        android:layout_marginEnd="@dimen/_12sdp"
        android:background="@drawable/signup_with_facebook_bg"
        >

        <ImageView
            android:id="@+id/signup_iv2"
            android:layout_width="@dimen/_20sdp"
            android:layout_height="@dimen/_20sdp"
            android:layout_centerVertical="true"
            android:layout_marginStart="@dimen/_12sdp"
            android:adjustViewBounds="true"
            android:src="@drawable/facebook" />

        <TextView
            android:id="@+id/signup_tv3"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:fontFamily="@font/font1"
            android:textStyle="bold"
            android:text="Sign up with Facebook"
            android:textColor="@color/white"
            android:textSize="@dimen/_15sdp" />
    </RelativeLayout>*/