package com.traidev.mcfresh.Category.Shops;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Range;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.traidev.mcfresh.Category.Shops.Adapters.ModalOffers;
import com.traidev.mcfresh.Category.Shops.Adapters.ResycleRestaurants;
import com.traidev.mcfresh.Home;
import com.traidev.mcfresh.HomeMenus.ActivityForFrag;
import com.traidev.mcfresh.HomeMenus.SearchLocation.MapsActivity;
import com.traidev.mcfresh.HomeMenus.cart.ex.AddressViewModel;
import com.traidev.mcfresh.HomeMenus.cart.ex.SavedAdapter;
import com.traidev.mcfresh.HomeMenus.cart.ex.UpdateQty;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.traidev.mcfresh.Utility.Network.RetrofitClient.BASE_URL;

public class ShopPageActivity extends AppCompatActivity implements UpdateQty {

    EditText SearchHomeEdit;

    ProgressBar proPro;
    Switch vegchange;

    public static String URL,Title,Type = "";
    BottomSheetDialog root =null;

    TextView title,address,rating,timing,area,desc,discount;
    ImageView banner;
    FrameLayout frameShow;

    int WalletAmount,ShopBal = 0;
    public static int OFFCHECK  = 0;
    TextView PrimeAddress;
    SavedAdapter savedAdapter;
    List<AddressViewModel> addressModal;

    List<ModalOffers> shops;
    CheckBox favChnage;

    private Main_Interface main_interface;

    SharedPrefManager sharedPrefManager;

    Button checkBtn;

    private List<ModalOffers> shopsOffers;
    private ResycleRestaurants adapterOffers;
    private RecyclerView shopOffersRec;
    private RecyclerView.LayoutManager layoutManagerOffers;
    TextView cartValue;
    LinearLayout linearLayout;


    int back = 0;
    String ShopID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_layout);

        sharedPrefManager  = new SharedPrefManager(this);
        checkBtn = findViewById(R.id.viewCart);

        title = findViewById(R.id.sTitle);
        banner = findViewById(R.id.bgBanner);
        discount = findViewById(R.id.sDiscount);
        cartValue = findViewById(R.id.cartValue);
        address = findViewById(R.id.sAddress);
        vegchange = findViewById(R.id.vegStatus);
//        rating = findViewById(R.id.sRating);
        timing = findViewById(R.id.sTiming);
        desc = findViewById(R.id.sDesc);
        favChnage = findViewById(R.id.fav_change);
        frameShow = findViewById(R.id.showBeforeLoadFrame);
        linearLayout = findViewById(R.id.progressLinear);
        SearchHomeEdit = findViewById(R.id.searchHome);
        proPro = findViewById(R.id.proPro);


        shopOffersRec = findViewById(R.id.ShopOffersRecycler);
        layoutManagerOffers = new LinearLayoutManager(getApplicationContext());
        shopOffersRec.setLayoutManager(layoutManagerOffers);
        shopOffersRec.setHasFixedSize(true);
