package com.traidev.mcfresh;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.firebase.messaging.FirebaseMessaging;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.traidev.mcfresh.HomeMenus.ActivityForFrag;
import com.traidev.mcfresh.HomeMenus.cart.ex.AddressViewModel;
import com.traidev.mcfresh.HomeMenus.cart.ex.SavedAdapter;
import com.traidev.mcfresh.HomeMenus.home.HomeFragment;
import com.traidev.mcfresh.HomeMenus.home.NavigationFragment;
import com.traidev.mcfresh.HomeMenus.delivery.AddDelivery;
import com.traidev.mcfresh.HomeMenus.orders.OrdersFrag;
import com.traidev.mcfresh.UserProfile.ProfileUpdate;
import com.traidev.mcfresh.UserProfile.UserProfile;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements  LocationListener {

    private AppBarConfiguration mAppBarConfiguration;
    private CircleImageView ShowPro;
    private DrawerLayout drawer;
    Dialog myDialog;
    SavedAdapter savedAdapter;
    LocationManager locationManager;
    public static int checkYt = 0;

    EditText SearchShops;

    List<AddressViewModel> addressModal;
    private Main_Interface main_interface;

    int nCount = 0;

    ChipNavigationBar bottomNavigation;

    TextView notify_badge;

    private FrameLayout MainFrame;

    private TextView username;
    SharedPrefManager sharedPrefManager;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ActivityForFrag.onResetFragment = "home";

        FirebaseMessaging.getInstance().subscribeToTopic("userMcFresh");

        drawer = findViewById(R.id.drawer_layout);

        sharedPrefManager = new SharedPrefManager(this);

        notify_badge = findViewById(R.id.notify_badge);
        SearchShops = findViewById(R.id.searchShops);


        SearchShops.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent id = new Intent(getApplicationContext(), SearchShopsHome.class);
                    id.putExtra("searchData",SearchShops.getText().toString().trim());
                    id.putExtra("searchType", "homeSearch");
                    startActivity(id);
                    return true;
                }
                return false;
            }
        });


        username = findViewById(R.id.user_navName);


   //        sharedPrefManager.setCity("Agra");
        myDialog = new Dialog(this);

        if (isFirstTimeStartApp()) {
            //ShareDilog();
            setFirstTimeStartStatus(false);
        }

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setItemSelected(R.id.nav_home_main,true);

        bottomNavigation.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i)
                {
                    case R.id.nav_home_main:
                        changeFragment(new HomeFragment());
                        return;
                    case R.id.nav_offers_main:
                        changeFragment(new OrdersFrag());
                        return;
                    case R.id.nav_delivery_main:
                        changeFragment(new AddDelivery());
                        return;
                    case R.id.nav_notify_main:
                        changeFragment(new NavigationFragment());
                        return;
                }
            }
        });

       fetchProfile(sharedPrefManager.getsUser().getUid());

        MainFrame = findViewById(R.id.nav_host_fragment);


    }

    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(Home.this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = locationManager.getBestProvider(criteria, true);

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            try {
                locationManager.requestLocationUpdates(provider, 0, 0, this);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
              String address = addresses.get(0).getAddressLine(0);
            String india = address.replace(", India", "");
            String city = india.replace(", Uttar Pradesh", "");
            String finald = india.replace(", Agra", "");
            sharedPrefManager.setLocation(finald);

        } catch (Exception e) {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    private void changeFragment(Fragment fragment) {

        Bundle bundle = new Bundle();
        bundle.putString("fav", "data");
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(MainFrame.getId(), fragment);
        fragmentTransaction.commit();
    }



    private void shareApp() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Join & Spread love with McFresh   \n" +
                        "Your Friend " + username.getText() + " \n" +
                        "  \nCheckout McFresh App at: https://play.google.com/store/apps/details?id=" + getPackageName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void fetchProfile(String id) {

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().getProfile(id);
        call.enqueue(new Callback<DefaultResponse>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if (response.code() == 201) {
                    String data = dr.getMessage();

                        notify_badge.setText(data);
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
/*

    public void searchLocation() {

        final BottomSheetDialog loc = new BottomSheetDialog(Home.this);
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
                    savedAdapter = new SavedAdapter(addressModal,getApplicationContext(),CLocaiton,savedAdddres);
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
                cart.putExtra("from","main");
                startActivity(cart);
            }
        });

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), MapsActivity.class);
                cart.putExtra("from","main");
                startActivity(cart);
            }
        });

        loc.show();
    }
*/


    public void alertOpen()
    {
        @SuppressLint("ResourceType")
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(getDrawable(R.raw.logo))
                .setTitle("Are you sure to logout!")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        sharedPrefManager.logoutUser();
                        startActivity(new Intent(getApplicationContext(), UserProfile.class));
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                    }
                })
                .show();
    }

  /*  void ShareDilog()
    {
        Button Update;
        myDialog.setContentView(R.layout.update_dialog);
        Update =  myDialog.findViewById(R.id.updateButton);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
*/
    void RateUs() {

        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isFirstTimeStartApp()
    {
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroSliderApp", Context.MODE_PRIVATE);
        return ref.getBoolean("FirstTimeStartFlagMcFresh",true);
    }

    private void setFirstTimeStartStatus(boolean stt)
    {
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroSliderApp",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putBoolean("FirstTimeStartFlagMcFresh",stt);
        editor.commit();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
            String name =  getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getClass().getSimpleName();
            if(name.contains("HomeFragment"))
                finish();
            else
                changeFragment(new HomeFragment());
        }
    }

    public void openProfile(View view) {
        Intent pro = new Intent(this, ProfileUpdate.class);
        pro.putExtra("Update", "profile");
        startActivity(pro);
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
