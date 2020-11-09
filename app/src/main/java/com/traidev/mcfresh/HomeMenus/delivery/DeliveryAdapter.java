package com.traidev.mcfresh.HomeMenus.delivery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.traidev.mcfresh.R;

import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder>{


    private List<DeliveryModal> nlist;
    private Context context;

    public DeliveryAdapter(List<DeliveryModal> nlist, Context context)
    {
        this.nlist = nlist;
        this.context = context;
    }

    @NonNull
    @Override
    public DeliveryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_delvery,parent,false);
            return new DeliveryAdapter.ViewHolder(view);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.mobile.setText("OID "+nlist.get(position).getMobile());
        holder.pick.setText(nlist.get(position).getPic());
        holder.drop.setText(nlist.get(position).getDrop());
        holder.otype.setText(nlist.get(position).getType());
        holder.orderId.setText(nlist.get(position).getOrderId());
        holder.oAmount.setText(" \u20B9" +nlist.get(position).getAmount());
        holder.oDate.setText(nlist.get(position).getTime());
        holder.ost.setText(nlist.get(position).getStatus());

        if(nlist.get(position).getStatus().equals("0")){
            holder.ost.setText("Pending");}
        else if(nlist.get(position).getStatus().equals("2"))
            holder.ost.setText("Delivered");
        else if(nlist.get(position).getStatus().equals("4"))
            holder.ost.setText("Cancelled");
        else if(nlist.get(position).getStatus().equals("1"))
        {
            holder.ost.setText("Preparing");
        }

    }


    @Override
    public int getItemCount() {
            return nlist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cust,mobile,ost,orderId,oDate,otype,oAmount,pick,drop;
        private ImageView sIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mobile = itemView.findViewById(R.id.custMobile);
            orderId = itemView.findViewById(R.id.orderId);
            oDate = itemView.findViewById(R.id.orderTime);
            otype = itemView.findViewById(R.id.orderType);
            oAmount = itemView.findViewById(R.id.orderAmount);
            pick = itemView.findViewById(R.id.pickLoc);
            drop = itemView.findViewById(R.id.dropLoc);
            ost = itemView.findViewById(R.id.orderStatus);
        }
    }




}
