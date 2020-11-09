package com.traidev.mcfresh.HomeMenus.home;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.traidev.mcfresh.HomeMenus.ActivityForFrag;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.UserProfile.ProfileUpdate;
import com.traidev.mcfresh.UserProfile.UserProfile;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.traidev.mcfresh.Utility.Network.RetrofitClient.BASE_URL;

public class NavigationFragment extends Fragment {


    TextView name,user;
    CircleImageView prof;

    SharedPrefManager sharedPrefManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_navigation, container, false);

        sharedPrefManager = new SharedPrefManager(getActivity());

        name = root.findViewById(R.id.user_navName);
        user = root.findViewById(R.id.user_navId);
        prof = root.findViewById(R.id.showPro);

        name.setText(sharedPrefManager.getsUser().getName());
        user.setText(sharedPrefManager.getsUser().getMobile());

        fetchProfile(sharedPrefManager.getsUser().getUid());


        root.findViewById(R.id.ordersNav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open("myorder");
            }
        });

        root.findViewById(R.id.favNav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open("favShops");
            }
        });

        root.findViewById(R.id.delNav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open("deliveries");
            }
        });

        root.findViewById(R.id.walletNav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open("wallet");
            }
        });

        root.findViewById(R.id.viewProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileUpdate.class));
            }
        });

        root.findViewById(R.id.profileNav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileUpdate.class));
            }
        });

        root.findViewById(R.id.queryNav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open("feedback");

            }
        });

        root.findViewById(R.id.feedNav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open("feedback");

            }
        });

        root.findViewById(R.id.termNav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open("terms");


            }
        });

        root.findViewById(R.id.aboutNav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open("aboutPage");

            }
        });

        root.findViewById(R.id.logNav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertOpen();

            }
        });





        return root;
    }



    public void open(String val) {
        Intent cart = new Intent(getContext(), ActivityForFrag.class);
        cart.putExtra("Frag", val);
        startActivity(cart);
    }


    public void alertOpen()
    {
        @SuppressLint("ResourceType")
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Are you sure to logout!")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        sharedPrefManager.logoutUser();
                        startActivity(new Intent(getContext(), UserProfile.class));
                        getActivity().finish();
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


    public void fetchProfile(String id) {

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().getProfilePic(id);
        call.enqueue(new Callback<DefaultResponse>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if (response.code() == 201) {
                    String data = dr.getMessage();

                    Glide.with(getActivity()).load(BASE_URL + "user/" + data).dontAnimate().centerCrop().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(R.raw.profile).into(prof);

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




}