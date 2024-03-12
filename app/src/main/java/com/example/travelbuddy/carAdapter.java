package com.example.travelbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class carAdapter extends RecyclerView.Adapter<carAdapter.MyViewHolder>{

    private Animation scale_down;
    private Context context;
    private List<carData> carDataList;

    public carAdapter(Context context, List<carData> carDataList) {
        this.context = context;
        this.carDataList = carDataList;
    }

    @Override
    public carAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_rent_layout, parent, false);
        return new carAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(carAdapter.MyViewHolder holder, int position) {
        scale_down= AnimationUtils.loadAnimation(context,R.anim.scale_down);
        Glide.with(context).load(carDataList.get(position).getImageUrl()).into(holder.carImage);
        holder.carName.setText(carDataList.get(position).getName());
        holder.availablePlace.setText(carDataList.get(position).getPlace());
        holder.price.setText(carDataList.get(position).getPrice()+ " Taka");
        holder.rating.setText(carDataList.get(position).getRating());
        holder.numberOfSeats.setText("Number of Seats :"+carDataList.get(position).getNumberOfSeats());
    }

    @Override
    public int getItemCount() {
        return carDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView carImage;
        TextView carName, availablePlace, numberOfSeats, rating, price, distanceBasedCost;
        Button reserve;
        public MyViewHolder(View itemView) {
            super(itemView);

            carImage=(ImageView) itemView.findViewById(R.id.car_rent_iv);
            carName=(TextView) itemView.findViewById(R.id.car_rent_name);
            availablePlace=(TextView) itemView.findViewById(R.id.car_rent_isAvailable);
            numberOfSeats=(TextView) itemView.findViewById(R.id.car_rent_seats);
            rating=(TextView) itemView.findViewById(R.id.car_rent_rating);
            price=(TextView) itemView.findViewById(R.id.car_rent_price);
            distanceBasedCost=(TextView) itemView.findViewById(R.id.car_rent_distancePrice);
            reserve=(Button) itemView.findViewById(R.id.car_rent_apply);
        }
    }
}
