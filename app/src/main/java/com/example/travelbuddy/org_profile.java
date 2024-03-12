package com.example.travelbuddy;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class org_profile extends Fragment {
    View view;
    LinearLayout log_signup,manage,help,setting,sign_out;
    Animation scaleDown,scaleDown2;
    ImageView abouts;
    Button signin;
    TextView tv;

    @Override
    public void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            log_signup.setVisibility(View.GONE);
            sign_out.setVisibility(View.VISIBLE);
            signin.setVisibility(View.GONE);
            FirebaseDatabase.getInstance().getReference("orgUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(Task<DataSnapshot> task) {
                    if(task.isSuccessful() && task.getResult().exists()){
                        tv.setText("Welcome ,"+task.getResult().child("userName").getValue());
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

        view= inflater.inflate(R.layout.fragment_org_profile, container, false);
        log_signup=(LinearLayout) view.findViewById(R.id.org_profile_log_signup);
        manage=(LinearLayout) view.findViewById(R.id.org_profile_manage_account);
        help=(LinearLayout) view.findViewById(R.id.org_profile_support);
        setting=(LinearLayout) view.findViewById(R.id.org_profile_settings);
        sign_out=(LinearLayout) view.findViewById(R.id.org_profile_sign_out);
        abouts=(ImageView) view.findViewById(R.id.org_profile_abouts);
        tv=(TextView) view.findViewById(R.id.org_profile_tv1);
        scaleDown= AnimationUtils.loadAnimation(getActivity(),R.anim.scale_down_2);
        signin=(Button) view.findViewById(R.id.org_profile_signIn);

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

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("orgUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
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

        log_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),organization_login.class);
                startActivity(intent);
            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getActivity(),organization_login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),organization_login.class);
                startActivity(intent);
            }
        });


        return  view;
    }
}