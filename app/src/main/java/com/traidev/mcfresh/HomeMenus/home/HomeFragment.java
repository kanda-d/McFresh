package com.traidev.mcfresh.HomeMenus.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.traidev.mcfresh.Category.CatShopViewModel;
import com.traidev.mcfresh.Category.CatShopsAdapter;
import com.traidev.mcfresh.Category.CategoriesActivity;
import com.traidev.mcfresh.Category.Product.ProductsActivity;
import com.traidev.mcfresh.Category.Shops.Adapters.ModalOffers;
import com.traidev.mcfresh.Category.Shops.Adapters.ResycleGrocery;
import com.traidev.mcfresh.Home;
import com.traidev.mcfresh.HomeMenus.ActivityForFrag;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerViewCats;
    private List<CatShopViewModel> shopPrimes;
    private CatShopsAdapter adapter3;
    private FrameLayout parentFrame;
    TextView SeeAll;
    ProgressBar updateImgProgress;
    ProgressBar primeShops;

    Button hideCheck;




    private RecyclerView recyclerView;
    private RecyclerView recyclerViewSliders;
    private RecyclerView.LayoutManager layoutManager;
    private List<ModalOffers> shopsGrid;
    private ResycleGrocery adapter;
    private Main_Interface main_interface;


    private ShimmerFrameLayout shimmerFrameLayout;


    SharedPrefManager sharedPrefManager;
     ImageView mainBanner,groBanner;
    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        CardView pick,food,gro,fruit,med,meat,pet,cos,baker,state,gad,gift;



        pick = root.findViewById(R.id.c_pick);
        food = root.findViewById(R.id.c_food);
        fruit = root.findViewById(R.id.c_fruit);
        gro = root.findViewById(R.id.c_gro);
        med = root.findViewById(R.id.c_med);
        cos = root.findViewById(R.id.c_cos);
        gift = root.findViewById(R.id.c_gift);
        meat = root.findViewById(R.id.c_meat);
        pet = root.findViewById(R.id.c_pet);
        baker = root.findViewById(R.id.c_baker);
        gad = root.findViewById(R.id.c_gadget);
        state = root.findViewById(R.id.c_state);


        food.setOnClickListener(this);
        fruit.setOnClickListener(this);
        gift.setOnClickListener(this);
        gro.setOnClickListener(this);
        pick.setOnClickListener(this);
        cos.setOnClickListener(this);
        state.setOnClickListener(this);
        med.setOnClickListener(this);
        baker.setOnClickListener(this);
        pet.setOnClickListener(this);
        meat.setOnClickListener(this);
        gad.setOnClickListener(this);


        sharedPrefManager = new SharedPrefManager(getContext());
        parentFrame = getActivity().findViewById(R.id.nav_host_fragment);


        hideCheck = root.findViewById(R.id.hideCheck);
        updateImgProgress = root.findViewById(R.id.updateImgProgress);


        root.findViewById(R.id.openStore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cat = new Intent(getContext(), ProductsActivity.class);
                cat.putExtra("catName", "food");
                startActivity(cat);
            }
        });


        recyclerView = root.findViewById(R.id.productsGridRecy);
        layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        fetchGridProducts();

       recyclerViewCats = root.findViewById(R.id.primeGridRecycler);
        recyclerViewCats.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCats.setHasFixedSize(true);
        primeShops = root.findViewById(R.id.progressPrime);

        final SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());

        fetchPrimeShops();

        return root;
    }

    public void fetchPrimeShops()
    {
        primeShops.setVisibility(View.VISIBLE);

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<CatShopViewModel>> call = main_interface.getPrimes("Agra");

        call.enqueue(new Callback<List<CatShopViewModel>>() {
            @Override
            public void onResponse(Call<List<CatShopViewModel>> call, Response<List<CatShopViewModel>> response) {

                    shopPrimes = response.body();
                    adapter3 = new CatShopsAdapter(shopPrimes,getContext(),5);
                    recyclerViewCats.setAdapter(adapter3);
                primeShops.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<CatShopViewModel>> call, Throwable t) {
            }
        });
    }



      public void fetchGridProducts()
      {
        recyclerView.setAdapter(null);

        updateImgProgress.setVisibility(View.VISIBLE);
        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

            Call<List<ModalOffers>> call = main_interface.getHomeProducts("homeProducts",sharedPrefManager.getsUser().getUid());

            call.enqueue(new Callback<List<ModalOffers>>() {
                @Override
                public void onResponse(Call<List<ModalOffers>> call, Response<List<ModalOffers>> response) {
                    if(response.code() != 404)
                    {
                        shopsGrid = response.body();
                        adapter = new ResycleGrocery(shopsGrid,getContext(),hideCheck);
                        recyclerView.setAdapter(adapter);
                    }
                    updateImgProgress.setVisibility(View.GONE);
                }
                @Override
                public void onFailure(Call<List<ModalOffers>> call, Throwable t) {

                }
            });
    }




    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.c_pick:
                Intent cat = new Intent(getContext(), ActivityForFrag.class);
                cat.putExtra("Frag", "adddel");
                startActivity(cat);
                break;
            case R.id.c_food:
                catClick("food");
                break;
            case R.id.c_gro:
                catClick("grocery");
                break;
            case R.id.c_fruit:
                catClick("fruit");
                break;
            case R.id.c_med:
                catClick("medicine");
                break;
            case R.id.c_meat:
                catClick("meat");
                break;
            case R.id.c_pet:
                catClick("pet");
                break;
            case R.id.c_cos:
                catClick("cos");
                break;
            case R.id.c_baker:
                catClick("baker");
                break;
            case R.id.c_state:
                catClick("state");
                break;
            case R.id.c_gadget:
               catClick("gadget");
                break;
            case R.id.c_gift:
                catClick("gift");
                break;
            default:
                return;

        }

    }



    public void catClick(String typ) {
        Intent id = new Intent(getContext(), CategoriesActivity.class);
        id.putExtra("catName", typ);
        startActivity(id);
    }




}