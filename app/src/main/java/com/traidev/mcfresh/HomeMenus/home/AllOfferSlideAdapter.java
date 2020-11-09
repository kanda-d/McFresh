package com.traidev.mcfresh.HomeMenus.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.traidev.mcfresh.R;

import java.util.List;

import static com.traidev.mcfresh.Utility.Network.RetrofitClient.BASE_URL_A;

public class AllOfferSlideAdapter extends RecyclerView.Adapter<AllOfferSlideAdapter.ViewHolder>{


    private List<OffersSlidersModel> slides;
    private Context context;

    public AllOfferSlideAdapter(List<OffersSlidersModel> slides, Context context)
    {
        this.context = context;
        this.slides = slides;
    }

    @NonNull
    @Override
    public AllOfferSlideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_offers_slider,parent,false);
            return new AllOfferSlideAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            final String Id = slides.get(position).getSliderID();
        Toast.makeText(context,slides.get(position).getBanner(),Toast.LENGTH_SHORT).show();

        Glide.with(context).load("http://mcfresh.in/panel/content/offers/"+slides.get(position).getBanner()).skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate().into((holder).Img);

    }

    @Override
    public int getItemCount() {
            return slides.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Img;

       public ViewHolder(@NonNull View itemView) {
            super(itemView);
           Img = (ImageView) itemView.findViewById(R.id.shop_thumb);
        }

    }




}
