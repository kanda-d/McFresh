package com.traidev.mcfresh.Category.Product;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.traidev.mcfresh.Category.Shops.Adapters.ModalOffers;
import com.traidev.mcfresh.Category.Shops.Adapters.ResycleGrocery;
import com.traidev.mcfresh.HomeMenus.ActivityForFrag;
import com.traidev.mcfresh.HomeMenus.SearchLocation.MapsActivity;
import com.traidev.mcfresh.HomeMenus.cart.ex.AddressViewModel;
import com.traidev.mcfresh.HomeMenus.cart.ex.SavedAdapter;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.net.URLEncoder;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity  {

    EditText SearchProdctCategoryEdit;
    Button checkBtn;
    List<ModalOffers> shops;

    int CARTOPEN = 0;
    TextView PrimeAddress;
    EditText SearchHomeEdit;
    TextView   eDelivery;
    Dialog WhatsappDialog;
    SavedAdapter savedAdapter;
    List<AddressViewModel> addressModal;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<ModalOffers> shopsGrid;
    private ResycleGrocery adapter;
    private Main_Interface main_interface;

    Context context;
    TextView cart_badge;
    GifImageView updateImgProgress;

    SharedPrefManager sharedPrefManager;
    Bundle extras;
    LinearLayout linearLayout;
    String catType;
    BottomSheetDialog root =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_activity);

        WhatsappDialog = new Dialog(ProductsActivity.this);

        TextView toolbar = findViewById(R.id.textHeader);
        context = getApplicationContext();
        updateImgProgress = findViewById(R.id.updateImgProgress);
        sharedPrefManager = new SharedPrefManager(ProductsActivity.this);
        linearLayout = findViewById(R.id.progressLinear);

        sharedPrefManager.setEmergency(false);


        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if (extras == null) {
                catType = null;
            } else {
                catType = extras.getString("catName");
                if(extras.getString("edeliver") != null)
                {
                    sharedPrefManager.setEmergency(true);
                }

            }
        }

        checkBtn = findViewById(R.id.checkoutProductBtn);
        recyclerView = findViewById(R.id.productsGridRecy);
        layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        SearchHomeEdit = findViewById(R.id.searchProducts);

        SearchHomeEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    fetchGridProducts(SearchHomeEdit.getText().toString(),"search");
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;

                }
                return false;
            }
        });



        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cart = new Intent(getApplicationContext(), ActivityForFrag.class);
                cart.putExtra("Frag", "cart");
                startActivity(cart);
            }
        });

        fetchGridProducts("all","all");
    }

    public void fetchGridProducts(String search,String type)
    {
        recyclerView.setAdapter(null);

        updateImgProgress.setVisibility(View.VISIBLE);
        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        if(type.equals("search"))
        {
            Call<List<ModalOffers>> call = main_interface.getProducts(sharedPrefManager.getsUser().getUid(),search);

            call.enqueue(new Callback<List<ModalOffers>>() {
                @Override
                public void onResponse(Call<List<ModalOffers>> call, Response<List<ModalOffers>> response) {
                    if(response.code() != 404)
                    {
                        shopsGrid = response.body();
                        adapter = new ResycleGrocery(shopsGrid,getApplicationContext(),checkBtn);
                        recyclerView.setAdapter(adapter);
                    }
                    updateImgProgress.setVisibility(View.GONE);
                }
                @Override
                public void onFailure(Call<List<ModalOffers>> call, Throwable t) {

                }
            });
        }
        else
        {
            Call<List<ModalOffers>> call = main_interface.getProducts(sharedPrefManager.getsUser().getUid(),search);

            call.enqueue(new Callback<List<ModalOffers>>() {
                @Override
                public void onResponse(Call<List<ModalOffers>> call, Response<List<ModalOffers>> response) {
                    if(response.code() != 404)
                    {
                        shopsGrid = response.body();
                        adapter = new ResycleGrocery(shopsGrid,getApplicationContext(),checkBtn);
                        recyclerView.setAdapter(adapter);

                    }
                    updateImgProgress.setVisibility(View.GONE);
                }
                @Override
                public void onFailure(Call<List<ModalOffers>> call, Throwable t) {

                }
            });
        }


    }


    public void backtoActivity(View view) {
        super.onBackPressed();
    }

    public void openCart(View view) {
        Intent cart = new Intent(getApplicationContext(), ActivityForFrag.class);
        cart.putExtra("Frag", "cart");
        startActivity(cart);
    }

    public void openNotify(View view) {
        Intent cart = new Intent(getApplicationContext(), ActivityForFrag.class);
        cart.putExtra("Frag", "notify");
        startActivity(cart);
    }


}


