package com.traidev.mcfresh.HomeMenus.cart.ex;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.traidev.mcfresh.HomeMenus.ActivityForFrag;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.ViewHolder>{


    private List<AddressViewModel> promolist;
    private TextView PrimeAddress;
    private Context context;
    private TextView saved;

    public SavedAdapter(List<AddressViewModel> promolist, Context context,TextView PrimeAddress,TextView saved)
    {
        this.promolist = promolist;
        this.context = context;
        this.PrimeAddress = PrimeAddress;
        this.saved = saved;
    }

    @NonNull
    @Override
    public SavedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_saved_address,parent,false);
            return new SavedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.Title.setText(promolist.get(position).getTitle());
        holder.Content.setText(promolist.get(position).getAddress());
        final String Id = promolist.get(position).getID();

        holder.Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrimeAddress.setText(promolist.get(position).getAddress());
                SharedPrefManager sh = new SharedPrefManager(context);
                sh.setLocation(promolist.get(position).getAddress());
                if(ActivityForFrag.onResetFragment.equals("cart"))
                {
                    Intent i = new Intent(context, ActivityForFrag.class);
                    i.putExtra("Frag","cart");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
                saved.callOnClick();

            }
        });
        holder.delete_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().deleteAddress(promolist.get(position).getID());
                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            DefaultResponse dr = response.body();

                                if(call.isExecuted())
                                {
                                    if(response.code() == 201) {
                                        String data = dr.getMessage();
                                        Toast.makeText(context,data,Toast.LENGTH_SHORT).show();
                                        promolist.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, promolist.size());
                                    }


                            }
                        }
                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
            return promolist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Title,Content;
        LinearLayout Apply;
        ImageView delete_add;

       public ViewHolder(@NonNull View itemView) {
            super(itemView);

           Content = itemView.findViewById(R.id.address_desc);
           Title =  itemView.findViewById(R.id.address_title);
           delete_add =  itemView.findViewById(R.id.delete_add);
           Apply = itemView.findViewById(R.id.linearAddress);

        }

    }




}
