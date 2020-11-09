package com.traidev.mcfresh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.traidev.mcfresh.HomeMenus.ActivityForFrag;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import org.json.JSONObject;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    private FrameLayout parentFrame;
    TextView cart_bg;
    TextView check;
    ImageView gif;
    ProgressDialog progressDialog;
String orderType = null;
   String PrimeAddress;

    Button MyOrders;
    private ImageView progressImg;
    SharedPrefManager sharedPrefManager;
    Bundle b;
    String Amount = null;

    String orderId=null;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_succesfull);

        progressImg = findViewById(R.id.updateImgProgress);

        TextView header = findViewById(R.id.textHeader);

        sharedPrefManager = new SharedPrefManager(getApplication());
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Random rand = new Random();
        final String otp = String.format("%04d", rand.nextInt(10000));

        orderId = "ORDX"+otp+sharedPrefManager.getsUser().getUid();

        progressDialog.setTitle("Order Progressing!");
        progressDialog.setMessage("Keep Calm! Status is Updating.... ");

        check = findViewById(R.id.text);
        MyOrders = findViewById(R.id.OrderButton);
        MyOrders.setVisibility(View.GONE);


        MyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MyOrders.getText().toString().contains("My Orders"))
                {
                    Intent id = new Intent(getApplicationContext(), ActivityForFrag.class);
                    id.putExtra("Frag", "myorder");
                    startActivity(id);
                    finish();
                }
                else
                {
                    PaymentActivity.super.onBackPressed();
                }

            }
        });

        gif = findViewById(R.id.statusGif);


        if (savedInstanceState == null) {
            b = getIntent().getExtras();
            if (b == null) {
            } else {

                orderType = b.getString("orderType");

                PrimeAddress = b.getString("address","");
                if(orderType.equals("shop"))
                {

                    MyOrders.setVisibility(View.VISIBLE);
                }

                Double amount = Double.parseDouble(b.getString("oamount"));
                startPayment(amount);
            }
        }

    }

    private void AddOrder(String orderId) {

        progressDialog.show();

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().OrderNow(sharedPrefManager.getsUser().getUid(),"Paid",orderId,PrimeAddress,b.getString("oamount"),b.getString("msgorder"));
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                if (response.code() == 201) {

                    Toast.makeText(getApplicationContext(), "Order successfully Added!", Toast.LENGTH_SHORT).show();
                    sharedPrefManager.setEmergency(false);
                    sharedPrefManager.setCart(0);
                    Intent id = new Intent(getApplicationContext(), ActivityForFrag.class);
                    id.putExtra("Frag", "myorder");
                    startActivity(id);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Can't Order!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                progressImg.setVisibility(View.INVISIBLE);
            }
        });
    }


    private void AddTifinOrder(String orderId) {

        progressDialog.show();

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().OrderNowTifin(sharedPrefManager.getsUser().getUid(),
                "Paid",
                orderId,b.getString("qty"),b.getString("oamount"),b.getString("orderType"),PrimeAddress);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                if (response.code() == 201) {

                    Toast.makeText(getApplicationContext(), "Order successfully Added!", Toast.LENGTH_SHORT).show();
                    sharedPrefManager.setEmergency(false);
                    sharedPrefManager.setCart(0);
                    Intent id = new Intent(getApplicationContext(), ActivityForFrag.class);
                    id.putExtra("Frag", "myorder");
                    startActivity(id);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Can't Order!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                progressImg.setVisibility(View.INVISIBLE);
            }
        });
    }


    private void TopupAccount()
    {

        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().topUpWallet(sharedPrefManager.getsUser().getUid(),b.getString("oamount"));
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code() == 201) {
                    String data = dr.getMessage();
                    Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), ActivityForFrag.class);
                    i.putExtra("Frag", "topup");
                    startActivity(i);
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

    private void AddMember(String selmember)
    {
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().addMembership(sharedPrefManager.getsUser().getUid(), selmember);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if (response.code() == 201) {

                    Intent mem = new Intent(getApplicationContext(), ActivityForFrag.class);
                    mem.putExtra("Frag", "member");
                    startActivity(mem);
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }





    public void startPayment(double am) {

        final Activity activity = this;

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_9sVXb9hJHWB7yj");
        checkout.setImage(R.raw.logo);

        try {

            JSONObject options = new JSONObject();

            options.put("name", "Payment");
            options.put("description", "Reference No. "+orderId);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount",am*100);
            options.put("theme",new JSONObject("{color: '#ff4701'}"));

            JSONObject preFill = new JSONObject();
            options.put("prefill", preFill);

            checkout.open(activity, options);


        } catch(Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }




    public void backtoActivity(View view) {
        super.onBackPressed();
    }


    @SuppressLint("ResourceType")
    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        if(orderType.equals("shop"))
        {
            AddOrder(orderId);
        }
        else if(orderType.equals("topup"))
        {
            TopupAccount();
        }
        else if(orderType.equals("tifin"))
        {
            AddTifinOrder(orderId);
        }
        else
        {
            AddMember(orderType);
        }


        check.setText("Your Payemnt has Successful! \n Connect you Soon!");
        gif.setImageResource(R.raw.check);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            check.setText("Your Payemnt has Unsuccessful! \n");
            gif.setImageResource(R.raw.cross);
            MyOrders.setText("Try Again!");

    }
}
