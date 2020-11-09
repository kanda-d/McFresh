package com.traidev.mcfresh.HomeMenus.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.traidev.mcfresh.Category.CatShopViewModel;
import com.traidev.mcfresh.Category.LatestsAdapter;
import com.traidev.mcfresh.Home;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavShopsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<CatShopViewModel> shops;
    private LatestsAdapter adapter;
    private Main_Interface main_interface;
    private ImageView progressImg;

    ImageView notFound;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_fav_shops, container, false);

        SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());



        progressImg = root.findViewById(R.id.updateImgProgress);

        recyclerView = root.findViewById(R.id.all_fav_shops);
        layoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        notFound = root.findViewById(R.id.notFound);

        fetchFavShop(sharedPrefManager.getsUser().getUid());

        return root;
    }



    public void fetchFavShop(String userid)
    {
        progressImg.setVisibility(View.VISIBLE);

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<CatShopViewModel>> call = main_interface.getFavShops(userid);

        call.enqueue(new Callback<List<CatShopViewModel>>() {
            @Override
            public void onResponse(Call<List<CatShopViewModel>> call, Response<List<CatShopViewModel>> response) {
                if(response.code() != 404)
                {
                    shops = response.body();
                    adapter = new LatestsAdapter(shops,getActivity(),3);
                    recyclerView.setAdapter(adapter);
                }
                else
                {
                    notFound.setVisibility(View.VISIBLE);
                }
                progressImg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<CatShopViewModel>> call, Throwable t) {

            }
        });
    }


}

















