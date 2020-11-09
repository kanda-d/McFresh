package com.traidev.mcfresh.UserProfile.AccountSetup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.traidev.mcfresh.Home;
import com.traidev.mcfresh.R;


public class WelcomeScreen extends Fragment {

    private FrameLayout parentFrame;
    private Button CreateAccount;
    private Button login;
    TextView skip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.freg_welcome, container, false);

        parentFrame = getActivity().findViewById(R.id.reg_frame);
        CreateAccount = view.findViewById(R.id.createAccount);
        login = view.findViewById(R.id.loginClick);

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SendOtpFrag());
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new LoginUserFrag());
            }
        });


        return view;

    }


    private void setFragment(Fragment fragment)
    {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrame.getId(),fragment);
        fragmentTransaction.commit();
    }


}
