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
import com.traidev.mcfresh.R;

import java.util.List;

import static com.traidev.mcfresh.Utility.Network.RetrofitClient.BASE_URL_A;

public class CategoryLatestsAdapter extends RecyclerView.Adapter<CategoryLatestsAdapter.ViewHolder>{

    private List<CatShopViewModel> nlist;
    private Context context;
    private int catLayout;

    public CategoryLatestsAdapter(List<CatShopViewModel> nlist, Context context, int catLayout)
    {
        this.nlist = nlist;
        this.context = context;
        this.catLayout = catLayout;
    }

    @NonNull
    @Override
    public CategoryLatestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(catLayout==2)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_catshop_slides,parent,false);
            return new CategoryLatestsAdapter.ViewHolder(view);
        }

        else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_shop_list,parent,false);
            return new CategoryLatestsAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.Title.setText(nlist.get(position).getTitle());

        if(catLayout!=2)
        {
            holder.Dicount.setText(nlist.get(position).getDiccount()+"% Off");
            holder.rating.setText(nlist.get(position).getRating());
        }

        final String Id = nlist.get(position).getID();
        Glide.with(context).load(BASE_URL_A + nlist.get(position).getThumbnil()).fitCenter().into((holder).Img);
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SelectedCatShow.class);
                i.putExtra("key", Id);
                i.putExtra("title", nlist.get(position).getTitle());
                i.putExtra("thumb", nlist.get(position).getThumbnil());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
            return nlist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Img;
        TextView Title,Content;
        CardView linear;
        TextView Dicount;
        TextView rating;

       public ViewHolder(@NonNull View itemView) {
            super(itemView);

             Img =  itemView.findViewById(R.id.cat_banner);
             linear =  itemView.findViewById(R.id.catClick);
             Title =  itemView.findViewById(R.id.cat_titile);
             Dicount = itemView.findViewById(R.id.sCatDiscount);
             rating = itemView.findViewById(R.id.shopRating);


        }

    }




}
