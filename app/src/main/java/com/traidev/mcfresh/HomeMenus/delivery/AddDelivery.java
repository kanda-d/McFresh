package com.traidev.mcfresh.HomeMenus.delivery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.traidev.mcfresh.Home;
import com.traidev.mcfresh.HomeMenus.ActivityForFrag;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.UserProfile.ProfileUpdate;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AddDelivery extends Fragment {


    private Button addDelivery;
    private EditText orderDesc,picMobile,dropMobile,pickD,dropD,dAmount;
    private Uri postUri = null;

    String pCheck = null;


    ImageView selectBill;
    ProgressDialog progressDialog;
    RadioGroup typeTrans;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_delivery, container, false);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Delivery details proccesing....");


        typeTrans = root.findViewById(R.id.typeTrans);

        String days = "Select Category,Food,Cloths,other";
        String[] elements = days.split(",");
        List<String> fixedLenghtList = Arrays.asList(elements);

        List<String> daysd = new ArrayList<String>(fixedLenghtList);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, daysd);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);

        selectBill = root.findViewById(R.id.billUpload);
//        catSpinner.setAdapter(dataAdapter);

        pickD = root.findViewById(R.id.pAd);
        picMobile = root.findViewById(R.id.pickMobile);
        dropMobile = root.findViewById(R.id.dropMobile);
        dAmount = root.findViewById(R.id.delAmount);
        dropD = root.findViewById(R.id.dAd);
        addDelivery = root.findViewById(R.id.addDelivery);

        selectBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setMinCropResultSize(512, 512)
                        .start(getActivity());

            }
        });

        addDelivery.setOnClickListener(new View.OnClickListener() {

            Context context;
            @Override
            public void onClick(View v) {

                String pick,drop,pmob,dmob,amount,desc,transtype;


                int checkID = typeTrans.getCheckedRadioButtonId();
                if (checkID == -1)
                {
                    Toast.makeText(getContext(),"Select the Pay Type",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    String  gender = String.valueOf(checkID);
                    if(gender.equals("2131296409"))
                    {
                        transtype = "COD";
                    }
                    else
                    {
                        transtype = "wallet";
                    }
                }


                pick = pickD.getText().toString();
                drop = dropD.getText().toString();
                desc = orderDesc.getText().toString();
                pmob = picMobile.getText().toString();
                dmob = dropMobile.getText().toString();
                amount = dAmount.getText().toString();

                if(pick.isEmpty()) {  Toast.makeText(getContext(),"Add Pick Address!",Toast.LENGTH_SHORT).show(); return; }
                if(drop.isEmpty()) {   Toast.makeText(getContext(),"Add Drop Address!",Toast.LENGTH_SHORT).show(); return; }
                if(dmob.isEmpty()) {   Toast.makeText(getContext(),"Add Delivery Mobile!",Toast.LENGTH_SHORT).show(); return; }
                if(amount.isEmpty()) { Toast.makeText(getContext(),"Add Delivery Amount!",Toast.LENGTH_SHORT).show(); return; }
                if(desc.isEmpty()) { Toast.makeText(getContext(),"Add Delivery Description!",Toast.LENGTH_SHORT).show(); return; }

                SharedPrefManager sh= new SharedPrefManager(getContext());

                if(pCheck != null)
                {
                    progressDialog.show();

                    File file = new File(postUri.getPath());
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                    RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().addDeliveryPicture(fileToUpload,filename,pick,pmob,dmob,desc,"",amount,transtype,drop,sh.getsUser().getUid());
                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            DefaultResponse dr = response.body();
                            if (response.code() == 201) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Delivery Added Successfully! ", Toast.LENGTH_SHORT).show();

                            } else if (response.code() == 422) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        }
                    });
                }
                else
                {
                    progressDialog.show();

                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().addDelivery(pick,pmob,dmob,desc,"",amount,transtype,drop,sh.getsUser().getUid());
                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            DefaultResponse dr = response.body();
                            if (response.code() == 201) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Delivery Added Successfully! ", Toast.LENGTH_SHORT).show();

                                Intent cart = new Intent(getContext(), ActivityForFrag.class);
                                cart.putExtra("Frag", "deliveries");
                                startActivity(cart);

                            } else if (response.code() == 422) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });
                }

            }
        });

        return root;
    }




    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                postUri = result.getUri();
                selectBill.setImageURI(postUri);
                pCheck = "Profile";

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



}