package com.traidev.mcfresh.HomeMenus.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareFrag extends Fragment {
    TextView code;
    String share  = null;
    LinearLayout sharetoFriend;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_share_option, container, false);

        final SharedPrefManager sharedPrefManager = new SharedPrefManager(getActivity());
        sharetoFriend = root.findViewById(R.id.sharetoFriend);

        code = root.findViewById(R.id.your_code);

        fetchCodeAmount(sharedPrefManager.getsUser().getUid());
        sharetoFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp(sharedPrefManager.getsUser().getName(),sharedPrefManager.getsUser().getUid());
            }
        });

        return root;
    }



    public void fetchCodeAmount(String id)
    {
        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().getShareDetails(id);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code() == 201) {

                    String[] dif = dr.getMessage().split("#");
                    code.append(dif[0]);
                    share = dif[0];

                }
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }

        });
    }

    private void shareApp(String user,String userid) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Join & Spread love with Flado  \n" +
                        "Your Friend " + user+ " \n" +
                        " \nUse your Friends Code - " + share + "  \nCheckout Flado App at: https://play.google.com/store/apps/details?id=" + getContext().getPackageName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }




}