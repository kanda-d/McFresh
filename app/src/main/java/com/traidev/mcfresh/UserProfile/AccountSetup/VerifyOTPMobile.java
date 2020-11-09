package com.traidev.mcfresh.UserProfile.AccountSetup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.chaos.view.PinView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.traidev.mcfresh.Home;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPMobile extends Fragment {

    ProgressDialog progressDialog;

    private TextView Send;
    private PinView FourOtp;
    private FrameLayout parentFrame;
    private RelativeLayout VerifyOtp;
    String mobile,OTP;
    int CHECKSEND = 0;

    TextView userCode;

    TextView btnText;
    ProgressBar btnProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_validatemobile_otp, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Otp has Sending...");


        Send = view.findViewById(R.id.timedown);
        VerifyOtp = view.findViewById(R.id.VerifyOTPbtn);
        FourOtp = view.findViewById(R.id.OtpPin);
        userCode = view.findViewById(R.id.userCode);

        btnProgress= view.findViewById(R.id.loginProgress);
        btnText = view.findViewById(R.id.login_text);

        Bundle b = this.getArguments();
        if(b != null)
        {
            mobile = b.getString("Mobile");
            OTP = b.getString("otpS");
        }

        VerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = FourOtp.getText().toString();
                if(!otp.isEmpty())
                {
                    btnProgress.setVisibility(View.VISIBLE);
                    btnText.setVisibility(View.INVISIBLE);

                    String Token = FirebaseInstanceId.getInstance().getToken();

                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().verifyMobile(mobile,otp,userCode.getText().toString());
                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            DefaultResponse dr = response.body();

                            if(response.code() == 201)
                            {
                                SharedPrefManager sh = new SharedPrefManager(getActivity());
                                SharedPrefManager.getInstance(getContext()).saveUser(sh.getsUser().getName(),mobile,sh.getsUser().getUid());
                                Intent intent = new Intent(getContext(), Home.class);
                                startActivity(intent);
                                getActivity().finish();

                            }
                            else if(response.code() == 422)
                            {
                                Toast.makeText(getContext(),"Invalid Otp !",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(),"Some Error Occuerd!",Toast.LENGTH_LONG).show();
                            }
                            btnProgress.setVisibility(View.INVISIBLE);
                            btnText.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(getContext(),"Enter Code we Send you!",Toast.LENGTH_LONG).show();
                }

            }
        });
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                Send.setText("Resend in " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                Send.setText("Send Again!");
            }
        }.start();

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Send.getText().toString().equals("Send Again!")) {

                    if (CHECKSEND == 1) {
                        Toast.makeText(getContext(), "OTP Already sent!", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.show();

                        Send.setText("OTP Sent");
                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().resendOTP(mobile, OTP);

                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                DefaultResponse dr = response.body();
                                if (response.code() == 201) {
                                    CHECKSEND = 1;
                                    Toast.makeText(getContext(), "Otp has Sent Again!", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 422) {
                                }
                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        progressDialog.dismiss();


                    }
                }


            }
        });




        parentFrame = getActivity().findViewById(R.id.reg_frame);

        return view;
    }


    private void setFragment(Fragment fragment)
    {
        Bundle b = new Bundle();
        b.putString("Mobile", mobile);
        fragment.setArguments(b);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrame.getId(),fragment);
        fragmentTransaction.commit();
    }

}
