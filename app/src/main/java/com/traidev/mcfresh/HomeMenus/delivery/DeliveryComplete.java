package com.traidev.mcfresh.HomeMenus.delivery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryComplete extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<DeliveryModal> delivery;
    private DeliveryAdapter adapter;
    private ProgressBar progressImg;
    ImageView emptyImage;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_delivery, container, false);

        progressImg = root.findViewById(R.id.updateImgProgress);
        emptyImage = root.findViewById(R.id.emptyImage);

        recyclerView = root.findViewById(R.id.all_notify);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        fetchDelivery();



        return root;
    }

    public void fetchDelivery()
    {
        SharedPrefManager sh = new SharedPrefManager(getActivity());

        Call<List<DeliveryModal>> call = RetrofitClient.getInstance().getApi().getDeliverys(sh.getsUser().getUid());

        call.enqueue(new Callback<List<DeliveryModal>>() {
            @Override
            public void onResponse(Call<List<DeliveryModal>> call, Response<List<DeliveryModal>> response) {

                if(response.code() != 404)
                {
                    delivery = response.body();
                    adapter = new DeliveryAdapter(delivery,getActivity());
                    recyclerView.setAdapter(adapter);}
                else
                {
                    emptyImage.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(),"No deliveries found!",Toast.LENGTH_LONG).show();
                }
                progressImg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<DeliveryModal>> call, Throwable t) {

            }
        });
    }

}
