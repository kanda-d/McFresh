package com.traidev.mcfresh.HomeMenus.cart.ex;



import com.google.gson.annotations.SerializedName;

public class PromoViewModel {

    @SerializedName("id")
    private String ID;

    @SerializedName("title")
    private String Title;

    @SerializedName("desc")
    private String Desc;

    @SerializedName("code")
    private String Code;

    @SerializedName("price")
    private int Price;

    @SerializedName("upto")
    private int Upto;

    public String getID() {
        return ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getDesc() {
        return Desc;
    }

    public String getCode() {
        return Code;
    }

    public int getPrice() {
        return Price;
    }


    public int getUpto() {
        return  Upto;
    }
}




