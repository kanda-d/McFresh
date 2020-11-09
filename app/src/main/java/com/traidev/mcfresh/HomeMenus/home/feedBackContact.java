package com.traidev.mcfresh.HomeMenus.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class feedBackContact extends Fragment {

    private EditText msg;
    private Button Send;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_feed_contact, container, false);

        final SharedPrefManager sharedPrefManager = new SharedPrefManager(getActivity());

        Send = root.findViewById(R.id.submitFeed);
        msg = root.findViewById(R.id.enterMsg);

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().SendFeedBack(sharedPrefManager.getsUser().getName(),msg.getText().toString());
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        DefaultResponse dr = response.body();

                        if(response.code() == 201) {
                            String data = dr.getMessage();
                            Toast.makeText(getContext(),data,Toast.LENGTH_SHORT).show();
                            msg.setText("");
                        }
                        else
                        {
                        }
                    }
                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {

                    }

                });
            }
        });
        return root;
    }







}