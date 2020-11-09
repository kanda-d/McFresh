package com.traidev.mcfresh.Category.Shops.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.traidev.mcfresh.Utility.Network.RetrofitClient.BASE_URL;

public class ResycleGrocery extends RecyclerView.Adapter<ResycleGrocery.ViewHolder> {

    private List<ModalOffers> modalOffersList;
    private Context context;
    private Button checkBtn;
    ImageView plus,minus;

    int check = 0;

    public ResycleGrocery(List<ModalOffers> modalOffersList, Context context, Button checkbtn) {
        this.modalOffersList = modalOffersList;
        this.context = context;
        this.checkBtn = checkbtn;
    }

    @NonNull
    @Override
    public ResycleGrocery.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.modal_products_layout, viewGroup, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
/*

        holder.product.setText(modalOffersList.get(position).getPro());
        holder.desc.setText(modalOffersList.get(position).getDesc());
        Glide.with(context).load(BASE_URL + "con/product/" + modalOffersList.get(position).getBanner()).fitCenter().dontAnimate().apply(new RequestOptions().override(100, 100))
                .into(holder.proThumb);

        final int amount = Integer.parseInt(modalOffersList.get(position).getPrice());

        final String Id = modalOffersList.get(position).getId();



        holder.price.setText("\u20B9" + amount);


        if(modalOffersList.get(position).getQty() > 0)
        {
            holder.Box.setVisibility(View.VISIBLE);
            holder.CartClick.setVisibility(View.GONE);
            holder.qtyUpdate.setText(String.valueOf(modalOffersList.get(position).getQty()));
            holder.priceUpdate.setText("\u20B9 "+amount*modalOffersList.get(position).getQty());
            checkBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.priceUpdate.setText("\u20B9 "+amount);
        }


        holder.CartClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    AddtoCart(Id);
                    modalOffersList.get(position).setQty(modalOffersList.get(position).getQty()+1);
                    notifyItemChanged(position);

            }
        });


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
                        if(CurrentQty!=20)
                        {
                            CurrentQty += 1;
                            holder.qtyUpdate.setText(String.valueOf(CurrentQty));
                            holder.priceUpdate.setText("\u20B9 " + amount * CurrentQty);
                        }
                        else
                        {
                            Toast.makeText(context,"You can't Exceed Limit!",Toast.LENGTH_SHORT).show();
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
                        holder.Box.setVisibility(View.GONE);
                        holder.CartClick.setVisibility(View.VISIBLE);

                    updateQtyCart(Id,CurrentQty);
                    RemovetoCart(Id);
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
*/

        if(modalOffersList.get(position).getDisc().equals("0"))
            holder.pro_discount.setVisibility(View.GONE);
        else
            holder.pro_discount.setText(modalOffersList.get(position).getDisc()+"% Off");

