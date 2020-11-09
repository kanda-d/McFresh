package com.traidev.mcfresh.Category.Shops.Adapters;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModalResCategory {



    @SerializedName("shopTitle")
    private String ShopTitle;

    @SerializedName("total")
    private String TotalPro;

    @SerializedName("products")
    private ArrayList<ModalOffers> products;


    public String getShopTitle() {
        return ShopTitle;
    }

    public ArrayList<ModalOffers> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ModalOffers> products) {
        this.products = products;
    }

    public String getTotalPro() {
        return TotalPro;
    }

}
