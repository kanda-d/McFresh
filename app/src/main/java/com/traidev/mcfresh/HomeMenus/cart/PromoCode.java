package com.traidev.mcfresh.HomeMenus.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.traidev.mcfresh.HomeMenus.ActivityForFrag;
import com.traidev.mcfresh.HomeMenus.cart.ex.PromoAdapter;
import com.traidev.mcfresh.HomeMenus.cart.ex.PromoViewModel;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromoCode extends Fragment {


    private FrameLayout parentFrame;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<PromoViewModel> notify;
    private PromoAdapter adapter;
    private Main_Interface main_interface;
    private ImageView progressImg;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_promo_codes, container, false);
        ActivityForFrag.onResetFragment  = "Promo";

        SharedPrefManager sh = new SharedPrefManager(getActivity());
        recyclerView = root.findViewById(R.id.all_promo);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        progressImg = root.findViewById(R.id.updateImgProgress);

        fetchPromo(sh.getsUser().getUid());

        return root;
    }


    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrame.getId(),fragment);
        fragmentTransaction.commit();
    }


    public void fetchPromo(String user)
    {
        Bundle b = this.getArguments();
        String paytype = b.getString("paytype");
        if(!b.getString("paytype").equals("COD"))
        {
            paytype = "online";
        }


        progressImg.setVisibility(View.VISIBLE);
        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<PromoViewModel>> call = main_interface.getPromos(user,paytype);

        call.enqueue(new Callback<List<PromoViewModel>>() {
            @Override
            public void onResponse(Call<List<PromoViewModel>> call, Response<List<PromoViewModel>> response) {
                if(response.code() != 404)
                {
                    notify = response.body();
                    adapter = new PromoAdapter(notify,getActivity());
                    recyclerView.setAdapter(adapter);
                }

                progressImg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<PromoViewModel>> call, Throwable t) {

            }
        });
    }








}