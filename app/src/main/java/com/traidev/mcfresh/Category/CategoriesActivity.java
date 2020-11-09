package com.traidev.mcfresh.Category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.SearchShopsHome;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.traidev.mcfresh.Utility.Network.RetrofitClient.BASE_URL_A;

public class CategoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView,recyclerViewList;
    private RecyclerView.LayoutManager layoutManager,layoutManager2;
    private List<CatShopViewModel> shopsGrid,shopsList;
    private CatShopsAdapter adapter,adapter2;
    private Main_Interface main_interface;
    RelativeLayout coming;
    ImageView bannerCat,filterclick;

    private ShimmerFrameLayout shimmerFrameLayout;

    EditText SearchShopCategoryEdit;

    Context context;

    SharedPrefManager sharedPrefManager;
    Bundle extras;
    String catType;
    // private ImageView progressImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        TextView toolbar = findViewById(R.id.textHeader);
        context = getApplicationContext();

        shimmerFrameLayout = findViewById(R.id.SearchloadingShimmer);
        coming = findViewById(R.id.comingrel);

        findViewById(R.id.backtoHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bannerCat = findViewById(R.id.bannerCategory);
        filterclick = findViewById(R.id.filterclick);

        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if (extras == null) {
                catType = null;
            } else {
                catType = extras.getString("catName");
            }
        }


        findViewById(R.id.seeAllFav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent id = new Intent(getApplicationContext(), SearchShopsHome.class);
                id.putExtra("searchType", "allShops");
                startActivity(id);
            }
        });


        SearchShopCategoryEdit = findViewById(R.id.searchShops);

        bannerCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent id = new Intent(getApplicationContext(), SearchShopsHome.class);
                id.putExtra("searchData","");
                id.putExtra("searchType", "disSearch");
                id.putExtra("cat", catType);
                startActivity(id);*/
            }
        });


        filterclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterPopup();
            }
        });


        SearchShopCategoryEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent id = new Intent(getApplicationContext(), SearchShopsHome.class);
                    id.putExtra("searchData", SearchShopCategoryEdit.getText().toString());
                    id.putExtra("searchType", "catSearch");
                    id.putExtra("searchCat", catType);
                    startActivity(id);
                    return true;
                }
                return false;
            }
        });


        sharedPrefManager = new SharedPrefManager(getApplicationContext());

        sharedPrefManager.setEmergency(false);


        recyclerView = findViewById(R.id.shopGridRecycler);
        layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);



        recyclerViewList = findViewById(R.id.shopListRecycler);
        layoutManager2 = new LinearLayoutManager(getApplicationContext());
        recyclerViewList.setLayoutManager(layoutManager2);
        recyclerViewList.setHasFixedSize(true);

        fetchCatbanner();
        fetchGridShop();
        fetchListShop();

    }


    public void fetchGridShop()
    {
        shimmerFrameLayout.startShimmerAnimation();

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);
        Call<List<CatShopViewModel>> call = main_interface.getShops("Agra",catType);

        call.enqueue(new Callback<List<CatShopViewModel>>() {
            @Override
            public void onResponse(Call<List<CatShopViewModel>> call, Response<List<CatShopViewModel>> response) {
                if(response.code() != 404)
                {
                    shopsGrid = response.body();
                    adapter = new CatShopsAdapter(shopsGrid,getApplicationContext(),2);
                    recyclerView.setAdapter(adapter);
                }
                else
                {
                    coming.setVisibility(View.VISIBLE);
                }


            }
            @Override
            public void onFailure(Call<List<CatShopViewModel>> call, Throwable t) {

            }
        });
    }


    public void fetchListShop()
    {

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<CatShopViewModel>> call = main_interface.getShops("Agra",catType);

        call.enqueue(new Callback<List<CatShopViewModel>>() {
            @Override
            public void onResponse(Call<List<CatShopViewModel>> call, Response<List<CatShopViewModel>> response) {
                if(response.code() != 404)
                {
                    shopsList = response.body();
                    adapter2 = new CatShopsAdapter(shopsList,getApplicationContext(),1);
                    recyclerViewList.setAdapter(adapter2);
                }
                else
                {

                }

                shimmerFrameLayout.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<CatShopViewModel>> call, Throwable t) {

            }
        });
    }

  /*  public void fetchCatLatest()
    {
        shimmerFrameLayout.setVisibility(View.VISIBLE);

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<CatShopViewModel>> call = main_interface.getPrimes("prime");

        call.enqueue(new Callback<List<CatShopViewModel>>() {
            @Override
            public void onResponse(Call<List<CatShopViewModel>> call, Response<List<CatShopViewModel>> response) {

                shopCats = response.body();
                adapter3 = new CatShopsAdapter(shopCats,getApplicationContext(),4);
                recyclerViewCats.setAdapter(adapter3);

            }


            @Override
            public void onFailure(Call<List<CatShopViewModel>> call, Throwable t) {

            }
        });
    }
*/


    private void showFilterPopup() {

        final BottomSheetDialog filter = new BottomSheetDialog(this);
        filter.setContentView(R.layout.filterpoup);

        final RadioGroup radioGroup;

        filter.setCanceledOnTouchOutside(false);

        filter.show();
        Button apply;

        radioGroup = filter.findViewById(R.id.filterGroup);
        apply = filter.findViewById(R.id.filterApply);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                RadioButton radio = (RadioButton) filter.findViewById(selectedId);
                String btn = radio.getText().toString();

                if(btn.contains("Popularity"))
                    filterActivity(btn,"filterPop");

                if(btn.contains("Rating"))
                    filterActivity(btn,"filterPop");

                if(btn.equals(" Cost: Low to High"))
                    filterActivity("low","filterCost");

                if(btn.equals(" Cost: High to Low"))
                    filterActivity("high","filterCost");
            }
        });
    }

    void filterActivity(String type,String data)
    {
        Intent id = new Intent(getApplicationContext(), SearchShopsHome.class);
        id.putExtra("searchData",type);
        id.putExtra("searchType", data);
        id.putExtra("cat", catType);
        startActivity(id);
    }


    public void fetchCatbanner()
    {
        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().getCatBanner(catType);
        call.enqueue(new Callback<DefaultResponse>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code() == 201) {

                    String data = dr.getMessage();
                    String[] dif = data.split("#");
                    if(dif[0].length()!=0)
                    {
                        Glide.with(getApplicationContext()).load(BASE_URL_A + "catbanner/"+dif[0]).dontAnimate().into(bannerCat);
                    }
                    else
                    {
                    }

                }
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }

        });
    }






    public void backtoActivity(View view) {
        super.onBackPressed();
    }

}