//        LinearLayout clickRating = findViewById(R.id.clickRating);

        sharedPrefManager.setEmergency(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ShopID = extras.getString("key");
            back = extras.getInt("share",0);
            fetchSingleShop(ShopID);
        }

        findViewById(R.id.viewCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), ActivityForFrag.class);
                cart.putExtra("Frag", "cart");
                startActivity(cart);
            }
        });


        vegchange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    fetchVegProducts();
                }
                else
                {
                    fetchProductsLatest("all");
                }

            }
        });



        fetchProductsLatest("all");

        favChnage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavCheck(favChnage.isChecked());

            }
        });

        /*clickRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent id = new Intent(getApplicationContext(), RatingCommentsActivity.class);
                id.putExtra("key", ShopID);
                id.putExtra("shopName", title.getText().toString());
                startActivity(id);

            }
        });
*/

        SearchHomeEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    fetchProductsLatest(SearchHomeEdit.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

      /*  clickShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Message from Flado   \n" +
                                "Fidn this Shop on Flado | " + title.getText() + ", \n" +
                                area.getText()+ "  \nGet Flado App at: https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });
*/

        fetchBal(sharedPrefManager.getsUser().getUid());
    }


    public void fetchSingleShop(String ShopId)
    {
        frameShow.setVisibility(View.VISIBLE);
        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().getSingleShop(ShopId,sharedPrefManager.getsUser().getUid());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code() == 201)
                {
                    if(call.isExecuted())
                    {
                        String data = dr.getMessage();
                        String[] dif = data.split("#");
                        title.setText(dif[0]);
                        desc.setText(dif[1]);
//                        rating.setText("★ "+dif[2]);
                        timing.setText(dif[3]);

                        LocalDate today = LocalDate.now(ZoneId.of("Asia/Calcutta"));
                        DayOfWeek dow = today.getDayOfWeek();
                        String day = String.valueOf(dow);
                        day.toLowerCase();

                        String shopDay = dif[4];
                        shopDay = shopDay.toLowerCase();
                        if(day.contains(shopDay))
                        {
                            OFFCHECK = 0;
                        }
                        else
                        {
                            String someRandomTime = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());

                            Toast.makeText(getApplicationContext(),dif[9],Toast.LENGTH_LONG).show();
                            int start = Integer.parseInt(dif[9]);
                            int end = Integer.parseInt(dif[10]);
                            int time = Integer.parseInt(someRandomTime);

                            Range<Integer> myRange = Range.create(start,end);
                            if (myRange.contains(time))
                            {
                                OFFCHECK = 1;
                            }
                            else {
                                OFFCHECK = 0;
                                Toast.makeText(getApplicationContext(),"Delivery not Available at this Time!",Toast.LENGTH_SHORT).show();
                            }
                        }

                        address.setText(dif[5]);
                        Glide.with(getApplicationContext())
                                .load(BASE_URL+"con/"+dif[6]).placeholder(R.drawable.ph_product).into((banner));
                        discount.setText(dif[7]+"% Off");
                        boolean check = Boolean.parseBoolean(dif[8]);
                        favChnage.setChecked(check);

                        frameShow.setVisibility(View.GONE);
                    }
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

    public void fetchBal(String id)
    {
        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().getBal(id);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code() == 201) {
                    WalletAmount = Integer.parseInt(dr.getMessage());
                }
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }

        });
    }

    public void fetchProductsLatest(String all)
    {

        if(all.isEmpty())
        {
            all= "all";
        }

        proPro.setVisibility(View.VISIBLE);
        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<ModalOffers>> call = main_interface.getOffers(ShopID,sharedPrefManager.getsUser().getUid(),all);

        call.enqueue(new Callback<List<ModalOffers>>() {
            @Override
            public void onResponse(Call<List<ModalOffers>> call, Response<List<ModalOffers>> response) {

                if(response.code() != 404)
                {
                    shopsOffers = response.body();
                    adapterOffers = new ResycleRestaurants(shopsOffers,getApplicationContext(),checkBtn);
                    shopOffersRec.setAdapter(adapterOffers);

                }
                proPro.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<ModalOffers>> call, Throwable t) {

            }
        });
    }

    public void fetchVegProducts()
    {
        proPro.setVisibility(View.VISIBLE);
        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<ModalOffers>> call = main_interface.getVegOffers(ShopID,sharedPrefManager.getsUser().getUid());

        call.enqueue(new Callback<List<ModalOffers>>() {
            @Override
            public void onResponse(Call<List<ModalOffers>> call, Response<List<ModalOffers>> response) {

                if(response.code() != 404)
                {
                    shopsOffers = response.body();
                    adapterOffers = new ResycleRestaurants(shopsOffers,getApplicationContext(),checkBtn);
                    shopOffersRec.setAdapter(adapterOffers);
                }
                proPro.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<ModalOffers>> call, Throwable t) {

            }
        });
    }


    public void FavCheck(boolean isChecked)
    {

        favChnage.setChecked(isChecked);

        if (isChecked == true) {

            Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().AddtoShopFav(ShopID, sharedPrefManager.getsUser().getUid());
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    Toast.makeText(getApplicationContext(),"Added to Favourites",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {

                }

            });
        } else {

            Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().RemovetoShopFav(ShopID, sharedPrefManager.getsUser().getUid());
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    Toast.makeText(getApplicationContext(),"Remove from Favourites",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                }

            });
        }
    }

