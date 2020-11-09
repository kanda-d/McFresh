package com.traidev.mcfresh.Category.Shops.Adapters;

import com.google.gson.annotations.SerializedName;

public class ModalRec {

    @SerializedName("id")
    private String id;

    @SerializedName("banner")
    private String banner;

    public String getId() {
        return id;
    }

    public String getBanner() {
        return banner;
    }


}
