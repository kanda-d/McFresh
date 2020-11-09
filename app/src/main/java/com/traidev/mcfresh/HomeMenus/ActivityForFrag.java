package com.traidev.mcfresh.HomeMenus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.traidev.mcfresh.Home;
import com.traidev.mcfresh.HomeMenus.cart.CartFrag;
import com.traidev.mcfresh.HomeMenus.cart.OrderSuccessfulFrag;
import com.traidev.mcfresh.HomeMenus.delivery.DeliveryComplete;
import com.traidev.mcfresh.HomeMenus.home.CancellationTerms;
import com.traidev.mcfresh.HomeMenus.home.HomeFragment;
import com.traidev.mcfresh.HomeMenus.delivery.AddDelivery;
import com.traidev.mcfresh.HomeMenus.home.ShareFrag;
import com.traidev.mcfresh.HomeMenus.home.Terms;
import com.traidev.mcfresh.HomeMenus.home.Aboutus;
import com.traidev.mcfresh.HomeMenus.home.feedBackContact;
import com.traidev.mcfresh.HomeMenus.notify.NotificationFragment;
import com.traidev.mcfresh.HomeMenus.orders.DeliviriesFrag;
import com.traidev.mcfresh.HomeMenus.orders.FavShopsFragment;
import com.traidev.mcfresh.HomeMenus.orders.OrderDetailsFrag;
import com.traidev.mcfresh.HomeMenus.orders.OrdersFrag;
import com.traidev.mcfresh.HomeMenus.wallet.TopwalletFragment;
import com.traidev.mcfresh.HomeMenus.wallet.walletFragment;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.UserProfile.AccountSetup.VerifyOTPMobile;
import com.traidev.mcfresh.Utility.SharedPrefManager;

public class ActivityForFrag extends AppCompatActivity  {

    private FrameLayout MainMainFrame;

    public static String onResetFragment = "null";


    String Frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_frag);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Frag = extras.getString("Frag");
        }


        SharedPrefManager sh  = new SharedPrefManager(this);


        TextView toolbar = findViewById(R.id.textHeader);

       MainMainFrame = findViewById(R.id.actvityforFrag);


        if(Frag.equals("feedback"))
        {  setDefaultFragment(new feedBackContact());
            toolbar.setText("Feedback");}

        if(Frag.equals("adddel"))
        {  setDefaultFragment(new AddDelivery());
            toolbar.setText("Delivery");}

        if(Frag.equals("deliveries"))
        {  setDefaultFragment(new DeliveryComplete());
            toolbar.setText("Your Deliveries");}

        if(Frag.equals("feedback"))
        {  setDefaultFragment(new feedBackContact());
            toolbar.setText("Feedback");}

        else if(Frag.equals("verifyMobile"))
        {
            String oam = extras.getString("Mobile");
            Fragment fragment = new VerifyOTPMobile();
            Bundle b = new Bundle();
            b.putString("Mobile", oam);
            fragment.setArguments(b);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(MainMainFrame.getId(),fragment);
            fragmentTransaction.commit();

              toolbar.setText("Verify Mobile");}



        else if(Frag.equals("orderDetails"))
        {
            String oam = extras.getString("orderid");
            Fragment fragment = new OrderDetailsFrag();
            Bundle b = new Bundle();
            b.putString("orderId", oam);
            fragment.setArguments(b);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(MainMainFrame.getId(),fragment);
            fragmentTransaction.commit();

            toolbar.setText("Order Details");}

        else if(Frag.equals("cart"))
        {  setDefaultFragment(new CartFrag());  toolbar.setText("My Cart");}


        else if(Frag.equals("notify"))
        {  setDefaultFragment(new NotificationFragment());  toolbar.setText("Updates");}

        else if(Frag.equals("home"))
        {  setDefaultFragment(new HomeFragment());  toolbar.setText("Home");}

        else if(Frag.equals("topup"))
        {  setDefaultFragment(new TopwalletFragment());  toolbar.setText("Topup");}

        else if(Frag.equals("share"))
        {  setDefaultFragment(new ShareFrag());  toolbar.setText("Share Benefits");}

        else if(Frag.equals("terms"))
        {  setDefaultFragment(new Terms());  toolbar.setText("Terms & Conditions");}

        else if(Frag.equals("wallet"))
        {  setDefaultFragment(new walletFragment());  toolbar.setText("Wallet");}

        else if(Frag.equals("myorder"))
        {  setDefaultFragment(new OrdersFrag());  toolbar.setText("My Orders");}


        else if(Frag.equals("checkout"))
        {  setDefaultFragment(new OrderSuccessfulFrag());  toolbar.setText("Successful");}



        else if(Frag.equals("favShops"))
        {  setDefaultFragment(new FavShopsFragment());  toolbar.setText("Favourites Shops");}


        else if(Frag.equals("aboutPage"))
        {  setDefaultFragment(new Aboutus());  toolbar.setText("About");}



        else if(Frag.equals("cancel"))
        {  setDefaultFragment(new CancellationTerms());  toolbar.setText("Cancellation & Refunds");}

        else if(Frag.equals("offerterms"))
        {  setDefaultFragment(new AddDelivery()); toolbar.setText("Offer Terms"); }

    }

    private void setDefaultFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(MainMainFrame.getId(),fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        if(onResetFragment.equals("cart"))
        {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }

        else if(onResetFragment.equals("Promo"))
        {
            setFragment(new CartFrag());
            return;
        }

        if(Frag.equals("myorder"))
        {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }
        else
        {
            String name =  getSupportFragmentManager().findFragmentById(R.id.actvityforFrag).getClass().getSimpleName();
            if(name.contains("CartFrag"))
                super.onBackPressed();
            else
                getSupportFragmentManager().popBackStackImmediate();
        }

        super.onBackPressed();
    }

    private void setFragment(Fragment fragment) {

        Fragment myFragment = new CartFrag();
        Bundle bundle = new Bundle();

        bundle.putString("paycheck",CartFrag.payCheck);
        myFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(MainMainFrame.getId(),myFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void backtoActivity(View view) {

        if(onResetFragment.equals("cart"))
        {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }
        else if(onResetFragment.equals("Promo"))
        {
            setFragment(new CartFrag());
            return;
        }

        if(Frag.equals("myorder"))
        {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }
        else
        {
            String name =  getSupportFragmentManager().findFragmentById(R.id.actvityforFrag).getClass().getSimpleName();
            if(name.contains("CartFrag"))
                super.onBackPressed();
            else
                getSupportFragmentManager().popBackStackImmediate();
        }

        super.onBackPressed();
    }
}
