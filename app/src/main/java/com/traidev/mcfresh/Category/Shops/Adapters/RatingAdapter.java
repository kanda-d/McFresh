package com.traidev.mcfresh.Category.Shops.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.traidev.mcfresh.R;

import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder>{


    private List<RatingViewModal> nlist;
    private Context context;

    public RatingAdapter(List<RatingViewModal> nlist, Context context)
    {
        this.nlist = nlist;
        this.context = context;
    }

    @NonNull
    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_user_ratings,parent,false);
            return new RatingAdapter.ViewHolder(view);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.Name.setText(nlist.get(position).getName());
        holder.Review.setText(nlist.get(position).getReview());
        holder.Rating.setRating(nlist.get(position).getRating());
        Glide.with(context).load("https://traidev.com/LIVE_APPS/Xation/user/"+nlist.get(position).getPro()).placeholder(R.raw.profile).into((holder).User);
    }


    @Override
    public int getItemCount() {
            return nlist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView User;
        RatingBar Rating;
        TextView Name,Review;

       public ViewHolder(@NonNull View itemView) {
            super(itemView);

             User =  itemView.findViewById(R.id.starUserPro);
             Rating =  itemView.findViewById(R.id.starUserRating);
             Name =  itemView.findViewById(R.id.starUserName);
             Review =  itemView.findViewById(R.id.starUserReview);
        }

    }




}
