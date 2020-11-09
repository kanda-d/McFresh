package com.traidev.mcfresh.Category;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.traidev.mcfresh.Utility.Network.RetrofitClient.BASE_URL_A;

public class SelectedCatShow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<CatShopViewModel> shopsGrid;
    private CatShopsAdapter adapter;
    private Main_Interface main_interface;
    private TextView title;
    private ImageView banner;
   // private ImageView progressImg;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_activity_categories);


        recyclerView = findViewById(R.id.shopGridRecyclerSelectd);
        title = findViewById(R.id.CatTilte);
        banner = findViewById(R.id.bgBannerCat);
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("key");
            String valuet = extras.getString("title");
            String thumb = extras.getString("thumb");
            Log.d("thumbUrl",thumb);

            title.setText(valuet);

            Glide.with(getApplicationContext())
                    .load(BASE_URL_A +thumb).into((banner));

            fetchGridShop(value);
        }
    }

   public void fetchGridShop(String cat)
    {
        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<CatShopViewModel>> call = main_interface.getCatShops(cat);

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
                {  }
            }
            @Override
            public void onFailure(Call<List<CatShopViewModel>> call, Throwable t) {

            }
        });
    }





}
