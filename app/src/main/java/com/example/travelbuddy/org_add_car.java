package com.example.travelbuddy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class org_add_car extends Fragment {
    View view;
    ImageView imageView;
    EditText name,price,numberOfSeats,numberOfPeoples,rating,availableAtPlace;
    String imageURL;
    Button save;
    Uri imageUri;
    TextView user;
    private String uniqueKey;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference("orgUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                if(task.isSuccessful() && task.getResult().exists()){
                    user.setText(String.valueOf(task.getResult().child("userName").getValue()));
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_org_add_car, container, false);

        imageView=(ImageView) view.findViewById(R.id.org_addCar_upload_image);
        name=(EditText) view.findViewById(R.id.org_addCar_name);
        price=(EditText) view.findViewById(R.id.org_addCar_price);
        numberOfSeats=(EditText) view.findViewById(R.id.org_addCar_bed);
        numberOfPeoples=(EditText) view.findViewById(R.id.org_addCar_people);
        availableAtPlace=(EditText) view.findViewById(R.id.org_addCar_place);
        rating=(EditText) view.findViewById(R.id.org_addCar_rating);
        save=(Button)view.findViewById(R.id.org_addCar_save);
        user=(TextView) view.findViewById(R.id.org_addCar_poster);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            imageUri=result.getData().getData();
                            imageView.setImageURI(imageUri);
                        }
                    }
                }
        );
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker=new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cName= name.getText().toString().trim();
                String cRating= rating.getText().toString().trim();
                String review= (Float.parseFloat(cRating)>=5.0 ? "Rating - good":"Rating - average");
                String cPrice= price.getText().toString().trim();
                String cNumberOfSeats= numberOfSeats.getText().toString().trim();
                String cNumberOfPeople= numberOfPeoples.getText().toString().trim();
                String cPlace=availableAtPlace.getText().toString().trim();

                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setCancelable(false)
                        .setView(R.layout.progress_bar).create();
                dialog.show();

                FirebaseStorage.getInstance().getReference().child("car_images").child(imageUri.getLastPathSegment())
                        .putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                FirebaseStorage.getInstance().getReference().child("car_images").child(imageUri.getLastPathSegment())
                                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageURL=uri.toString();
                                        uniqueKey= FirebaseDatabase.getInstance().getReference("carData").push().getKey();
                                        FirebaseDatabase.getInstance().getReference("carData").child(uniqueKey)
                                                .setValue(new carData(uniqueKey,cName,cPlace,review,cPrice,cNumberOfSeats,"0",uri.toString(),false))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getActivity(), "all saved!", Toast.LENGTH_LONG).show();
                                                        dialog.dismiss();
                                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2,new org_home())
                                                                .addToBackStack(null).commit();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(Exception e) {

                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(getActivity(), "Retrieving image url(CAR):"+e.getMessage(), Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getActivity(), "Image Uploading(CAR) :"+e.getMessage(), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        return view;
    }
}