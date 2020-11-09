package com.traidev.mcfresh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.traidev.mcfresh.Category.CatShopViewModel;
import com.traidev.mcfresh.Category.LatestsAdapter;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchShopsHome extends AppCompatActivity {

    Bundle extras;
    String searchString;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<CatShopViewModel> shops;
    private LatestsAdapter adapter;
    private Main_Interface main_interface;
    private ImageView progressImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_search_home_shops);

        progressImg = findViewById(R.id.updateImgProgress);

        recyclerView = findViewById(R.id.all_fav_shops);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        TextView header = findViewById(R.id.textHeader);


        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if (extras == null) {
                searchString = null;
            } else {

                searchString = extras.getString("searchData");
                String type  = extras.getString("searchType");
                if(type.equals("homeSearch"))
                {
                    SearchHomeShops(searchString,type);
                    header.setText(searchString);
                }
                else if(type.equals("catSearch"))
                {
                    String catName  = extras.getString("searchCat");
                    SearchHomeShops(searchString,catName);
                    header.setText(catName);
                }
                else if(type.equals("homeSliderSearch"))
                {
                    String id = extras.getString("searchData");
                    SearchSliderShops(id,type);
                }
                else if(type.equals("catSliderSearch"))
                {
                    String id = extras.getString("searchData");
                    SearchSliderShops(id,type);
                    header.setText("Max Discounts");
                }
                else if(type.equals("Primeshops"))
                {
                    SearchPrime();
                    header.setText("Prime Shops");
                }
                else if(type.equals("allShops"))
                {
                    AllRand();
                    header.setText("Shops");
                }
                else if(type.equals("BestSeller"))
                {
                    SearchPrime();
                    header.setText("Best Sellers");
                }
                else if(type.equals("BestSeller"))
                {
                    SearchPrime();
                    header.setText("Best Sellers");
                }
                else if(type.equals("Discounts"))
                {
                    SearchSliderShops("low","filterPop");
                    header.setText("Extra Discounts");
                }

                else if(type.equals("filterPop"))
                {
                    SearchHomeShops(searchString,type);
                }
                else if(type.equals("filterCost"))
                {
                    SearchHomeShops(searchString,type);
                }
            }
        }

    }

    public void SearchHomeShops(final String keyWord,String type)
    {

        progressImg.setVisibility(View.VISIBLE);

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        SharedPrefManager user = new SharedPrefManager(getApplication());

        Call<List<CatShopViewModel>> call = main_interface.getSearchShops(keyWord,type);

        call.enqueue(new Callback<List<CatShopViewModel>>() {
            @Override
            public void onResponse(Call<List<CatShopViewModel>> call, Response<List<CatShopViewModel>> response) {
                if(response.code() != 404)
                {
                    shops = response.body();
                    adapter = new LatestsAdapter(shops,getApplicationContext(),1);
                    recyclerView.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No Shops with "+keyWord,Toast.LENGTH_SHORT).show();
                }
                progressImg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<CatShopViewModel>> call, Throwable t) {

            }
        });
    }


    public void SearchSliderShops(String sId,String type)
    {

        progressImg.setVisibility(View.VISIBLE);

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        SharedPrefManager user = new SharedPrefManager(getApplication());

        Call<List<CatShopViewModel>> call = main_interface.getSliderShops(sId,type);

        call.enqueue(new Callback<List<CatShopViewModel>>() {
            @Override
            public void onResponse(Call<List<CatShopViewModel>> call, Response<List<CatShopViewModel>> response) {
                if(response.code() != 404)
                {
                    shops = response.body();
                    adapter = new LatestsAdapter(shops,getApplicationContext(),1);
                    recyclerView.setAdapter(adapter);
                }
                else
                {
                }
                progressImg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<CatShopViewModel>> call, Throwable t) {

            }
        });
    }

    public void SearchPrime()
    {
        progressImg.setVisibility(View.VISIBLE);

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        SharedPrefManager user = new SharedPrefManager(getApplication());

        Call<List<CatShopViewModel>> call = main_interface.getPrimes("prime");

        call.enqueue(new Callback<List<CatShopViewModel>>() {
            @Override
            public void onResponse(Call<List<CatShopViewModel>> call, Response<List<CatShopViewModel>> response) {
                if(response.code() != 404)
                {
                    shops = response.body();
                    adapter = new LatestsAdapter(shops,getApplicationContext(),1);
                    recyclerView.setAdapter(adapter);
                }
                else
                {
                }
                progressImg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<CatShopViewModel>> call, Throwable t) {

            }
        });
    }

    public void AllRand()
    {
        progressImg.setVisibility(View.VISIBLE);

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<CatShopViewModel>> call = main_interface.getAllShops("prime");

        call.enqueue(new Callback<List<CatShopViewModel>>() {
            @Override
            public void onResponse(Call<List<CatShopViewModel>> call, Response<List<CatShopViewModel>> response) {
                if(response.code() != 404)
                {
                    shops = response.body();
                    adapter = new LatestsAdapter(shops,getApplicationContext(),1);
                    recyclerView.setAdapter(adapter);
                }

                progressImg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<CatShopViewModel>> call, Throwable t) {

            }
        });
    }


    public void backtoActivity(View view) {
        super.onBackPressed();
    }


}
