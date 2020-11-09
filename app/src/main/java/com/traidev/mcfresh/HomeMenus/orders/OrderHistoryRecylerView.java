package com.traidev.mcfresh.HomeMenus.orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.traidev.mcfresh.Home;
import com.traidev.mcfresh.HomeMenus.ActivityForFrag;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.traidev.mcfresh.Utility.Network.RetrofitClient.BASE_URL;

public class OrderHistoryRecylerView extends RecyclerView.Adapter<OrderHistoryRecylerView.ViewHolder> {

    private List<ModelOrderHistory> modalOrderHistoryList;
    private Context context;

    String reason = "null";

    public OrderHistoryRecylerView(List<ModelOrderHistory> modalOrderHistoryList, Context context) {
        this.modalOrderHistoryList = modalOrderHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHistoryRecylerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.modal_orders_layout,viewGroup,false);
        return new ViewHolder(view);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.sTitle.setText(""+modalOrderHistoryList.get(position).getTitle());
        holder.sAdd.setText(modalOrderHistoryList.get(position).getAddress());
        holder.oItems.setText(modalOrderHistoryList.get(position).getItem()+"");
        holder.oAmount.setText(" \u20B9" +modalOrderHistoryList.get(position).getAmount());
        holder.oDate.setText(modalOrderHistoryList.get(position).getTime());
        holder.oId.setText(modalOrderHistoryList.get(position).getOrderId());

        if(modalOrderHistoryList.get(position).getStatus().equals("0")){
            holder.sStatus.setText("Pending"); holder.CancelOrder.setVisibility(View.VISIBLE);}
        else if(modalOrderHistoryList.get(position).getStatus().equals("2"))
            holder.sStatus.setText("Delivered");
        else if(modalOrderHistoryList.get(position).getStatus().equals("4"))
            holder.sStatus.setText("Cancelled");
        else if(modalOrderHistoryList.get(position).getStatus().equals("1"))
        {
            holder.sStatus.setText("Preparing");
            holder.CancelOrder.setVisibility(View.VISIBLE);
        }


        if(modalOrderHistoryList.get(position).getIcon().contains("Default")) {
            holder.sIcon.setImageResource(R.raw.logo);
        }
        else
        {
            Glide.with(context).load(BASE_URL+"/con/"+modalOrderHistoryList.get(position).getIcon()).fitCenter().dontAnimate().into(holder.sIcon);
        }

        if(modalOrderHistoryList.get(position).getItem().contains("Lunch") || modalOrderHistoryList.get(position).getItem().contains("Dinner"))
        {
            holder.RepeatOrder.setVisibility(View.INVISIBLE);
        }

        holder.clicktoDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oid = modalOrderHistoryList.get(position).getOrderId();

                Intent id = new Intent(context, ActivityForFrag.class);
                id.putExtra("Frag", "orderDetails");
                id.putExtra("orderid", oid);
                id.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(id);
            }
        });

        holder.RepeatOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderNow(modalOrderHistoryList.get(position).getOrderId(),holder.repeatProgress);
            }
        });


        if(!modalOrderHistoryList.get(position).getItem().contains("Lunch") || !modalOrderHistoryList.get(position).getItem().contains("Dinner"))
        holder.CancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BottomSheetDialog filter = new BottomSheetDialog(v.getContext());
                filter.setContentView(R.layout.cancel_order_popup);

                final RadioGroup radioGroup;
                EditText reasonMsg;

                final TextView changeOrder;
                filter.setCanceledOnTouchOutside(false);

                filter.show();
                Button apply;
                radioGroup = filter.findViewById(R.id.cancelReason);
                changeOrder = filter.findViewById(R.id.changeOrder);
                reasonMsg = filter.findViewById(R.id.cancelMsg);
                apply = filter.findViewById(R.id.cancelOrderButton);

                if(!modalOrderHistoryList.get(position).getTitle().contains("Flado Services"))
                if(modalOrderHistoryList.get(position).getStatus().equals("1"))
                {
                    if(!modalOrderHistoryList.get(position).getPaytype().equals("COD"))
                    {
                          changeOrder.setVisibility(View.VISIBLE);
                    }
                }


                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        RadioButton radio = (RadioButton) filter.findViewById(checkedId);
                         reason = radio.getText().toString();
                        if(reason.contains("Others"))
                        {
                            reasonMsg.setEnabled(true);
                            reasonMsg.requestFocus();
                        }
                    }
                });

                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(reason.toLowerCase().contains("others"))
                        {
                            if(!reasonMsg.getText().toString().isEmpty()) {
                            reason = reasonMsg.getText().toString();
                            return; }
                            else {
                             Toast.makeText(context,"Add Reason for Cancel the Order!",Toast.LENGTH_SHORT).show();
                             return;}
                        }
                        else
                        {
                            if(!reason.equals("null") && !reason.equals("Others"))
                            {
                                apply.setEnabled(false);
                                apply.setText("Cancelling...");
                                SharedPrefManager sh = new SharedPrefManager(context);
                                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().CancelOrder(modalOrderHistoryList.get(position).getOrderId()
                                        ,reason);
                                call.enqueue(new Callback<DefaultResponse>() {
                                    @Override
                                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                        DefaultResponse dr = response.body();
                                        if (response.code() == 201) {
                                            Toast.makeText(context, "Order Cancelled!", Toast.LENGTH_SHORT).show();

                                            Intent i = new Intent(context, Home.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(i);

                                        } else {
                                            Toast.makeText(context, "Can't Cancel Order!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                    }

                                });
                            }
                            else
                            {
                                Toast.makeText(context,"Select Reason for Cancel the Order!",Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });

            }
        });
    }


    @Override
    public int getItemCount() {
        return modalOrderHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView sTitle,sAdd,oDate,sStatus,oItems,oAmount,oId;
        private CircleImageView sIcon;
        private TextView RepeatOrder,CancelOrder;
        ProgressBar repeatProgress;
        CardView clicktoDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sTitle = itemView.findViewById(R.id.shopTitle);
            sAdd = itemView.findViewById(R.id.shopAddress);
            oDate = itemView.findViewById(R.id.orderTime);
            sIcon = itemView.findViewById(R.id.shopIcon);
            oId = itemView.findViewById(R.id.orderID);
            clicktoDetails = itemView.findViewById(R.id.clicktoDetails);

            sStatus = itemView.findViewById(R.id.orderStatus);
            oItems = itemView.findViewById(R.id.orderItems);
            oAmount = itemView.findViewById(R.id.orderAmount);
            RepeatOrder = itemView.findViewById(R.id.repeatOrder);
            CancelOrder = itemView.findViewById(R.id.cancelOrder);
            repeatProgress = itemView.findViewById(R.id.repeatProgress);
        }
    }


    public void OrderNow(String oid, final ProgressBar pro) {

        pro.setVisibility(View.VISIBLE);
        SharedPrefManager sh = new SharedPrefManager(context);
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().OrderRepeat(oid,sh.getsUser().getUid());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                if (response.code() == 201) {
                    Intent i = new Intent(context, ActivityForFrag.class);
                    i.putExtra("Frag","cart");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    pro.setVisibility(View.INVISIBLE);

                } else {
                    Toast.makeText(context, "Can't Repeat Order!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }


        });


    }



}
