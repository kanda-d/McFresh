package com.traidev.mcfresh.HomeMenus.orders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.traidev.mcfresh.HomeMenus.ActivityForFrag;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderDetailsFrag extends Fragment {

    private Main_Interface main_interface;
    Button accept,reject;
    String oid = null;
    FrameLayout framLoad;
    String userId = null;
    private ProgressBar progressBar,orderStatus;
    TextView sname,saddress,paytype,date,deliveryaddress,orderid,ordeAM;
    ImageView uProfile;
    SharedPrefManager sharedPrefManager;



    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<OrderViewModa> notify;
    private OrderProductAdapter adapter;

    
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sharedPrefManager = new SharedPrefManager(getActivity());
        View root = inflater.inflate(R.layout.fragment_order_detials, container, false);

        //progressBar = root.findViewById(R.id.updateProgOrders);

        sname = root.findViewById(R.id.shopname);
        saddress = root.findViewById(R.id.shopaddress);
        paytype = root.findViewById(R.id.paytype);
        date = root.findViewById(R.id.orderTime);
        deliveryaddress = root.findViewById(R.id.deliveryAddress);
        orderid = root.findViewById(R.id.orderId);
        ordeAM = root.findViewById(R.id.orderAmount);


        recyclerView = root.findViewById(R.id.all_products);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        framLoad = root.findViewById(R.id.showBeforeLoadFrame);

        Bundle b = this.getArguments();
        if(b != null)
        {
            oid = b.getString("orderId");
            fetchOrder(oid);
        }

        Button repeat = root.findViewById(R.id.repeatProductBtn);
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderNow(oid);
            }
        });

        orderid.setText(oid);

        return root;
    }



    public void fetchOrder(final String id)
    {


        framLoad.setVisibility(View.VISIBLE);
        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<DefaultResponse> call = main_interface.getOrderDetails(id);
        call.enqueue(new Callback<DefaultResponse>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                DefaultResponse dr = response.body();

                if(response.code() != 404)
                {
                    String data = dr.getMessage();
                    String[] dif = data.split("#");
                    sname.setText(dif[0]);
                    saddress.setText(dif[1]);
                    ordeAM.setText("\u20B9"+dif[2]+".00");
                    date.setText(dif[3]);
                    paytype.setText(dif[4]);
                    deliveryaddress.setText(dif[5]);

                    fetchOrders(oid);

                }

            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

    public void fetchOrders(String id)
    {

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<OrderViewModa>> call = main_interface.getOrderProducts(id);

        call.enqueue(new Callback<List<OrderViewModa>>() {
            @Override
            public void onResponse(Call<List<OrderViewModa>> call, Response<List<OrderViewModa>> response) {
                if(response.code() != 404)
                {
                    notify = response.body();
                    adapter = new OrderProductAdapter(notify,getContext());
                    recyclerView.setAdapter(adapter);
                    framLoad.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<List<OrderViewModa>> call, Throwable t) {

            }
        });
    }

    public void OrderNow(String oid) {

        SharedPrefManager sh = new SharedPrefManager(getContext());
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().OrderRepeat(oid, sh.getsUser().getUid());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                if (response.code() == 201) {
                    Intent i = new Intent(getContext(), ActivityForFrag.class);
                    i.putExtra("Frag", "cart");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(i);

                } else {
                    Toast.makeText(getContext(), "Can't Repeat Order!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }


        });


    }





}