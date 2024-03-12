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
import androidx.core.util.Pair;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.Result;

public class post extends Fragment {
    View view;
    ImageView imageView;
    EditText name,price,numberOfBeds,numberOfPeoples,rating,place,checkInOut;
    CheckBox wifi,pool,parking,pet,spa,ac;
    String imageURL;
    Button save;
    Uri imageUri;
    String uniqueKey;
    boolean isWifi,isPool,isParking,isPet,isSpa,isAc;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_post, container, false);
        imageView=(ImageView) view.findViewById(R.id.post_upload_image);
        name=(EditText) view.findViewById(R.id.post_name);
        price=(EditText) view.findViewById(R.id.post_price);
        numberOfBeds=(EditText) view.findViewById(R.id.post_bed);
        numberOfPeoples=(EditText) view.findViewById(R.id.post_people);
        checkInOut=(EditText)view.findViewById(R.id.post_checkInOut);
        place=(EditText) view.findViewById(R.id.post_place);
        rating=(EditText) view.findViewById(R.id.post_rating);
        wifi=(CheckBox) view.findViewById(R.id.post_wifi);
        pet=(CheckBox) view.findViewById(R.id.post_pet);
        spa=(CheckBox) view.findViewById(R.id.post_spa);
        pool=(CheckBox) view.findViewById(R.id.post_pool);
        parking=(CheckBox) view.findViewById(R.id.post_parking);
        ac=(CheckBox) view.findViewById(R.id.post_ac);
        save=(Button)view.findViewById(R.id.post_save);
        MaterialDatePicker materialDatePicker= MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds())).build();

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
        checkInOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(),"Select dates");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Pair<Long,Long> datePair=(Pair<Long,Long>) selection;
                        long startDateMillis=datePair.first;
                        long endDateMillis=datePair.second;
                        Date start=new Date(startDateMillis);
                        Date end=new Date(endDateMillis);
                        long diffMillis=end.getTime()-start.getTime();
                        checkInOut.setText(String.valueOf((int) TimeUnit.DAYS.convert(diffMillis,TimeUnit.MILLISECONDS)));
                    }
                });
            }
        });

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
            public void onClick(View view) {
                String hName= name.getText().toString().trim();
                String hRating= rating.getText().toString().trim();
                String review= (Float.parseFloat(hRating)>=5.0 ? "Rating - good":"Rating - average");
                String hPrice= price.getText().toString().trim();
                String hNumberOfBeds= numberOfBeds.getText().toString().trim();
                String hNumberOfPeople= numberOfPeoples.getText().toString().trim();
                String hPlace=place.getText().toString().trim();
                String hCheckInOut=checkInOut.getText().toString().trim();
                String key=hNumberOfPeople+"_"+hNumberOfBeds+"_"+hCheckInOut;
                boolean hWifi= wifi.isChecked();
                boolean hPets= pet.isChecked();
                boolean hAc= ac.isChecked();
                boolean hParking= parking.isChecked();
                boolean hPool= pool.isChecked();
                boolean hSpa= spa.isChecked();

                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setCancelable(false)
                        .setView(R.layout.progress_bar).create();
                dialog.show();

                FirebaseStorage.getInstance().getReference().child("hotel_images").child(imageUri.getLastPathSegment())
                        .putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                FirebaseStorage.getInstance().getReference().child("hotel_images").child(imageUri.getLastPathSegment())
                                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageURL=uri.toString();
                                        uniqueKey=FirebaseDatabase.getInstance().getReference("hotelData").push().getKey();
                                        FirebaseDatabase.getInstance().getReference("hotelData").child(uniqueKey)
                                                .setValue(new hotelData(hName,review,uri.toString(),hPrice,hNumberOfBeds,hNumberOfPeople,hPlace,hCheckInOut,key,uniqueKey,false,false,hWifi,hPets,hAc,hParking,hPool,hSpa))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getActivity(), "all saved!", Toast.LENGTH_LONG).show();
                                                        dialog.dismiss();
                                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new profile())
                                                                .addToBackStack(null).commit();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(Exception e) {
                                                Toast.makeText(getActivity(), "Uploading data :"+e.getMessage(), Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(getActivity(), "Retrieving image :"+e.getMessage(), Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getActivity(), "Image Uploading :"+e.getMessage(), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

            }
        });


        return view;
    }
}