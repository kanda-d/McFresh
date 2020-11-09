package com.traidev.mcfresh.Category.Shops.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.traidev.mcfresh.HomeMenus.cart.ex.UpdateQty;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.traidev.mcfresh.Utility.Network.RetrofitClient.BASE_URL;

public class ResycleCart extends RecyclerView.Adapter<ResycleCart.ViewHolder> {

    private List<ModalOffers> modalOffersList;
    private Context context;
    private TextView  items;
    private UpdateQty updateQty;
    private ProgressBar linearProgres;
    ImageView plus,minus;


    public ResycleCart(List<ModalOffers> modalOffersList, Context context,UpdateQty updateQty,ProgressBar linearProgres) {
        this.modalOffersList = modalOffersList;
        this.context = context;
        this.items = items;
        this.updateQty = updateQty;
        this.linearProgres = linearProgres;
    }

    @NonNull
    @Override
    public ResycleCart.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.modal_cart_products_layout,viewGroup,false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,int position) {

        holder.product.setText(modalOffersList.get(position).getPro());
        holder.desc.setText(modalOffersList.get(position).getDesc());
        Glide.with(context).load(BASE_URL + "con/product/" + modalOffersList.get(position).getBanner()).fitCenter().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.proThumb);

        final int amount = Integer.parseInt(modalOffersList.get(position).getPrice());

        final String Id = modalOffersList.get(position).getId();
        final String ShopId = modalOffersList.get(position).getShopID();

        holder.price.setText("\u20B9" + amount);

        holder.Box.setVisibility(View.VISIBLE);
        holder.CartClick.setVisibility(View.GONE);
        holder.qtyUpdate.setText(String.valueOf(modalOffersList.get(position).getQty()));
        holder.priceUpdate.setText("\u20B9 "+amount*modalOffersList.get(position).getQty());

        if(!modalOffersList.get(position).getVariation().equals("null"))
        {
            holder.qtydesc.setVisibility(View.VISIBLE);
            holder.qtydesc.setText("Quantity: "+modalOffersList.get(position).getBanner());
        }
        final int currentPosition = position;
        final ModalOffers product = modalOffersList.get(position);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    plus.setEnabled(false);
                    int CurrentQty = Integer.parseInt(holder.qtyUpdate.getText().toString());

                        if(CurrentQty==1)
                        {
                            CurrentQty +=1;
                            holder.qtyUpdate.setText(String.valueOf(CurrentQty));
                            holder.priceUpdate.setText("\u20B9 "+amount*CurrentQty);
                        }
                        else
                        {
                            CurrentQty+=1;
                            holder.qtyUpdate.setText(String.valueOf(CurrentQty));
                            holder.priceUpdate.setText("\u20B9 "+amount*CurrentQty);
                        }

                    updateQtyCart(Id,CurrentQty);
            }
        });


        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                minus.setEnabled(false);

                int CurrentQty = Integer.parseInt(holder.qtyUpdate.getText().toString());
                if(CurrentQty==1)
                {
                    updateQtyCart(Id,CurrentQty);

                        RemovetoCart(Id,product);
                }
                else
                {
                    CurrentQty-=1;
                    holder.qtyUpdate.setText(String.valueOf(CurrentQty));
                    holder.priceUpdate.setText("\u20B9 "+amount*CurrentQty);
                    updateQtyCart(Id,CurrentQty);
                }
            }
        });
    }

    private void removeItem(ModalOffers modalOffers) {
        int position = modalOffersList.indexOf(modalOffers);
        if(position!=-1)
        {
            modalOffersList.remove(position);
            notifyItemRemoved(position);

        }
    }

    @Override
    public int getItemCount() {
        return modalOffersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView proThumb;
        TextView product,price,qtydesc,desc,CartClick,qtyUpdate,priceUpdate,pro_discount;
        LinearLayout Box;
        LinearLayout removeCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            proThumb = itemView.findViewById(R.id.pro_thumb);
            removeCart = itemView.findViewById(R.id.removeCart);
            CartClick = itemView.findViewById(R.id.addtoCart);
            product = itemView.findViewById(R.id.pro_title);
            price = itemView.findViewById(R.id.pro_price);
            qtydesc = itemView.findViewById(R.id.quantityDesc);
            desc = itemView.findViewById(R.id.pro_desc);
            pro_discount = itemView.findViewById(R.id.pro_discount);
            Box = itemView.findViewById(R.id.qtyBox);

            plus = itemView.findViewById(R.id.addQty);
            minus = itemView.findViewById(R.id.removeQty);
            qtyUpdate = itemView.findViewById(R.id.qtyUpdate);
            priceUpdate = itemView.findViewById(R.id.priceUpdate);

        }
    }


    public void updateQtyCart(String id, int qty)
    {
        linearProgres.setVisibility(View.VISIBLE);

        final SharedPrefManager sharedPrefManager = new SharedPrefManager(context);

        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().updateQty(id,qty,sharedPrefManager.getsUser().getUid());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code()==201)
                {
                    if(call.isExecuted())
                    {
                        plus.setEnabled(true);
                        minus.setEnabled(true);
                        updateQty.UpdateQty(1,modalOffersList.size());
                        linearProgres.setVisibility(View.GONE);
                    }
                }


            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }
        });
    }

    public void RemovetoCart(String id,ModalOffers pos)
    {
        linearProgres.setVisibility(View.VISIBLE);

        final SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        sharedPrefManager.setCart(sharedPrefManager.getsUser().getItem()-1);
        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().RemovefromCart(id,sharedPrefManager.getsUser().getUid());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                if(response.code() == 201)
                {
                    if(call.isExecuted())
                    {
                        sharedPrefManager.setCart(Integer.parseInt(dr.getMessage()));
                        linearProgres.setVisibility(View.INVISIBLE);
                        // notifyItemRangeChanged(pos,modalOffersList.size());
                        removeItem(pos);
                        updateQty.UpdateQty(1,modalOffersList.size());
                        minus.setEnabled(true);

                    }
                    else
                    {

                    }


                }
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }
        });
    }



}
