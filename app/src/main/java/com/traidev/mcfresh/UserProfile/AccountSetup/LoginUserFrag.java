package com.traidev.mcfresh.UserProfile.AccountSetup;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.traidev.mcfresh.Home;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.UserProfile.AccountSetup.Forgot.forgotOtpFrag;
import com.traidev.mcfresh.UserProfile.UserProfile;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginUserFrag extends Fragment {

    private FrameLayout parentFrame;
    private EditText user, pass;
    private RelativeLayout Login;
    TextView Email, Google;
    int RC_SIGN_IN = 0;
    TextView btnText;
    String Token;
    TextView forgotPass;
    ProgressBar btnProgress;

    public GoogleSignInClient mGoogleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_user_login, container, false);

        parentFrame = getActivity().findViewById(R.id.reg_frame);
        Login = view.findViewById(R.id.loginClick);
        user = view.findViewById(R.id.email);
        pass = view.findViewById(R.id.password);

        btnProgress= view.findViewById(R.id.loginProgress);
        forgotPass = view.findViewById(R.id.forgot);

        btnText = view.findViewById(R.id.login_text);
        Token = FirebaseInstanceId.getInstance().getToken();

        Google = view.findViewById(R.id.signGoogle);

        UserProfile.onResetFragment = true;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UserProfile) getActivity()).setFragment(new forgotOtpFrag());
            }
        });


        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);


        Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGoogle();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mo,uname;
                mo = user.getText().toString();

                uname = pass.getText().toString();
                if (!mo.isEmpty() && !uname.isEmpty()) {

                    btnProgress.setVisibility(View.VISIBLE);
                    btnText.setVisibility(View.INVISIBLE);

                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().loginUser(mo, uname,Token);
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
                                Toast.makeText(getContext(), "Username or Password Wrong", Toast.LENGTH_LONG).show();
                            }

                            btnText.setVisibility(View.VISIBLE);
                            btnProgress.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            Toast.makeText(getContext(),"No Internet Connection!",Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Enter Username & Password!", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrame.getId(), fragment);
        fragmentTransaction.commit();
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
                        Toast.makeText(getContext(),"No Internet Connection!",Toast.LENGTH_LONG).show();
                    }
                });
            }
}
