package com.traidev.mcfresh.Category;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.traidev.mcfresh.Category.Shops.ShopPageActivity;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.traidev.mcfresh.Utility.Network.RetrofitClient.BASE_URL;

public class LatestsAdapter extends RecyclerView.Adapter<LatestsAdapter.ViewHolder>{


    private List<CatShopViewModel> nlist;
    private Context context;
    private int shopLayout;

    public LatestsAdapter(List<CatShopViewModel> nlist, Context context, int i)
    {
        this.nlist = nlist;
        this.context = context;
        this.shopLayout = i;
    }


    @NonNull
    @Override
    public LatestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(shopLayout==2)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_shop_grid,parent,false);
            return new LatestsAdapter.ViewHolder(view);
        }

        if(shopLayout==3)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_fav_shop_grid,parent,false);
            return new LatestsAdapter.ViewHolder(view);
        }

        else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_shop_list,parent,false);
            return new LatestsAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.Title.setText(nlist.get(position).getTitle());
        holder.Content.setText(nlist.get(position).getMsg());
        holder.rating.setText(nlist.get(position).getRating());

        if(shopLayout!=3){ holder.Discount.setText(nlist.get(position).getDiccount()+"% Off"); }

        if(shopLayout==3)
        {
            final String Id = nlist.get(position).getID();
            holder.fav.setChecked(nlist.get(position).getCondition());

            holder.fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                SharedPrefManager sharedPrefManager = new SharedPrefManager(context);

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    holder.fav.setChecked(isChecked);
                    if(isChecked == true){

                        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().AddtoShopFav(Id,sharedPrefManager.getsUser().getUid() );
                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                DefaultResponse dr = response.body();
                                if(response.code() == 201) {
                                    String data = dr.getMessage();
                                    Toast.makeText(context,data,Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            }

                        });
                    }
                    else {

                        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().RemovetoShopFav(Id,sharedPrefManager.getsUser().getUid() );
                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                DefaultResponse dr = response.body();
                                if(response.code() == 201) {
                                    String data = dr.getMessage();
                                    Toast.makeText(context,data,Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            }

                        });
                    }
                }
            });
        }

        if(shopLayout != 1){ holder.rating.setText("★ "+nlist.get(position).getRating()); }

        final String Id = nlist.get(position).getID();
        Glide.with(context).load(BASE_URL+"con/"+nlist.get(position).getThumbnil()).placeholder(R.drawable.ph_product).into((holder).Img);
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
            return nlist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView linear;
        ImageView Img;
        TextView Title,Content;
        TextView Discount,rating;
        CheckBox fav;


       public ViewHolder(@NonNull View itemView) {
            super(itemView);

           Img = (ImageView) itemView.findViewById(R.id.shop_thumb);
           linear = (CardView) itemView.findViewById(R.id.clickShop);
           Content = (TextView) itemView.findViewById(R.id.shop_desc);
           Title = (TextView) itemView.findViewById(R.id.shop_title);
           Discount = itemView.findViewById(R.id.sCatDiscount);
           rating = itemView.findViewById(R.id.shopRating);
           fav = itemView.findViewById(R.id.favShopCheck);

        }

    }




}
