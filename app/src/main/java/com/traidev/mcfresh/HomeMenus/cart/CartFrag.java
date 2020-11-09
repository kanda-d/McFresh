package com.traidev.mcfresh.HomeMenus.cart;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.traidev.mcfresh.Category.CategoriesActivity;
import com.traidev.mcfresh.Category.Shops.Adapters.ModalOffers;
import com.traidev.mcfresh.Category.Shops.Adapters.ResycleCart;
import com.traidev.mcfresh.Home;
import com.traidev.mcfresh.HomeMenus.ActivityForFrag;
import com.traidev.mcfresh.HomeMenus.SearchLocation.MapsActivity;
import com.traidev.mcfresh.HomeMenus.cart.ex.AddressViewModel;
import com.traidev.mcfresh.HomeMenus.cart.ex.SavedAdapter;
import com.traidev.mcfresh.HomeMenus.cart.ex.UpdateQty;
import com.traidev.mcfresh.PaymentActivity;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.UserProfile.ProfileUpdate;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFrag extends Fragment implements UpdateQty {

    int WalletAmount,ShopBal = 0;
    double lat1=0,log1=0,lat2=0,log2=0;

    int Distance = 0;

    int MAINDISCOUNT=0;
    CardView ChoosePay;
    private FrameLayout parentFrame;
    LinearLayout progressCart;
    ProgressBar progressPay;
    TextView hideGro;

    public int GrandAmount,SubAmount,DeliveryAmount,TaxAmount;

    Random rand = new Random();
    String instmesg = "null";

    int TIP = 0;
    public static String payCheck = "Paid";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<ModalOffers> shops;
    TextView edel;
    private ResycleCart adapter;
    NestedScrollView hideScroll;
    LinearLayout hideCard;
    RelativeLayout NextStepBtn;
    private Main_Interface main_interface;
    TextView orderInstructions;
    LinearLayout emptyCart;
    private ImageView progressImg;
    SharedPrefManager sharedPrefManager;
    int CodeDiscount,upto = 0;
    Bundle extras;
    String Amount = null;

    TextView subTotal,Tax,Delivery,GrandTotal,DistanceT,DistanceAm,PromoText,PromoAmount,TaxText;

    TextView promoCode;
    TextView t20,t50,t75;
    int Rider = 0;
    TextView payType;

    TextView PrimeAddress;
    TextView clearTip;
    EditText SearchHomeEdit;
    SavedAdapter savedAdapter;
    List<AddressViewModel> addressModal;
    String orderId =null;
    RelativeLayout BlokedFrame;
    TextView cart_bg;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final String otp = String.format("%04d", rand.nextInt(10000));

        sharedPrefManager = new SharedPrefManager(getActivity());

        orderId = "ORDX"+otp+sharedPrefManager.getsUser().getUid();

        View root = inflater.inflate(R.layout.activity_cart_shop, container, false);
        ActivityForFrag.onResetFragment  = "cart";
        parentFrame = getActivity().findViewById(R.id.actvityforFrag);
        parentFrame = getActivity().findViewById(R.id.actvityforFrag);
        promoCode = root.findViewById(R.id.promoCode);
        progressCart = root.findViewById(R.id.progressCart);
        PrimeAddress = root.findViewById(R.id.addressSet);
        BlokedFrame = root.findViewById(R.id.BlokedFrame);
        PrimeAddress.setText(sharedPrefManager.getsUser().getLocation());

        PromoText = root.findViewById(R.id.promoText);
        ChoosePay = root.findViewById(R.id.ChoosePayFrame);
        PromoAmount = root.findViewById(R.id.promoAmount);
        hideCard = root.findViewById(R.id.hideCard);
        hideScroll = root.findViewById(R.id.hideScroll);

        subTotal = root.findViewById(R.id.subTotal);
        Tax = root.findViewById(R.id.addTax);
        Delivery = root.findViewById(R.id.deliveryCharge);
        GrandTotal = root.findViewById(R.id.grandAmount);

        DistanceAm = root.findViewById(R.id.distance_am);
        DistanceT = root.findViewById(R.id.distance_text);

        progressImg = root.findViewById(R.id.updateImgProgress);
        progressPay = root.findViewById(R.id.payProgress);
        emptyCart = root.findViewById(R.id.emptyCartImage);
        NextStepBtn = root.findViewById(R.id.NextStepBtn);
        NextStepBtn.setEnabled(false);
        TaxText = root.findViewById(R.id.TaxText);

        t20 = root.findViewById(R.id.a20);
        t50 = root.findViewById(R.id.a50);
        t75 = root.findViewById(R.id.a75);
        hideGro = root.findViewById(R.id.hideGro);

        root.findViewById(R.id.choosePay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoosePay.setVisibility(View.VISIBLE);
            }
        });


        payType = root. findViewById(R.id.payType);
        clearTip = root. findViewById(R.id.clearTip);
        final TextView code = root.findViewById(R.id.codeRemove);


        Bundle b = this.getArguments();
        if(b != null)
        {
            payCheck = b.getString("paycheck","Paid");

            if(payCheck.equals("Paid"))
            {
                payType.setText("Pay Online");
            }
            else if(payCheck.equals("Wallet"))
            {
                payType.setText("Pay Wallet");
            }
            else if(payCheck.equals("COD"))
            {
                payType.setText("Cash on Delivery");
            }

            if(b.getInt("discount") != 0)
            {
                CodeDiscount = b.getInt("discount");
                upto = b.getInt("upto");
                code.setVisibility(View.VISIBLE);

                promoCode.setText(b.getString("code")+" - Applied");
                PromoText.setVisibility(View.VISIBLE);
                PromoAmount.setVisibility(View.VISIBLE);
                PromoText.setText("Promo - ("+b.getString("code")+")");

            }
        }

        root.findViewById(R.id.codPayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payCheck = "COD";
                payType.setText("Cash on Delivery");
                ChoosePay.setVisibility(View.GONE);

                PromoText.setVisibility(View.GONE);
                PromoAmount.setVisibility(View.GONE);
                promoCode.setText("Add Promo Code");
                code.setVisibility(View.INVISIBLE);
                GrandAmount = GrandAmount+MAINDISCOUNT;
                GrandTotal.setText("\u20B9"+GrandAmount);

            }
        });


        root.findViewById(R.id.onlinePayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payCheck = "Paid";
                payType.setText("Pay Online");
                ChoosePay.setVisibility(View.GONE);

                PromoText.setVisibility(View.GONE);
                PromoAmount.setVisibility(View.GONE);
                promoCode.setText("Add Promo Code");
                code.setVisibility(View.INVISIBLE);
                GrandAmount = GrandAmount+MAINDISCOUNT;
                GrandTotal.setText("\u20B9"+GrandAmount);
                MAINDISCOUNT =0;

            }
        });

        root.findViewById(R.id.walletPayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payCheck = "Wallet";
                payType.setText("Pay Wallet");
                ChoosePay.setVisibility(View.GONE);

                PromoText.setVisibility(View.GONE);
                PromoAmount.setVisibility(View.GONE);
                promoCode.setText("Add Promo Code");
                code.setVisibility(View.INVISIBLE);
                GrandAmount = GrandAmount+MAINDISCOUNT;
                GrandTotal.setText("\u20B9"+GrandAmount);
                MAINDISCOUNT =0;

            }
        });


        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PromoText.setVisibility(View.GONE);
                PromoAmount.setVisibility(View.GONE);
                promoCode.setText("Add Promo Code");
                code.setVisibility(View.INVISIBLE);
                GrandAmount = GrandAmount+MAINDISCOUNT;
                GrandTotal.setText("\u20B9"+GrandAmount);
                MAINDISCOUNT =0;
            }
        });


        root.findViewById(R.id.BrowseCat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cat = new Intent(getContext(), CategoriesActivity.class);
                cat.putExtra("catName", "food");
                startActivity(cat);
            }
        });

        root.findViewById(R.id.changeAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               searchLocation();
            }
        });



        clearTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTip.setVisibility(View.INVISIBLE);
                GrandAmount -= Rider;
                GrandTotal.setText("\u20B9"+(GrandAmount));
                Rider =0;
            }
        });

        t20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTip.setVisibility(View.VISIBLE);
                GrandAmount -=Rider;
                Rider = 20;
                GrandAmount += Rider;
                GrandTotal.setText("\u20B9"+(GrandAmount));
                Toast.makeText(getContext(),"Rs. 20 Added for Rider!",Toast.LENGTH_SHORT).show();
            }
        });

        t50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTip.setVisibility(View.VISIBLE);
                GrandAmount -=Rider;
                Rider = 50;
                GrandAmount += Rider;
                GrandTotal.setText("\u20B9"+(GrandAmount));

                Toast.makeText(getContext(),"Rs. 50 Added for Rider!",Toast.LENGTH_SHORT).show();
            }
        });

        t75.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTip.setVisibility(View.VISIBLE);
                GrandAmount -=Rider;
                Rider = 75;
                GrandAmount += Rider;
                GrandTotal.setText("\u20B9"+(GrandAmount));
                Toast.makeText(getContext(),"Rs. 75 Added for Rider!",Toast.LENGTH_SHORT).show();
            }
        });

        promoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new PromoCode());
            }
        });

        recyclerView = root.findViewById(R.id.all_cart_recyler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        if(sharedPrefManager.getsUser().getLocation().contains("Agra"))
        {
            getLocationFromAddress(getContext(),sharedPrefManager.getsUser().getLocation());
        }
        else
        {

            getLocationFromAddress(getContext(),sharedPrefManager.getsUser().getLocation()+",Agra");
        }



        NextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sharedPrefManager.getsUser().getMobile().contains("gmail.com"))
                {
                    Toast.makeText(getContext(),"First Verify Your Mobile Number!",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getContext(), ProfileUpdate.class));
                    return;
                }
                else
                {
                    if(GrandAmount >= 149)
                    {
                        if(GrandAmount>=49999)
                        {
                            Toast.makeText(getContext(),"Maximum Order Amount for Delivery 49999!",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            OrderNow();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Minimum Order Amount for Delivery 149!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        fetchCartProducts(sharedPrefManager.getsUser().getUid());
        CartAmount(sharedPrefManager.getsUser().getUid());

        return root;
    }


    public void CartAmount(String id)
    {

        NextStepBtn.setEnabled(false);
        progressCart.setVisibility(View.VISIBLE);

        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().getCartAmount(id);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code()==201) {

                    if(call.isExecuted())
                    {
                        String delivery, discount;

                        String data = dr.getMessage();
                        String[] dif = data.split("#");

                        String Total = dif[0];
                        WalletAmount = Integer.parseInt(dif[1]);

                        subTotal.setText("\u20B9" + Total);
                        SubAmount = Integer.parseInt(Total);
                        DeliveryAmount = 15;



                        if (CodeDiscount != 0) {

                            if(!promoCode.getText().toString().contains("FIRST50"))
                            {

                                int afterDiscount = (SubAmount * CodeDiscount) / 100;
                                if(afterDiscount>upto)
                                {
                                    MAINDISCOUNT = upto;
                                    GrandAmount = GrandAmount - upto;
                                    GrandTotal.setText("\u20B9" + (GrandAmount));
                                    PromoAmount.setText("-\u20B9"+upto);
                                }
                                else
                                {
                                    MAINDISCOUNT = afterDiscount;
                                    GrandAmount = GrandAmount - afterDiscount;
                                    GrandTotal.setText("\u20B9" + (GrandAmount));
                                    PromoAmount.setText("-\u20B9"+afterDiscount);
                                }
                            }
                            else {

                                MAINDISCOUNT = 50;
                                GrandAmount = SubAmount - 50;
                                GrandTotal.setText("\u20B9" + (GrandAmount));
                                PromoAmount.setText("-\u20B950");
                            }
                        }


                        int DCharnge = 0;


                        if (dif[2].equals("gro")) {
                            TaxText.setText("INCLUSIVE OF TAXES");
                            Tax.setVisibility(View.INVISIBLE);
                            TaxAmount = 0;
                            hideGro.setVisibility(View.GONE);
                            DeliveryAmount = 40;
                        }

                        else {

                            double tax = Math.round((SubAmount * 5.0) / 100.0);
                            GrandAmount = (int)tax + GrandAmount;
                            TaxAmount = (int)tax;
                            Tax.setText("\u20B9" + (int)tax);

                            getLocationFromAddress(getContext(),dif[3]);

                            if(lat2!=0)
                            {
                                distance(lat1,log1,lat2,log2);
                            }

                            if(Distance>5)
                            {
                                DistanceAm.setVisibility(View.VISIBLE);
                                DistanceT.setVisibility(View.VISIBLE);
                                int Charnge  = Distance - 5;
                                DCharnge =  Charnge*8;
                                DistanceAm.setText("\u20B9"+"20");
                            }
                        }


                        GrandAmount = (SubAmount + TaxAmount + DeliveryAmount + DCharnge) - MAINDISCOUNT;

                        GrandTotal.setText("\u20B9" + GrandAmount);
                        progressCart.setVisibility(View.GONE);
                        NextStepBtn.setEnabled(true);
                        Delivery.setText("\u20B9" + DeliveryAmount);
                    }
                }
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressCart.setVisibility(View.GONE);

            }
        });
    }


    public void fetchCartProducts(String userid)
    {
        progressImg.setVisibility(View.VISIBLE);
        emptyCart.setVisibility(View.INVISIBLE);
        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<ModalOffers>> call = main_interface.getCartProducts(userid);

        call.enqueue(new Callback<List<ModalOffers>>() {
            @Override
            public void onResponse(Call<List<ModalOffers>> call, Response<List<ModalOffers>> response) {
                if(response.code() == 201)
                {
                    shops = response.body();
                    adapter = new ResycleCart(shops,getContext(),CartFrag.this,progressPay);
                    recyclerView.setAdapter(adapter);
                    //OrderButton.setText("Checkout \u20B9"+ShopBal);
                }
                else if(response.code()==402)
                {
                    BlokedFrame.setVisibility(View.VISIBLE);
                }

                else
                {
                    emptyCart.setVisibility(View.VISIBLE);
                    hideScroll.setVisibility(View.INVISIBLE);
                    hideCard.setVisibility(View.INVISIBLE);
                }

                progressImg.setVisibility(View.INVISIBLE);
            }


            @Override
            public void onFailure(Call<List<ModalOffers>> call, Throwable t) {

            }
        });
    }

    public void searchLocation() {

        final BottomSheetDialog loc = new BottomSheetDialog(getActivity());
        loc.setContentView(R.layout.searchlocation_modal);
        loc.setCanceledOnTouchOutside(false);
        TextView cr = loc.findViewById(R.id.useCurrent);

        final RecyclerView recyclerViewLoc;
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

        recyclerViewLoc = loc.findViewById(R.id.all_saved_address);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerViewLoc.setLayoutManager(layoutManager);
        recyclerViewLoc.setHasFixedSize(true);

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<AddressViewModel>> callD = main_interface.getSavedAddress(sharedPrefManager.getsUser().getUid());
        callD.enqueue(new Callback<List<AddressViewModel>>() {
            @Override
            public void onResponse(Call<List<AddressViewModel>> callD, Response<List<AddressViewModel>> response) {
                if(response.code() == 201)
                {
                    progressImg.setVisibility(View.GONE);
                    addressModal = response.body();
                    savedAdapter = new SavedAdapter(addressModal,getContext(),PrimeAddress,savedAdddres);
                    recyclerViewLoc.setAdapter(savedAdapter);
                    recyclerViewLoc.setVisibility(View.VISIBLE);
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

                Intent cart = new Intent(getContext(), MapsActivity.class);
                cart.putExtra("from","cart");
                startActivity(cart);
            }
        });

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getContext(), MapsActivity.class);
                cart.putExtra("from","cart");
                startActivity(cart);
            }
        });

        loc.show();
    }

    @Override
    public void UpdateQty(int price,int type) {

        if(type==0)
        {
            try {
                startActivity(new Intent(getActivity(),Home.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            CartAmount(sharedPrefManager.getsUser().getUid());
        }
        //GrandTotal.setText("\u20B9"+GrandAmount);
    }

    public void OrderNow() {

        if(payCheck.equals("Paid"))
        {
            Intent i = new Intent(getContext(), PaymentActivity.class);
            i.putExtra("oamount", String.valueOf(GrandAmount));
            i.putExtra("msgorder", instmesg);
            i.putExtra("orderType", "shop");
            i.putExtra("address", PrimeAddress.getText().toString());
            startActivity(i);
        }
        else if(payCheck.equals("Wallet"))
        {
            if(WalletAmount>=GrandAmount)
            {
                progressImg.setVisibility(View.VISIBLE);

                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().OrderNow(sharedPrefManager.getsUser().getUid(), payCheck, orderId, PrimeAddress.getText().toString(), String.valueOf(GrandAmount),instmesg);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        DefaultResponse dr = response.body();
                        if (response.code() == 201) {

                            Toast.makeText(getContext(), "Order successfully Added!", Toast.LENGTH_SHORT).show();
                            sharedPrefManager.setEmergency(false);
                            sharedPrefManager.setCart(0);
                            setFragment(new OrderSuccessfulFrag());

                        } else {
                            Toast.makeText(getContext(), "Can't Order!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressImg.setVisibility(View.INVISIBLE);
                    }
                });
            }
            else
            {
                Toast.makeText(getActivity(),"Topup your Wallet!",Toast.LENGTH_LONG).show();
            }

        }
        else
        {

            if(GrandAmount > 149 && !TaxText.getText().toString().contains("INCLUSIVE OF TAXES")) {
                Toast.makeText(getActivity(),"Cash on Delivery is available on orders below 199/-",Toast.LENGTH_SHORT).show();
                return;
            }


                progressImg.setVisibility(View.VISIBLE);

                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().OrderNow(sharedPrefManager.getsUser().getUid(), payCheck, orderId, PrimeAddress.getText().toString(), String.valueOf(GrandAmount),instmesg);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                        DefaultResponse dr = response.body();

                        if (response.code() == 201) {

                            Toast.makeText(getContext(), "Order successfully Added!", Toast.LENGTH_SHORT).show();
                            sharedPrefManager.setEmergency(false);
                            sharedPrefManager.setCart(0);
                            setFragment(new OrderSuccessfulFrag());

                        } else {
                            Toast.makeText(getContext(), "Can't Order!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {

                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressImg.setVisibility(View.INVISIBLE);
                    }
                });

            }

        }



    private void setFragment(Fragment fragment)
    {
        Bundle b = new Bundle();
        b.putString("status", "OK");
        b.putString("paytype", payCheck);
        fragment.setArguments(b);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrame.getId(),fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
            if(lat1==0)
            {
                lat1 = location.getLatitude();
                log1 = location.getLongitude();
            }
            else
            {
                lat2 = location.getLatitude();
                log2 = location.getLongitude();
            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }



    private double distance(double lat1, double lon1, double lat2, double lon2) {
        progressPay.setVisibility(View.VISIBLE);
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        double km = dist * 1.609344;

        km = Math.round(km);
        Distance = (int) km;
        int next  = Distance/2;
        Distance = Distance + next;
        progressPay.setVisibility(View.GONE);

        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


}