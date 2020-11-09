package com.traidev.mcfresh.Category.Shops;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.traidev.mcfresh.Category.Shops.Adapters.RatingAdapter;
import com.traidev.mcfresh.Category.Shops.Adapters.RatingViewModal;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Main_Interface;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingCommentsActivity extends AppCompatActivity {

    RatingBar ratingBar;
    EditText reviewText;
    Button reviewButton;
    RatingBar Rating;
    TextView Name,Review;
    LinearLayout addReview;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<RatingViewModal> ratings;
    private RatingAdapter adapter;
    private Main_Interface main_interface;
    private ImageView progressImg;

    CardView userReview;
    String ShopID = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_reviews_activity);

        String shpoName= null;

        final SharedPrefManager sharedPrefManager  = new SharedPrefManager(this);


        progressImg = findViewById(R.id.updateImgProgress);

        TextView toolbar = findViewById(R.id.handyTitle);

        recyclerView = findViewById(R.id.all_ratings_shops);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        ratingBar = findViewById(R.id.RatingBar);
        addReview = findViewById(R.id.addReview);
        reviewText = findViewById(R.id.reviewEdit);
        userReview = findViewById(R.id.userReview);
        reviewButton = findViewById(R.id.submitButton);

        Rating =  findViewById(R.id.starUserRating);
        Name =  findViewById(R.id.starUserName);
        Name.setText(sharedPrefManager.getsUser().getName());
        Review = findViewById(R.id.starUserReview);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ShopID = extras.getString("key");
            shpoName = extras.getString("shopName");
            checkUser(ShopID,sharedPrefManager.getsUser().getUid());

            fetchReviews(ShopID);
        }


        toolbar.setText("Reviews of "+shpoName);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!reviewText.getText().toString().isEmpty())
                AddReview(ratingBar.getRating(),ShopID,reviewText.getText().toString(),sharedPrefManager.getsUser().getUid());
                else
                    Toast.makeText(getApplicationContext(),"Add any Comment!",Toast.LENGTH_SHORT).show();
            }
        });



    }


    public void checkUser(String id,String userid)
    {
        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().getuserReview(id,userid);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code() == 201) {
                    String data = dr.getMessage();
                    userReview.setVisibility(View.VISIBLE);
                    addReview.setVisibility(View.GONE);
                    String[] dif = data.split("#");
                    Rating.setRating(Float.parseFloat(dif[0]));
                    Review.setText(dif[1]);

                }
                else
                {
                    userReview.setVisibility(View.GONE);
                    addReview.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }

        });
    }


    public void fetchReviews(String shopID)
    {
        progressImg.setVisibility(View.VISIBLE);
        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<RatingViewModal>> call = main_interface.getShopRatings(shopID);

        call.enqueue(new Callback<List<RatingViewModal>>() {
            @Override
            public void onResponse(Call<List<RatingViewModal>> call, Response<List<RatingViewModal>> response) {
                if(response.code() != 404)
                {
                    ratings = response.body();
                    adapter = new RatingAdapter(ratings,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }
                else
                {
                }
                progressImg.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<List<RatingViewModal>> call, Throwable t) {

            }
        });
    }


    public void AddReview(Float rating, String shopID, String review, String userid)
    {
        progressImg.setVisibility(View.VISIBLE);

        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().addReviews(userid,review,rating,shopID);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                if(response.code() == 201) {
                    Toast.makeText(getApplicationContext(),"Review added for Verify!",Toast.LENGTH_SHORT).show();
                    reviewText.setText("");
                    ratingBar.setRating(0);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Some error occured!",Toast.LENGTH_SHORT).show();
                }
                progressImg.setVisibility(View.INVISIBLE);

            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }

        });
    }

    public void backtoActivity(View view) {
      super.onBackPressed();
    }

}
