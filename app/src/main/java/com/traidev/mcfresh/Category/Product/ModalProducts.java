package com.traidev.mcfresh.Category.Product;

import com.google.gson.annotations.SerializedName;

public class ModalProducts {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String pro;

    @SerializedName("shopId")
    private String ShopID;

    @SerializedName("thumb")
    private String banner;


    @SerializedName("price")
    private String price;

    @SerializedName("qty")
    private int qty;


    @SerializedName("disc")
    private String Disc;


    @SerializedName("desc")
    private String desc;

    public String getId() {
        return id;
    }

    public String getBanner() {
        return banner;
    }

    public String getPro() {
        return pro;
    }

    public String getShopID() {
        return ShopID;
    }


    public String getPrice() {
        return price;
    }

    public String getDisc() {
        return Disc;
    }

    public int getQty() {
        return qty;
    }

    public String getDesc() {
        return desc;
    }
}
