package com.example.travelbuddy;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {

    private  Animation scale_down;
    private Context context;
    private List<hotelData> hotelDataList;


    public myAdapter(Context context, List<hotelData> hotelDataList) {
        this.context = context;
        this.hotelDataList = hotelDataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        scale_down=AnimationUtils.loadAnimation(context,R.anim.scale_down);
        Glide.with(context).load(hotelDataList.get(position).getHotelImage()).into(holder.imageView);
        holder.name.setText(hotelDataList.get(position).getName());
        holder.rating.setText(hotelDataList.get(position).getRating());
        holder.price.setText(hotelDataList.get(position).getPrice() + " Taka");
        holder.numberOfBeds.setText("Number of Beds :"+hotelDataList.get(position).getNumberOfBeds());
        holder.numberOfPeople.setText("Number of People :"+hotelDataList.get(position).getNumberOfPeople());
        holder.place.setText(hotelDataList.get(position).getPlace());
        holder.checkDates.setText(hotelDataList.get(position).getCheckInOut() + " nights for " +
                Float.parseFloat(String.valueOf((Integer.parseInt(hotelDataList.get(position).getPrice())*Integer.parseInt(hotelDataList.get(position).getCheckInOut()))/30.0))
                +" Taka only!"
        );
        if(hotelDataList.get(holder.getAbsoluteAdapterPosition()).isReserved()) holder.reserve.setText("Reserved !");
        else holder.reserve.setText("Reserve ?");
        if(hotelDataList.get(holder.getAbsoluteAdapterPosition()).isFavourite())  holder.favourites.setImageResource(R.drawable.ic_favourite_filled);
        else holder.favourites.setImageResource(R.drawable.ic_favorite_border);
        if(hotelDataList.get(holder.getAbsoluteAdapterPosition()).isWifi()){
            holder.wifi.setTextColor(context.getResources().getColor(R.color.black));
            holder.imageWifi.setColorFilter(context.getResources().getColor(R.color.black));
        }else{
            holder.wifi.setTextColor(context.getResources().getColor(R.color.light_ash));
            holder.imageWifi.setColorFilter(ActivityCompat.getColor(context,R.color.light_ash));
        }
        if(hotelDataList.get(holder.getAbsoluteAdapterPosition()).isPets()){
            holder.pets.setTextColor(context.getResources().getColor(R.color.black));
            holder.imagePets.setColorFilter(context.getResources().getColor(R.color.black));
        }else{
            holder.pets.setTextColor(context.getResources().getColor(R.color.light_ash));
            holder.imagePets.setColorFilter(ActivityCompat.getColor(context,R.color.light_ash));
        }
        if(hotelDataList.get(holder.getAbsoluteAdapterPosition()).isSpa()){
            holder.spa.setTextColor(context.getResources().getColor(R.color.black));
            holder.imageSpa.setColorFilter(ActivityCompat.getColor(context,R.color.black));
        }else{
            holder.spa.setTextColor(context.getResources().getColor(R.color.light_ash));
            holder.imageSpa.setColorFilter(ActivityCompat.getColor(context,R.color.light_ash));
        }
        if(hotelDataList.get(holder.getAbsoluteAdapterPosition()).isPool()){
            holder.pool.setTextColor(context.getResources().getColor(R.color.black));
            holder.imagePool.setColorFilter(context.getResources().getColor(R.color.black));
        }else{
            holder.pool.setTextColor(context.getResources().getColor(R.color.light_ash));
            holder.imagePool.setColorFilter(ActivityCompat.getColor(context,R.color.light_ash));
        }
        if(hotelDataList.get(holder.getAbsoluteAdapterPosition()).isParking()){
            holder.parking.setTextColor(context.getResources().getColor(R.color.black));
            holder.imageParking.setColorFilter(context.getResources().getColor(R.color.black));
        }else{
            holder.parking.setTextColor(context.getResources().getColor(R.color.light_ash));
            holder.imageParking.setColorFilter(ActivityCompat.getColor(context,R.color.light_ash));
        }
        if(hotelDataList.get(holder.getAbsoluteAdapterPosition()).isAc()){
            holder.ac.setTextColor(context.getResources().getColor(R.color.black));
            holder.imageAc.setColorFilter(context.getResources().getColor(R.color.black));
        }else{
            holder.ac.setTextColor(context.getResources().getColor(R.color.light_ash));
            holder.imageAc.setColorFilter(ActivityCompat.getColor(context,R.color.light_ash));
        }
        holder.favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hotelDataList.get(holder.getAbsoluteAdapterPosition()).isFavourite()){
                    hotelDataList.get(holder.getAbsoluteAdapterPosition()).setFavourite(true);
                    holder.favourites.setImageResource(R.drawable.ic_favourite_filled);
                    HashMap updateFav=new HashMap();
                    updateFav.put("favourite",true);
                    FirebaseDatabase.getInstance().getReference("hotelData")
                            .child(hotelDataList.get(holder.getAbsoluteAdapterPosition()).getUniqueCode())
                            .updateChildren(updateFav)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(Task task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "Added to favourites !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(context, "Failed to update true to favourites : "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    hotelDataList.get(holder.getAbsoluteAdapterPosition()).setFavourite(false);
                    holder.favourites.setImageResource(R.drawable.ic_favorite_border);
                    HashMap updateFav=new HashMap();
                    updateFav.put("favourite",false);
                    FirebaseDatabase.getInstance().getReference("hotelData")
                            .child(hotelDataList.get(holder.getAbsoluteAdapterPosition()).getUniqueCode())
                            .updateChildren(updateFav)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(Task task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "Removed from favourites !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(context, "Failed to false update favourites : "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.relativeLayout.getVisibility() == View.GONE) holder.relativeLayout.setVisibility(View.VISIBLE);
            }
        });
        holder.close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) holder.close.startAnimation(scale_down);
                return false;
            }
        });
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.relativeLayout.setVisibility(View.GONE);
            }
        });
        holder.reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hotelDataList.get(holder.getAbsoluteAdapterPosition()).isReserved()){
                    hotelDataList.get(holder.getAbsoluteAdapterPosition()).setReserved(true);
                    holder.reserve.setText("Reserved !");
                    holder.reserve.setClickable(false);
                    HashMap update=new HashMap();
                    update.put("reserved",true);
                    FirebaseDatabase.getInstance().getReference("hotelData")
                            .child(hotelDataList.get(holder.getAbsoluteAdapterPosition()).getUniqueCode())
                            .updateChildren(update)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(Task task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "Reserved !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(context, "Failed to update reserve : "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Toast.makeText(context, "Already Reserved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelDataList.size();
    }


    public void searchHotel(ArrayList<hotelData> arrayList){
        hotelDataList=arrayList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView,close,imageWifi,imagePets,imagePool,imageParking,imageAc,imageSpa,favourites;
        TextView name,rating,price,numberOfBeds,numberOfPeople,wifi,pets,pool,parking,ac,spa,place,checkDates;
        CardView cardView;
        Button reserve;
        RelativeLayout relativeLayout;
        public MyViewHolder(View itemView) {
            super(itemView);

            relativeLayout=itemView.findViewById(R.id.recycler_item_rl);
            reserve= itemView.findViewById(R.id.recycler_item_apply);
            close=itemView.findViewById(R.id.recycler_item_close);
            cardView=itemView.findViewById(R.id.recycler_item_cardView);
            favourites= itemView.findViewById(R.id.recycler_item_favourites);
            imageView=itemView.findViewById(R.id.recycler_item_iv);
            imageWifi=itemView.findViewById(R.id.recycler_item_imageFreeWifi);
            imagePets=itemView.findViewById(R.id.recycler_item_imagePets);
            imagePool=itemView.findViewById(R.id.recycler_item_imagePool);
            imageParking=itemView.findViewById(R.id.recycler_item_imageParking);
            imageAc=itemView.findViewById(R.id.recycler_item_imageAc);
            imageSpa=itemView.findViewById(R.id.recycler_item_imageSpa);
            name= itemView.findViewById(R.id.recycler_item_name);
            rating= itemView.findViewById(R.id.recycler_item_rating);
            price= itemView.findViewById(R.id.recycler_item_tv6);
            numberOfBeds= itemView.findViewById(R.id.recycler_item_beds);
            numberOfPeople= itemView.findViewById(R.id.recycler_item_people);
            place= itemView.findViewById(R.id.recycler_item_place);
            checkDates= itemView.findViewById(R.id.recycler_item_check_dates);
            wifi= itemView.findViewById(R.id.recycler_item_freeWifi);
            pets= itemView.findViewById(R.id.recycler_item_pets);
            pool= itemView.findViewById(R.id.recycler_item_pool);
            parking= itemView.findViewById(R.id.recycler_item_parking);
            ac= itemView.findViewById(R.id.recycler_item_ac);
            spa= itemView.findViewById(R.id.recycler_item_spa);
        }
    }
}
