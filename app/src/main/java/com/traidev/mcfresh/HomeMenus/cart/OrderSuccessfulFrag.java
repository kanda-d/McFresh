package com.traidev.mcfresh.HomeMenus.cart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.traidev.mcfresh.HomeMenus.ActivityForFrag;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.SharedPrefManager;

public class OrderSuccessfulFrag extends Fragment  {

    int WalletAmount,ShopBal = 0;

    private FrameLayout parentFrame;
    TextView cart_bg;
    TextView check; ImageView gif;
    ProgressDialog progressDialog;


    Button MyOrders;
    ImageView statusImage;
    private ImageView progressImg;
    SharedPrefManager sharedPrefManager;
    Bundle extras;
    String Amount = null;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sharedPrefManager = new SharedPrefManager(getActivity());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        View root = inflater.inflate(R.layout.activity_order_succesfull, container, false);

        check = root.findViewById(R.id.text);

        gif = root.findViewById(R.id.statusGif);
        parentFrame = getActivity().findViewById(R.id.actvityforFrag);
        MyOrders = root.findViewById(R.id.OrderButton);

        progressDialog.setTitle("Order Submitted!");
        progressDialog.setMessage("Keep Calm! Status is Updating.... ");

        progressDialog.show();


        Bundle b = this.getArguments();
        if(b != null)
        {
            progressDialog.dismiss();
        }


        MyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ActivityForFrag.class);
                i.putExtra("Frag", "myorder");
                startActivity(i);
            }
        });
        return root;
    }






}