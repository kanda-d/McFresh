package com.traidev.mcfresh.UserProfile.AccountSetup.Forgot;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.traidev.mcfresh.Home;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.UserProfile.AccountSetup.LoginUserFrag;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class forgotOtpFrag extends Fragment {

    private FrameLayout parentFrame;
    private EditText mobile;
    private RelativeLayout SignIn;
    Dialog chooseDilog;
    EditText password_forgot;

    private PinView SixOtp;
    FirebaseAuth auth;
    RelativeLayout verifyRelative;
    private RelativeLayout VerifyOtp;
    TextView verifytext;
    ProgressBar verifyProgress;

    String mVerificationId = "";

    TextView btnText;
    ProgressBar btnProgress;

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_forgot_mobile, container, false);

        parentFrame = getActivity().findViewById(R.id.reg_frame);
        SignIn = view.findViewById(R.id.sendForgate);
        mobile = view.findViewById(R.id.mobile_login);

        Context context;
        chooseDilog = new Dialog(getContext());
        auth = FirebaseAuth.getInstance();

        btnProgress = view.findViewById(R.id.loginProgress);
        password_forgot = view.findViewById(R.id.password_forgot);
        btnText = view.findViewById(R.id.login_text);

        VerifyOtp = view.findViewById(R.id.VerifyOTPbtn);
        verifyProgress = view.findViewById(R.id.verifyProgress);
        verifytext = view.findViewById(R.id.verify_text);

        verifyRelative = view.findViewById(R.id.verifyRelative);

        SixOtp = view.findViewById(R.id.OtpPin);

        TextView forgotback = view.findViewById(R.id.forgotback);

        forgotback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new LoginUserFrag());

            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mo;
                mo = mobile.getText().toString();

                if (mo.isEmpty()) {
                    mobile.setError("Mobile number required!");
                    return;
                }

                if (mo.length() == 10)
                    forgotAccount();
                else
                    mobile.setError("10 Digit Mobile Number!");
            }

          });


        VerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(verifytext.getText().toString().equals("Verify"))
                {
                    String otp = SixOtp.getText().toString();
                    if(!otp.isEmpty())
                    {
                        verifyProgress.setVisibility(View.VISIBLE);
                        verifytext.setVisibility(View.INVISIBLE);
                        String otpc = SixOtp.getText().toString();
                        signInVerify(PhoneAuthProvider.getCredential(mVerificationId,otpc));
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Enter Code we Send you!",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    ChangeAccountDetails();
                }

            }
        });


        return view;
    }


    public void forgotAccount() {
        String mo;
        mo = mobile.getText().toString();

        btnProgress.setVisibility(View.VISIBLE);
        btnText.setVisibility(View.INVISIBLE);

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().forgotPassword(mo);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                if (response.code() == 201) {
                    SendOtptoUser(mo);
                } else if (response.code() == 422) {
                    Toast.makeText(getContext(), "Mobile number already exist!", Toast.LENGTH_LONG).show();
                }

                btnProgress.setVisibility(View.INVISIBLE);
                btnText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }
        });


    }

    private void setFragment(Fragment fragment) {
        Bundle b = new Bundle();
        b.putString("Mobile", mobile.getText().toString());
        fragment.setArguments(b);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrame.getId(), fragment);
        fragmentTransaction.commit();
    }



    private void SendOtptoUser(String mo) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + mo, 60, TimeUnit.SECONDS, getActivity(),
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        SixOtp.setText(phoneAuthCredential.getSmsCode());
                        verfiyUser();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        Toast.makeText(getContext(), "There's Problem in Verification, Try Again!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Toast.makeText(getContext(), "Reset Code Send Successfully!", Toast.LENGTH_SHORT).show();
                        verifyRelative.setVisibility(View.VISIBLE);
                        mVerificationId = s;

                    }
                });
    }


    public void signInVerify(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    verfiyUser();
                } else {
                    Toast.makeText(getContext(), "Otp is Wrong!", Toast.LENGTH_SHORT).show();
                    verifyProgress.setVisibility(View.INVISIBLE);
                    verifytext.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    public void verfiyUser() {

        password_forgot.setVisibility(View.VISIBLE);
        SixOtp.setVisibility(View.GONE);
        verifytext.setText("Set Password");
    }


    private void ChangeAccountDetails() {

        String pass = password_forgot.getText().toString();
        if (pass.length() > 0) {
            verifyProgress.setVisibility(View.VISIBLE);
            verifytext.setVisibility(View.INVISIBLE);

            String Token = FirebaseInstanceId.getInstance().getToken();

            Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().forgotVerifyUser(mobile.getText().toString(), password_forgot.getText().toString(), Token);
            call.enqueue(new Callback<DefaultResponse>() {

                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    DefaultResponse dr = response.body();

                    if (response.code() == 201) {
                        String data = dr.getMessage();
                        String[] dif = data.split("#");

                        SharedPrefManager.getInstance(getContext()).saveUser(dif[0], dif[1], dif[2]);
                        startActivity(new Intent(getContext(), Home.class));
                        getActivity().finish();
                    } else if (response.code() == 422) {
                        Toast.makeText(getContext(), "Invalid Otp !", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Some Error Occuerd!", Toast.LENGTH_LONG).show();
                    }

                    btnProgress.setVisibility(View.INVISIBLE);
                    btnText.setVisibility(View.VISIBLE);


                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            btnProgress.setVisibility(View.INVISIBLE);
            btnText.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Set a New Password!", Toast.LENGTH_LONG).show();
        }
    }


}



