package com.traidev.mcfresh.HomeMenus.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.traidev.mcfresh.R;

import java.util.List;

import static com.traidev.mcfresh.Utility.Network.RetrofitClient.BASE_URL;


public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder>{


    private List<OrderViewModa> nlist;
    private Context context;

    public OrderProductAdapter(List<OrderViewModa> nlist, Context context)
    {
        this.nlist = nlist;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_modal_produts,parent,false);
            return new OrderProductAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final String title = nlist.get(position).getTitle();
        final String price = nlist.get(position).getQty();
        final String desc = nlist.get(position).getMsg();
        final String pro = nlist.get(position).getThumbnil();

        holder.Title.setText(title);
        holder.qty.setText(desc);
        holder.desc.setText(nlist.get(position).getPrice());
        holder.total.setText("\u20B9"+price);

        Glide.with(context).load(BASE_URL+"con/product/"+pro).dontAnimate().into((holder).Img);
        if(pro.length()>4) {
            Glide.with(context).load(BASE_URL+"con/product/"+pro).dontAnimate().into((holder).Img);
        }
        else
        {
            holder.Img.setVisibility(View.INVISIBLE);
            holder.Img.getLayoutParams().width =390;
        }
    }


    @Override
    public int getItemCount() {
            return nlist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Img;
        TextView Title,qty,total,desc;

       public ViewHolder(@NonNull View itemView) {
            super(itemView);

           Img =  itemView.findViewById(R.id.proIcon);
           qty =  itemView.findViewById(R.id.proDesc);
           Title =  itemView.findViewById(R.id.proTitle);
           total =  itemView.findViewById(R.id.proprice);
           desc =  itemView.findViewById(R.id.totalDesc);

        }

    }




}
