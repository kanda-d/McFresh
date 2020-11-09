package com.traidev.mcfresh.HomeMenus.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliviriesFrag extends Fragment {
    
    
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<ModelOrderHistory> orders;
    private OrderHistoryRecylerView adapter;
    private Main_Interface main_interface;
    private ImageView progressBar;

    ImageView notFound;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SharedPrefManager sharedPrefManager = new SharedPrefManager(getActivity());

        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        progressBar = root.findViewById(R.id.updateImgProgress);
        notFound = root.findViewById(R.id.notFound);

        recyclerView = root.findViewById(R.id.order_history_rec);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        fetchHistory(sharedPrefManager.getsUser().getUid());

        return root;
    }

    public void fetchHistory(String id)
    {
        progressBar.setVisibility(View.VISIBLE);
        notFound.setVisibility(View.GONE);
        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<ModelOrderHistory>> call = main_interface.getOrdersHistory(id);
        call.enqueue(new Callback<List<ModelOrderHistory>>() {
            @Override
            public void onResponse(Call<List<ModelOrderHistory>> call, Response<List<ModelOrderHistory>> response) {
                if(response.code() != 404)
                {
                    orders = response.body();
                    adapter = new OrderHistoryRecylerView(orders,getActivity().getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }
                else
                {
                    recyclerView.setAdapter(null);
                    notFound.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<ModelOrderHistory>> call, Throwable t) {

            }
        });
    }



}