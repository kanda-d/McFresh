package com.traidev.mcfresh.Category.Shops.Adapters;



import com.google.gson.annotations.SerializedName;

public class RatingViewModal {

    @SerializedName("id")
    private String ID;

    @SerializedName("uIcon")
    private String Pro;

    @SerializedName("uName")
    private String Name;

    @SerializedName("uRating")
    private float Rating;

    @SerializedName("uReview")
    private String Review;


    public String getID() {
        return ID;
    }

    public String getPro() {
        return Pro;
    }

    public String getName() {
        return Name;
    }

    public float getRating() {
        return Rating;
    }

    public String getReview() {
        return Review;
    }
}




