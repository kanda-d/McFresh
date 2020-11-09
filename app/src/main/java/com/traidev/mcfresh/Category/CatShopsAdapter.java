package com.traidev.mcfresh.Category;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.traidev.mcfresh.Category.Shops.ShopPageActivity;
import com.traidev.mcfresh.R;

import java.util.List;

import static com.traidev.mcfresh.Utility.Network.RetrofitClient.BASE_URL;

public class CatShopsAdapter extends RecyclerView.Adapter<CatShopsAdapter.ViewHolder>{

    private List<CatShopViewModel> nlist;
    private Context context;
    private int shopLayout;
    double lat1=0,log1=0,lat2=0,log2=0;


    public CatShopsAdapter(List<CatShopViewModel> nlist, Context context,int shopLayout)
    {
        this.nlist = nlist;
        this.context = context;
        this.shopLayout = shopLayout;
    }

    @NonNull
    @Override
    public CatShopsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(shopLayout==2)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_shop_grid,parent,false);
            return new CatShopsAdapter.ViewHolder(view);
        }

        if(shopLayout==5)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_shop_stylish_home,parent,false);
            return new CatShopsAdapter.ViewHolder(view);
        }
        if(shopLayout==4)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_shop_stylish,parent,false);
            return new CatShopsAdapter.ViewHolder(view);
        }

        else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_shop_list,parent,false);
            return new CatShopsAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.Title.setText(nlist.get(position).getTitle());
        holder.Content.setText(nlist.get(position).getMsg());
        final String Id = nlist.get(position).getID();

        if(shopLayout!=5 && shopLayout!=4)
        holder.Discount.setText(nlist.get(position).getDiccount()+"% Off");


        if(shopLayout!=2)
        {
            holder.rating.setText(nlist.get(position).getRating());
        }
        if(shopLayout==2)
        {
            holder.rating.setText("★ "+nlist.get(position).getRating());
        }

        Glide.with(context).load(BASE_URL+"con/"+nlist.get(position).getThumbnil()).fitCenter().dontAnimate().placeholder(R.drawable.ph_product).diskCacheStrategy(DiskCacheStrategy.ALL).into((holder).Img);
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ShopPageActivity.class);
                i.putExtra("key",Id);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (nlist == null)
            return 0;
        else
            return  nlist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Img;
        TextView Title,Content;
        CardView linear;
        TextView Discount;
        TextView rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

             Img =  itemView.findViewById(R.id.shop_thumb);
             linear = itemView.findViewById(R.id.clickShop);
             Content =  itemView.findViewById(R.id.shop_desc);
             Title =  itemView.findViewById(R.id.shop_title);
             Discount = itemView.findViewById(R.id.sCatDiscount);
             rating = itemView.findViewById(R.id.shopRating);
        }

    }


}
