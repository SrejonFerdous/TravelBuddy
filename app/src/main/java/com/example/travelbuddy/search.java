package com.example.travelbuddy;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import androidx.core.util.Pair;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class search extends Fragment {
    View view;
    LinearLayout searchLoc_ll,calendar_ll,guest_ll;
    TextView loc,guests,date;
    BottomSheetDialog bottomSheetDialog;
    Button searchData;
    CardView cardView;
    RecyclerView recyclerView;
    List<String> ageList=new ArrayList<>();
    List<hotelData> searchDetails;
    List<hotelData> allDetails;
    int adultNumber=0,childNumber=0,roomNumber=0;
    final int totGuest=48;
    LinearLayout bottomSheetLayout;
    View bottomSheetView;
    String location,check,guest;
    myAdapter adapter;
    String totDays;

   @Override
    public void onResume() {
        super.onResume();
        Bundle bundle=this.getArguments();
        //Bundle bundle2=this.getArguments();
        if(bundle!=null){
            Toast.makeText(getActivity(), "bundle exists !", Toast.LENGTH_SHORT).show();
            loc.setText(bundle.getString("totAddress"));
        }else Toast.makeText(getActivity(), "bundle null!", Toast.LENGTH_SHORT).show();
        /*if(bundle2!=null){
            Toast.makeText(getActivity(), "bundle 2 exists !", Toast.LENGTH_SHORT).show();
            guests.setText(String.valueOf(bundle2.getInt("adultNumber")+bundle2.getInt("childNumber")) + " guests, " + String.valueOf(bundle2.getInt("roomNumber")));
        }else Toast.makeText(getActivity(), "bundle 2 null!", Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_search, container, false);
        searchLoc_ll=(LinearLayout) view.findViewById(R.id.search_llSearch);
        calendar_ll=(LinearLayout)view.findViewById(R.id.search_llCalender);
        guest_ll=(LinearLayout)view.findViewById(R.id.search_llGuests);
        loc=(TextView) view.findViewById(R.id.search_location);
        guests=(TextView)view.findViewById(R.id.search_rooms);
        cardView=(CardView) view.findViewById(R.id.search_cardView);
        date=(TextView)view.findViewById(R.id.search_checks);
        searchData=(Button) view.findViewById(R.id.search_searchData);
        recyclerView=(RecyclerView) view.findViewById(R.id.stays_rv1);
        for(int j=1;j<=17;j++) ageList.add(String.valueOf(j));
        MaterialDatePicker materialDatePicker= MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds())).build();

        calendar_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(),"Select dates");//getChildFragmentManager
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Pair<Long, Long> datePair = (Pair<Long, Long>) selection;
                        long startDateMillis = datePair.first;
                        long endDateMillis = datePair.second;
                        Date startDate = new Date(startDateMillis);
                        Date endDate = new Date(endDateMillis);
                        long differenceMillis = endDate.getTime() - startDate.getTime();
                        int totalDays = (int) TimeUnit.DAYS.convert(differenceMillis, TimeUnit.MILLISECONDS);
                        totDays=String.valueOf(totalDays);
                        date.setText(materialDatePicker.getHeaderText() + ", " + totDays + " nights");
                    }
                });
            }
        });
        searchLoc_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,new search_loc()).addToBackStack(null).commit();

            }
        });

        searchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location=loc.getText().toString();
               /* check=date.getText().toString();
                String[] dateRange=check.split(" - ");
                guest=guests.getText().toString();
                String[] guestRange=guest.split(" guests, ");
                String[] roomRange=guestRange[1].split(" rooms");*/
                String key=(adultNumber+childNumber)+"_"+roomNumber+"_"+totDays;
                GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),1);
                recyclerView.setLayoutManager(gridLayoutManager);
                AlertDialog dialog=new AlertDialog.Builder(getActivity()).setCancelable(false)
                        .setView(R.layout.progress_bar).create();
                dialog.show();
                allDetails=new ArrayList<>();
                searchDetails=new ArrayList<>();
                FirebaseDatabase.getInstance().getReference("hotelData")
                        .orderByChild("key").equalTo(key)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                searchDetails.clear();
                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                        searchDetails.add(dataSnapshot.getValue(hotelData.class));
                                }
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "query called !" + key, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                Toast.makeText(getActivity(),"room number error :"+error.getMessage(),Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                adapter=new myAdapter(getContext(),searchDetails);
                recyclerView.setAdapter(adapter);
                dialog.show();

            }
        });

        guest_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<View> childView=new ArrayList<>();
                bottomSheetDialog=new BottomSheetDialog(
                        getContext(),R.style.BottomSheetDialogTheme
                );
                childView.clear();
                bottomSheetView=LayoutInflater.from(getContext()).inflate(R.layout.guest_layout,(LinearLayout)view.findViewById(R.id.guest_bottomSheetContainer));
                bottomSheetLayout=(LinearLayout) bottomSheetView.findViewById(R.id.guest_childAgell);
                bottomSheetLayout.removeAllViews();
                ImageView adultDec=(ImageView) bottomSheetView.findViewById(R.id.adults_minus);
                ImageView adultInc=(ImageView) bottomSheetView.findViewById(R.id.adults_plus);
                ImageView childDec=(ImageView) bottomSheetView.findViewById(R.id.children_minus);
                ImageView childInc=(ImageView) bottomSheetView.findViewById(R.id.children_plus);
                ImageView roomInc=(ImageView) bottomSheetView.findViewById(R.id.rooms_plus);
                ImageView roomDec=(ImageView) bottomSheetView.findViewById(R.id.rooms_minus);
                EditText adult=(EditText) bottomSheetView.findViewById(R.id.adults_number);
                EditText children=(EditText) bottomSheetView.findViewById(R.id.children_number);
                EditText rooms=(EditText) bottomSheetView.findViewById(R.id.rooms_number);
                Button apply=(Button) bottomSheetView.findViewById(R.id.guest_apply);
                Button reset=(Button) bottomSheetView.findViewById(R.id.guest_reset);
                ImageView close=(ImageView) bottomSheetView.findViewById(R.id.guest_close);
                adult.setText(String.valueOf(adultNumber));
                rooms.setText(String.valueOf(roomNumber));
                children.setText(String.valueOf(childNumber));
                for(int i=1;i<=childNumber;i++){
                    childView.add(getLayoutInflater().inflate(R.layout.child_age_layout,null,false));
                    TextView childNo=(TextView) childView.get(i-1).findViewById(R.id.child_age_tv);
                    AppCompatSpinner spinner=(AppCompatSpinner) childView.get(i-1).findViewById(R.id.child_age_spinner);
                    childNo.setText("Child "+String.valueOf(i));
                    ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,ageList);
                    spinner.setAdapter(arrayAdapter);
                    bottomSheetLayout.addView(childView.get(i-1));
                }
                reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adultNumber=childNumber=roomNumber=0;
                        bottomSheetLayout.removeAllViews();
                        childView.clear();
                        adult.setText(String.valueOf(adultNumber));
                        rooms.setText(String.valueOf(roomNumber));
                        children.setText(String.valueOf(childNumber));
                    }
                });
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*Bundle bundle1=new Bundle();
                        Fragment fragment=new search();
                        bundle1.putInt("childNumber",Integer.parseInt(children.getText().toString()));
                        bundle1.putInt("adultNumber",Integer.parseInt(adult.getText().toString()));
                        bundle1.putInt("roomNumber",Integer.parseInt(rooms.getText().toString()));
                        fragment.setArguments(bundle1);*/
                        guests.setText(String.valueOf(childNumber+adultNumber) + " guests, " + String.valueOf(roomNumber) + (roomNumber>1 ? " rooms":" room"));
                        bottomSheetDialog.dismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                adultDec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adultInc.setClickable(true);
                        childInc.setClickable(true);
                        if(adultNumber==0) {
                            adultNumber = 0;
                            adultDec.setClickable(false);
                        } else {
                            adultNumber--;

                        }
                        adult.setText(String.valueOf(adultNumber));
                    }
                });
                adultInc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adultDec.setClickable(true);
                        childDec.setClickable(true);
                        if(adultNumber > (totGuest-childNumber-1)){
                            adultNumber=(totGuest-childNumber);
                            adultInc.setClickable(false);
                        } else {
                            adultNumber++;
                        }
                        adult.setText(String.valueOf(adultNumber));
                    }
                });
                roomDec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(roomNumber==0) roomNumber=0;
                        else roomNumber--;
                        rooms.setText(String.valueOf(roomNumber));
                    }
                });
                roomInc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(roomNumber==6) roomNumber=6;
                        else roomNumber++;
                        rooms.setText(String.valueOf(roomNumber));
                    }
                });
                childInc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adultDec.setClickable(true);
                        childDec.setClickable(true);
                        if(childNumber > (totGuest - adultNumber - 1)){
                            childNumber=(totGuest-adultNumber);
                            childInc.setClickable(false);
                        } else {
                            childNumber++;
                            bottomSheetLayout.removeAllViews();
                            childView.clear();
                            for(int i=1;i<=childNumber;i++){
                                childView.add(getLayoutInflater().inflate(R.layout.child_age_layout,null,false));
                                TextView childNo=(TextView) childView.get(i-1).findViewById(R.id.child_age_tv);
                                AppCompatSpinner spinner=(AppCompatSpinner) childView.get(i-1).findViewById(R.id.child_age_spinner);
                                childNo.setText("Child "+String.valueOf(i));
                                ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,ageList);
                                spinner.setAdapter(arrayAdapter);
                                bottomSheetLayout.addView(childView.get(i-1));
                            }
                        }
                        children.setText(String.valueOf(childNumber));
                    }
                });
                childDec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adultInc.setClickable(true);
                        childInc.setClickable(true);
                        if(childNumber == 0){
                            childNumber=0;
                            childDec.setClickable(false);
                        } else {
                            childNumber--;
                            Toast.makeText(getActivity(),String.valueOf(childView.size()), Toast.LENGTH_SHORT).show();
                            bottomSheetLayout.removeView(childView.get(childNumber));
                            childView.remove(childNumber);
                        }
                        children.setText(String.valueOf(childNumber));
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        return  view;
    }
}