        holder.product.setText(modalOffersList.get(position).getPro());
        holder.desc.setText(modalOffersList.get(position).getDesc());
        Glide.with(context).load(BASE_URL + "con/product/" + modalOffersList.get(position).getBanner()).placeholder(R.drawable.ph_product).dontAnimate().apply(new RequestOptions().override(200, 200)).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.proThumb);

        final int amount = Integer.parseInt(modalOffersList.get(position).getPrice());

        final String Id = modalOffersList.get(position).getId();
        final String ShopId = modalOffersList.get(position).getShopID();

        holder.price.setText("\u20B9" + amount);

        if (modalOffersList.get(position).getQty() > 0) {
            holder.Box.setVisibility(View.VISIBLE);
            holder.CartClick.setVisibility(View.GONE);
            holder.qtyUpdate.setText(String.valueOf(modalOffersList.get(position).getQty()));
            holder.priceUpdate.setText("\u20B9 " + amount * modalOffersList.get(position).getQty());
            checkBtn.setVisibility(View.VISIBLE);
        } else {
            holder.priceUpdate.setText("\u20B9 " + amount);
        }



        holder.CartClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddtoCart(Id,modalOffersList.get(position).getPrice());
                holder.Box.setVisibility(View.VISIBLE);
                holder.CartClick.setVisibility(View.GONE);
                checkBtn.setVisibility(View.VISIBLE);
                holder.qtyUpdate.setText("1");
            }
        });


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus.setEnabled(false);
                int CurrentQty = Integer.parseInt(holder.qtyUpdate.getText().toString());

                if (CurrentQty == 1) {
                    CurrentQty += 1;
                    holder.qtyUpdate.setText(String.valueOf(CurrentQty));
                    holder.priceUpdate.setText("\u20B9 " + amount * CurrentQty);
                } else {
                    if(CurrentQty!=20)
                    {
                        CurrentQty += 1;
                        holder.qtyUpdate.setText(String.valueOf(CurrentQty));
                        holder.priceUpdate.setText("\u20B9 " + amount * CurrentQty);
                    }
                    else
                    {
                        Toast.makeText(context,"You can't Exceed Limit!",Toast.LENGTH_SHORT).show();
                    }
                }

                updateQtyCart(Id, CurrentQty);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                minus.setEnabled(false);

                int CurrentQty = Integer.parseInt(holder.qtyUpdate.getText().toString());
                if (CurrentQty == 1) {

                    holder.Box.setVisibility(View.GONE);
                    holder.CartClick.setVisibility(View.VISIBLE);

                    updateQtyCart(Id, CurrentQty);
                    RemovetoCart(Id);
                } else {
                    CurrentQty -= 1;
                    holder.qtyUpdate.setText(String.valueOf(CurrentQty));
                    holder.priceUpdate.setText("\u20B9 " + amount * CurrentQty);
                    updateQtyCart(Id, CurrentQty);
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        if (modalOffersList == null)
        return 0;
        else
        return  modalOffersList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView proThumb;
        TextView product,price,desc,CartClick,qtyUpdate,priceUpdate,pro_discount;
        LinearLayout Box;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            proThumb = itemView.findViewById(R.id.pro_thumb);
            CartClick = itemView.findViewById(R.id.addtoCart);
            product = itemView.findViewById(R.id.pro_title);
            price = itemView.findViewById(R.id.pro_price);
            desc = itemView.findViewById(R.id.pro_desc);
            pro_discount = itemView.findViewById(R.id.pro_discount);
            Box = itemView.findViewById(R.id.qtyBox);

            plus = itemView.findViewById(R.id.addQty);
            minus = itemView.findViewById(R.id.removeQty);
            qtyUpdate = itemView.findViewById(R.id.qtyUpdate);
            priceUpdate = itemView.findViewById(R.id.priceUpdate);

        }
    }

    public void AddtoCart(String id,String price)
    {
        final SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().AddtoCart(id,"gro","1",price,sharedPrefManager.getsUser().getUid());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                DefaultResponse dr = response.body();
                sharedPrefManager.setCart(Integer.parseInt(dr.getMessage()));
                if(call.isExecuted())
                {
                    check=0;
                }
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }
        });
    }

    public void updateQtyCart(String id, int qty)
    {
        final SharedPrefManager sharedPrefManager = new SharedPrefManager(context);

        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().updateQty(id,qty,sharedPrefManager.getsUser().getUid());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                    if(call.isExecuted())
                    {
                        plus.setEnabled(true);
                        minus.setEnabled(true);
                    }
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }
        });
    }

    public void RemovetoCart(String id)
    {

        final SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        sharedPrefManager.setCart(sharedPrefManager.getsUser().getItem()-1);
        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().RemovefromCart(id,sharedPrefManager.getsUser().getUid());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                if(response.code() == 201)
                {
                    sharedPrefManager.setCart(Integer.parseInt(dr.getMessage()));
                    minus.setEnabled(true);
                }
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }
        });
    }

    public void removeAt(int position) {
        modalOffersList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, modalOffersList.size());
    }


}
