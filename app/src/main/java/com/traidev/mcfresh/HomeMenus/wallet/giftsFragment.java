package com.traidev.mcfresh.HomeMenus.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class giftsFragment extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<ModelWalletHistory> history;
    private WalletHistoryRecylerView adapter;
    private Main_Interface main_interface;
    private ProgressBar progressBar;

    private TextView bal;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SharedPrefManager sharedPrefManager = new SharedPrefManager(getActivity());

        View root = inflater.inflate(R.layout.fragment_navigation, container, false);

        bal = root.findViewById(R.id.balance);
        progressBar = root.findViewById(R.id.updateProgHistory);
        fetchBal(sharedPrefManager.getsUser().getUid());

        recyclerView = root.findViewById(R.id.wallet_hitory_recy);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

       fetchHistory(sharedPrefManager.getsUser().getUid());

        return root;
    }

    public void fetchHistory(String id)
    {
        progressBar.setVisibility(View.VISIBLE);
        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<ModelWalletHistory>> call = main_interface.getWalletHistory(id);
        call.enqueue(new Callback<List<ModelWalletHistory>>() {
            @Override
            public void onResponse(Call<List<ModelWalletHistory>> call, Response<List<ModelWalletHistory>> response) {
                if(response.code() != 404)
                {
                    history = response.body();
                    adapter = new WalletHistoryRecylerView(history,getActivity());
                    recyclerView.setAdapter(adapter);
                }
                else
                { }
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<ModelWalletHistory>> call, Throwable t) {

            }
        });
    }



    public void fetchBal(String id)
    {
        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().getBal(id);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code() == 201) {
                    String data = dr.getMessage();
                    bal.setText("\u20B9" + data);
                }
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }

        });
    }

}