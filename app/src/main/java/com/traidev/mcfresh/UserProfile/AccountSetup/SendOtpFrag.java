package com.traidev.mcfresh.UserProfile.AccountSetup;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.traidev.mcfresh.Home;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.traidev.mcfresh.UserProfile.UserProfile.onResetFragment;


public class SendOtpFrag extends Fragment {

    private PinView SixOtp;
    FirebaseAuth auth;
    RelativeLayout verifyRelative;

    private FrameLayout parentFrame;
    String mVerificationId = "";

    private TextView Send;
    private RelativeLayout VerifyOtp;
    private TextInputEditText mobile,email,name,pass,userCode;
    TextInputLayout mInput,eInput;
    private RelativeLayout SignIn;
    TextView btnText;
    ProgressBar btnProgress;

    TextView verifytext;
    ProgressBar verifyProgress;
    TextView Google,Email;


    int RC_SIGN_IN =0;

    public GoogleSignInClient mGoogleSignInClient;

    Random rand = new Random();
    String otp = String.format("%04d", rand.nextInt(10000));
    String Token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext() , gso);

        View view = inflater.inflate(R.layout.frag_enter_mobile, container, false);

        parentFrame = getActivity().findViewById(R.id.reg_frame);
        SignIn = view.findViewById(R.id.SendOtpbtn);
        verifyRelative = view.findViewById(R.id.verifyRelative);
        mobile = view.findViewById(R.id.mobile);
        email = view.findViewById(R.id.email);
        name = view.findViewById(R.id.name);
        pass = view.findViewById(R.id.password);
        userCode = view.findViewById(R.id.userCode);
        SixOtp = view.findViewById(R.id.OtpPin);

        auth = FirebaseAuth.getInstance();

        btnProgress= view.findViewById(R.id.loginProgress);
        btnText = view.findViewById(R.id.login_text);

        VerifyOtp= view.findViewById(R.id.VerifyOTPbtn);
        verifyProgress= view.findViewById(R.id.verifyProgress);
        verifytext = view.findViewById(R.id.verify_text);
        Token = FirebaseInstanceId.getInstance().getToken();

        Google = view.findViewById(R.id.signGoogle);
        Send = view.findViewById(R.id.timedown);

        Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGoogle();
            }
        });



        onResetFragment = true;

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendOtptoUser(mobile.getText().toString());
                Send.setClickable(false);
                Send.setText("Sent");
            }
        });


        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String mo,uname,passd;
                mo = mobile.getText().toString();
                uname = name.getText().toString();
                passd = pass.getText().toString();


                if(btnText.getText().toString().contains("Send OTP"))
                {
                    if(!mo.isEmpty())
                    {
                        if(mo.length() == 10)
                        {

                            if(uname.isEmpty())
                            {
                                name.setError("Set Name");
                                return;
                            }
                            if(passd.isEmpty())
                            {
                                pass.setError("Add 6 Digit Password!");
                                return;
                            }
                            if(passd.length() < 6)
                            {
                                pass.setError("Add 6 Digit Password!");
                                return;
                            }

                            btnProgress.setVisibility(View.VISIBLE);
                            btnText.setVisibility(View.INVISIBLE);

                           Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().createUser(uname,pass.getText().toString(),mo);

                            call.enqueue(new Callback<DefaultResponse>() {
                                @Override
                                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                    DefaultResponse dr = response.body();
                                    if(response.code() == 201)
                                    {
                                        SendOtptoUser(mo);
                                    }
                                    else if(response.code() == 422)
                                    {
                                        Toast.makeText(getContext(),"Mobile number already Exist!",Toast.LENGTH_LONG).show();
                                    }

                                    btnProgress.setVisibility(View.INVISIBLE);
                                    btnText.setVisibility(View.VISIBLE);
                                }
                                @Override
                                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                    Toast.makeText(getContext(),"Slow Connection ! Try Again",Toast.LENGTH_LONG).show();
                                    btnProgress.setVisibility(View.INVISIBLE);
                                    btnText.setVisibility(View.VISIBLE);
                                }
                            });


                        }
                        else
                        {
                            mobile.setError("Enter 10 Digit Mobile Number!");
                        }
                    }
                    else
                    {
                        mobile.setError("Enter 10 Digit Mobile Number!");
                    }
                }
                else
                {
                    final String email,pass;
                    email = mobile.getText().toString();
                    pass = name.getText().toString();

                    btnProgress.setVisibility(View.VISIBLE);
                    btnText.setVisibility(View.INVISIBLE);

                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().createUserEmail(uname,email,pass,Token);

                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            DefaultResponse dr = response.body();
                            if(response.code() == 201)
                            {
                                SharedPrefManager.getInstance(getContext()).saveUser(uname,email,dr.getMessage());
                                Intent intent = new Intent(getContext(), Home.class);
                                startActivity(intent);
                                getActivity().finish();
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

            }
        });


        VerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });


        return view;
    }

    private void SendOtptoUser(String mo) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + mo,60, TimeUnit.SECONDS,
                getActivity(),

                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        SixOtp.setText(phoneAuthCredential.getSmsCode());
                        verfiyUser();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        Toast.makeText(getContext(),"There's Problem in Verification, Try Again!",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Toast.makeText(getContext(),"Code Send Successfully!",Toast.LENGTH_SHORT).show();
                        verifyRelative.setVisibility(View.VISIBLE);
                        mVerificationId = s;

                        new CountDownTimer(60000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                Send.setText("Resend in " + millisUntilFinished / 1000);
                            }
                            public void onFinish() {
                                Send.setText("Send Again!");
                            }

                        }.start();
                        super.onCodeSent(s, forceResendingToken);
                    }
                });
    }

    public void signInVerify(PhoneAuthCredential credential)
    {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    verfiyUser();
                }
                else
                {   Toast.makeText(getContext(),"Otp is Wrong!",Toast.LENGTH_SHORT).show();
                verifyProgress.setVisibility(View.INVISIBLE);
                verifytext.setVisibility(View.VISIBLE);}

            }
        });
    }




    public  void verfiyUser()
    {
        String Token = FirebaseInstanceId.getInstance().getToken();

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateUserVerify(mobile.getText().toString(),name.getText().toString(),Token);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code() == 201)
                {
                    SharedPrefManager.getInstance(getContext()).saveUser(name.getText().toString(),mobile.getText().toString(),dr.getMessage());
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

    private void SignInGoogle() {
        Google.setText("Wait...");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            SignUp(account.getEmail(),account.getDisplayName());

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Errorsign", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    void SignUp(final String email,final String name)
    {
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().createUserEmail(name,email,"",Token);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                if(response.code() == 201)
                {
                    String data = dr.getMessage();
                    String[] dif = data.split("#");

                    if(dif.length>0)
                    {
                        SharedPrefManager.getInstance(getContext()).saveUser(name,dif[0],dif[1]);
                        Intent intent = new Intent(getContext(), Home.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Try Again!",Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


}
