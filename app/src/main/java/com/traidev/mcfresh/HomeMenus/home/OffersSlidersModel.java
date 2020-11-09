package com.traidev.mcfresh.HomeMenus.home;

import com.google.gson.annotations.SerializedName;

public class OffersSlidersModel {

    @SerializedName("sliderId")
    private String sliderID;

    @SerializedName("thumb")
    private String Banner;


    public String getBanner() {
        return Banner;
    }

    public String getSliderID() {
        return sliderID;
    }


}
