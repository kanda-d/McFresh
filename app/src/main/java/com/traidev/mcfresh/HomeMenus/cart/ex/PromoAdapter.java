package com.traidev.mcfresh.HomeMenus.cart.ex;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.traidev.mcfresh.HomeMenus.cart.CartFrag;
import com.traidev.mcfresh.R;

import java.util.List;


public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.ViewHolder>{


    private List<PromoViewModel> promolist;
    private Context context;

    public PromoAdapter(List<PromoViewModel> promolist, Context context)
    {
        this.promolist = promolist;
        this.context = context;
    }

    @NonNull
    @Override
    public PromoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_apply_coupan,parent,false);
            return new PromoAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.Title.setText(promolist.get(position).getTitle());
        holder.Content.setText(promolist.get(position).getDesc());
        holder.Code.setText(promolist.get(position).getCode());
        final String Id = promolist.get(position).getID();
        holder.Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new CartFrag();
                Bundle bundle = new Bundle();

                bundle.putInt("discount", promolist.get(position).getPrice());
                bundle.putString("paycheck",CartFrag.payCheck);
                bundle.putString("code", promolist.get(position).getCode());
                bundle.putInt("upto", promolist.get(position).getUpto());
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.actvityforFrag, myFragment).addToBackStack(null).commit();
            }
        });
    }


    @Override
    public int getItemCount() {
            return promolist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Title,Content,Apply,Code;

       public ViewHolder(@NonNull View itemView) {
            super(itemView);

           Content = (TextView) itemView.findViewById(R.id.promo_desc);
           Title = (TextView) itemView.findViewById(R.id.promo_title);
           Code = (TextView) itemView.findViewById(R.id.promo_code);
           Apply = (TextView) itemView.findViewById(R.id.promo_apply);

        }

    }




}
