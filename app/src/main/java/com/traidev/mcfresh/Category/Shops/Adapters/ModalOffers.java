package com.traidev.mcfresh.Category.Shops.Adapters;

import com.google.gson.annotations.SerializedName;

public class ModalOffers {

    public static final int TYPE_1 =1;
    public static final int TYPE_2 =2;


    @SerializedName("id")
    private String id;

    @SerializedName("shopId")
    private String ShopID;

    @SerializedName("banner")
    private String banner;

    @SerializedName("product")
    private String pro;

    @SerializedName("price")
    private String price;

    @SerializedName("qty")
    private int Qty;

    @SerializedName("disc")
    private String Disc;

    @SerializedName("desc")
    private String desc;

    @SerializedName("variation")
    private String variation;

    @SerializedName("mode")
    private int Model;


    public String getId() {
        return id;
    }

    public String getBanner() {
        return banner;
    }

    public String getPro() {
        return pro;
    }

    public String getPrice() {
        return price;
    }

    public int getQty() {
        return Qty;
    }

    public String getShopID() {
        return ShopID;
    }

    public String getDesc() {
        return desc;
    }

    public String getDisc() {
        return Disc;
    }

    public String getVariation() {
        return variation;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getProType() {
        return Model;
    }


}
