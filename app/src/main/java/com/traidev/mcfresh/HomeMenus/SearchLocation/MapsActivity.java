package com.traidev.mcfresh.HomeMenus.SearchLocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.traidev.mcfresh.Category.Product.ProductsActivity;
import com.traidev.mcfresh.Category.Shops.ShopPageActivity;
import com.traidev.mcfresh.Home;
import com.traidev.mcfresh.HomeMenus.ActivityForFrag;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private int STORAGE_PERMISSION_CODE = 1;

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    LocationManager locationManager;
    String title = null;

    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private double latitide, longitude;
    EditText editText;
    String type = null;
    private int ProximityRadius = 1000;
    Bundle extras =null;
    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if (extras == null) {
                type = null;
            } else {
                type = extras.getString("from");
            }
        }

        final ChipGroup chipGroup = (ChipGroup)findViewById(R.id.chip_group);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, @IdRes int checkedId) {

                Chip chip = findViewById(checkedId);
                if (chip != null)
                    title = chip.getText().toString();
                else
                    title = null;
            }
        });



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }
         editText = findViewById(R.id.location);

        extras = getIntent().getExtras();
        if (extras != null) {
            editText.setText(extras.getString("location"));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#E6E1D9"));
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        @SuppressLint("ResourceType") View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(2);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        buildGoogleApiClient();
        getLocation();

    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
           try {
               locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 5, (android.location.LocationListener) this);
           } catch (Exception e) {
               e.printStackTrace();
           }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }



    private void requestLocPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MapsActivity.this,
                                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
        }
    }

     @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }


    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);

        if (lastLocation != null) {
        latitide = lastLocation.getLatitude();
        longitude = lastLocation.getLongitude();

            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latitide,longitude, 1);

                String loc =addresses.get(0).getAddressLine(0);
                String data = loc.replace("Agra, Uttar Pradesh","");
                String finallocation = data.replace(", India","");
                editText.setText(finallocation);

            } catch (Exception e) {
            }

        LatLng loc = new LatLng(latitide, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            MarkerOptions markerOptions = new MarkerOptions();
            // LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
           }


        }

    }


    public boolean checkUserLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        }
        else
        {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    requestLocPermission();
                }
                return;
        }

        if (requestCode == Request_User_Location_Code)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }

    }


  @SuppressLint("LongLogTag")
  @Override
    public void onLocationChanged(Location location)
    {
        latitide = location.getLatitude();
        longitude = location.getLongitude();

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> address = null;
        try {
            address = geocoder.getFromLocation(latitide, longitude, 1);

            SharedPrefManager sh = new SharedPrefManager(getApplicationContext());
            sh.setLocation(address.get(0).getThoroughfare() + ", " + address.get(0).getSubLocality());

            editText.setText(address.get(0).getAddressLine(0));


        } catch (IOException e) {
            e.printStackTrace();
        }




        lastLocation = location;

        if (currentUserLocationMarker != null)
        {
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("You Are Here ");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current));

        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if (googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }

    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public void editAddress(View view) {

        editText.setClickable(true);
        String len = editText.getText().toString();
        editText.setSelection(len.length());
    }

    public void confirmLocation(View view) {

            if(title == null)
            {
             Toast.makeText(getApplicationContext(),"Select address base!",Toast.LENGTH_LONG).show();
            }
            else {
                SharedPrefManager sh = new SharedPrefManager(getApplication());

                String loc = editText.getText().toString();

                Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().saveAddress(sh.getsUser().getUid(),loc,title);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    }
                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {

                    }
                });

                if(type.contains("main"))
                {
                    Intent cart = new Intent(this, Home.class);
                    sh.setLocation(loc);
                    startActivity(cart);
                }
                else if(type.contains("pro"))
                {
                    Intent cart = new Intent(this, ProductsActivity.class);
                    sh.setLocation(loc);
                    startActivity(cart);
                }
                else if(type.contains("cart"))
                {
                    sh.setLocation(loc);
                    Intent cart = new Intent(this, ActivityForFrag.class);
                    cart.putExtra("Frag","cart");
                    startActivity(cart);
                }

                else if(type.contains("tifinCart"))
                {
                    sh.setLocation(loc);
                    Intent cart = new Intent(this, ActivityForFrag.class);
                    cart.putExtra("Frag","tifinCart");
                    cart.putExtra("type",extras.getString("type"));
                    startActivity(cart);
                }
                else if(type.contains("shop"))
                {
                    Intent cart = new Intent(this, ShopPageActivity.class);
                    cart.putExtra("key",extras.getString("key"));
                    cart.putExtra("extra","1");
                    sh.setLocation(loc);
                    startActivity(cart);
                }
                finish();

              /*  else
                {
                    Intent cart = new Intent(this, ActivityForFrag.class);
                    cart.putExtra("Frag", "setAddress");
                    cart.putExtra("location", loc);
                    startActivity(cart);

                }*/

                finish();
            }

    }
}