/*


 public void viewCart() {

        root = new BottomSheetDialog(this);
        root.setContentView(R.layout.cart_bottom_sheet);

         final ImageView emptyCart;

        final RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;
        final Button viewCart;
        TextView changeAdd;
        final ProgressBar progressImg;

       progressImg = root.findViewById(R.id.updateProgress);


         PrimeAddress = root.findViewById(R.id.addressSet);
        changeAdd = root.findViewById(R.id.changeAddress);
        viewCart = root.findViewById(R.id.viewCartBtn);
        emptyCart = root.findViewById(R.id.emptyCart);


     recyclerView = root.findViewById(R.id.all_cart_recyler);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

         PrimeAddress.setText(sharedPrefManager.getsUser().getLocation());

        String id = sharedPrefManager.getsUser().getUid();

        progressImg.setVisibility(View.VISIBLE);

        changeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLocation();
            }
        });

        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), ActivityForFrag.class);
                cart.putExtra("Frag", "cart");
                startActivity(cart);
            }
        });

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<ModalOffers>> callD = main_interface.getCartProducts(id);
        callD.enqueue(new Callback<List<ModalOffers>>() {
            @Override
            public void onResponse(Call<List<ModalOffers>> callD, Response<List<ModalOffers>> response) {
                if(response.code() != 404)
                {
                    progressImg.setVisibility(View.INVISIBLE);
                    shops = response.body();
                    adapter = new ResycleOffers(shops,getApplicationContext(),checkBtn,1,ShopPageActivity.this,cartValue,linearLayout);
                    recyclerView.setAdapter(adapter);
                    viewCart.setVisibility(View.VISIBLE);
                }
                else
                {
                    progressImg.setVisibility(View.INVISIBLE);
                    viewCart.setVisibility(View.INVISIBLE);
                    emptyCart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ModalOffers>> callD, Throwable t) {

            }
        });

        root.setCanceledOnTouchOutside(false);
        root.show();

    }
*/

    public void searchLocation() {

        final BottomSheetDialog loc = new BottomSheetDialog(ShopPageActivity.this);
        loc.setContentView(R.layout.searchlocation_modal);
        loc.setCanceledOnTouchOutside(false);
        TextView cr = loc.findViewById(R.id.useCurrent);

        final RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;
        final ProgressBar progressImg;


        final TextView addLocation,savedAdddres;
        addLocation = loc.findViewById(R.id.addAddress);
        savedAdddres = loc.findViewById(R.id.savedAdddres);

        savedAdddres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loc.dismiss();
            }
        });
        progressImg = loc.findViewById(R.id.updateImgProgress);

        recyclerView = loc.findViewById(R.id.all_saved_address);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<AddressViewModel>> callD = main_interface.getSavedAddress(sharedPrefManager.getsUser().getUid());
        callD.enqueue(new Callback<List<AddressViewModel>>() {
            @Override
            public void onResponse(Call<List<AddressViewModel>> callD, Response<List<AddressViewModel>> response) {
                if(response.code() == 201)
                {
                    progressImg.setVisibility(View.GONE);
                    addressModal = response.body();
                    savedAdapter = new SavedAdapter(addressModal,getApplicationContext(),PrimeAddress,savedAdddres);
                    recyclerView.setAdapter(savedAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                else
                {
                    progressImg.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<AddressViewModel>> callD, Throwable t) {

            }
        });


        cr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cart = new Intent(getApplicationContext(), MapsActivity.class);
                cart.putExtra("from","shop");
                cart.putExtra("key",ShopID);
                startActivity(cart);
            }
        });

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), MapsActivity.class);
                cart.putExtra("from","shop");
                cart.putExtra("key",ShopID);
                startActivity(cart);
            }
        });

        loc.show();
    }

    @Override
    public void UpdateQty(int price,int type) {

    }

    public void Sharing(View view) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey, Find this restaurant on Flado & Get Extra Discount | "+title.getText()+", "+ address.getText() +" : http://www.flado.in?id="+ShopID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }



    @Override
    public void onBackPressed() {
        if(back == 0)
            super.onBackPressed();
        else
        {
            Intent i = new Intent(this, Home.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    }
}
