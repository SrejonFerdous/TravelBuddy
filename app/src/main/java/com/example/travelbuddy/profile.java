package com.example.travelbuddy;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class profile extends Fragment {

    View view;
    LinearLayout log_signup,manage,help,setting,sign_out;
    Animation scaleDown,scaleDown2;
    ImageView abouts,profilePic;
    Button signin;
    TextView tv;

    @Override
    public void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            log_signup.setVisibility(View.GONE);
            sign_out.setVisibility(View.VISIBLE);
            signin.setVisibility(View.GONE);
            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(Task<DataSnapshot> task) {
                    if(task.isSuccessful() && task.getResult().exists()){
                        tv.setText(String.valueOf(task.getResult().child("userName").getValue()));
                        Picasso.get().load(String.valueOf(task.getResult().child("photo").getValue())).into(profilePic);
                    }
                }
            });
        }else{
            log_signup.setVisibility(View.VISIBLE);
            sign_out.setVisibility(View.GONE);
            signin.setVisibility(View.VISIBLE);
            tv.setText("Sign in to see deals, manage your trips \n and many more");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        log_signup=(LinearLayout) view.findViewById(R.id.profile_log_signup);
        manage=(LinearLayout) view.findViewById(R.id.profile_manage_account);
        help=(LinearLayout) view.findViewById(R.id.profile_support);
        setting=(LinearLayout) view.findViewById(R.id.profile_settings);
        sign_out=(LinearLayout) view.findViewById(R.id.profile_sign_out);
        abouts=(ImageView) view.findViewById(R.id.profile_abouts);
        tv=(TextView) view.findViewById(R.id.profile_tv1);
        scaleDown2= AnimationUtils.loadAnimation(getActivity(),R.anim.scale_down);
        signin=(Button) view.findViewById(R.id.profile_signIn);
        abouts.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) abouts.startAnimation(scaleDown2);
                return false;
            }
        });
        log_signup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) log_signup.startAnimation(scaleDown);
                return false;
            }
        });
        manage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) manage.startAnimation(scaleDown);
                return false;
            }
        });
        help.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) help.startAnimation(scaleDown);
                return false;
            }
        });
        setting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) setting.startAnimation(scaleDown);
                return false;
            }
        });
        sign_out.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) sign_out.startAnimation(scaleDown);
                return false;
            }
        });

        log_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),log_in.class);
                startActivity(intent);
            }
        });
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                        .setValue(null)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if(task.isSuccessful()){
                                    FirebaseAuth.getInstance().getCurrentUser().delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(getActivity(), "Successfully deleted !",Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText(getActivity(),"Account deletion exception :"+e.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }else Toast.makeText(getActivity(),"Error Deleting from database",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GoogleSignIn.getLastSignedInAccount(getContext()) != null){
                    GoogleSignIn.getClient(getActivity(), new GoogleSignInOptions
                            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build()
                    ).signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if(task.isSuccessful()){
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(getActivity(),"Google Log out called",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(),"Sign Out Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getActivity(),"Custom Log out called",Toast.LENGTH_SHORT).show();
                }
                Intent intent=new Intent(getActivity(),log_in.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),log_in.class);
                startActivity(intent);
            }
        });

        return view;
    }
}