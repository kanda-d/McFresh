package com.traidev.mcfresh.HomeMenus.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.traidev.mcfresh.PaymentActivity;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopwalletFragment extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<ModelWalletHistory> history;
    private WalletHistoryRecylerView adapter;
    private Main_Interface main_interface;
    private ProgressBar progressBar;

    private RelativeLayout Addmoney;
    private String userId;
    private Button a50,a100,a200;
    private TextView bal;
    private EditText Amount;

    TextView btnText;
    ProgressBar btnProgress;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final SharedPrefManager sharedPrefManager = new SharedPrefManager(getActivity());
        View root = inflater.inflate(R.layout.fragment_topup, container, false);

        Addmoney = root.findViewById(R.id.addMoneyWallet);
        Amount = root.findViewById(R.id.amountTop);
        progressBar = root.findViewById(R.id.updateTopHistory);

        userId = sharedPrefManager.getsUser().getUid();

        bal = root.findViewById(R.id.balance);
        a50 = root.findViewById(R.id.add50);
        a100 = root.findViewById(R.id.add100);
        a200 = root.findViewById(R.id.add200);

        btnProgress= root.findViewById(R.id.loginProgress);
        btnText = root.findViewById(R.id.login_text);

        a50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Amount.getText().toString().isEmpty())
                {
                    Amount.setText("50");
                    Amount.setSelection(Amount.getText().toString().length());
                }
                else {
                    int am = Integer.parseInt(Amount.getText().toString());
                    Amount.setText(""+(50+am));
                        Amount.setSelection(Amount.getText().toString().length());

                }

            }
        });

        a100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Amount.getText().toString().isEmpty())
                {
                    Amount.setText("100");
                    Amount.setSelection(Amount.getText().toString().length());
                }
                else {
                    int am = Integer.parseInt(Amount.getText().toString());
                    Amount.setText(""+(100+am));
                        Amount.setSelection(Amount.getText().toString().length());
                }
            }
        });

        a200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Amount.getText().toString().isEmpty())
                {
                    Amount.setText("200");
                    Amount.setSelection(Amount.getText().toString().length());
                }
                else {
                    int am = Integer.parseInt(Amount.getText().toString());
                        Amount.setText(""+(200+am));
                        Amount.setSelection(Amount.getText().toString().length());
                }
            }
        });


        fetchBal(userId);

        recyclerView = root.findViewById(R.id.topup_wallet_hitory_recy);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        fetchTopupHistory(userId);

        Addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String am = Amount.getText().toString();
                if(!am.isEmpty())
                {
                    if(Integer.parseInt(am)>=10001)
                        Toast.makeText(getContext(),"Maximum Amount limit is 10,000!",Toast.LENGTH_SHORT).show();
                    else
                        TopUpWallet(Amount.getText().toString());
                }
                else
                    Toast.makeText(getContext(),"Add Amount",Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    public void fetchTopupHistory(String id)
    {
        progressBar.setVisibility(View.VISIBLE);
        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<ModelWalletHistory>> call = main_interface.getTopupHistory(id);
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
                    bal.setText("Wallet Balance  :  \u20B9" + data);
                }
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }


    public void TopUpWallet(String amount)
    {
        btnText.setVisibility(View.GONE);
        btnProgress.setVisibility(View.VISIBLE);
        Intent i = new Intent(getContext(), PaymentActivity.class);

        i.putExtra("oamount", amount);
        i.putExtra("orderType", "topup");
        startActivity(i);
        btnText.setVisibility(View.VISIBLE);
        btnProgress.setVisibility(View.GONE);
    }
}