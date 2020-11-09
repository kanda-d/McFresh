package com.traidev.mcfresh.HomeMenus.cart.ex;



import com.google.gson.annotations.SerializedName;

public class AddressViewModel {

    @SerializedName("id")
    private String ID;

    @SerializedName("title")
    private String Title;

    @SerializedName("address")
    private String Address;


    public String getID() {
        return ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getAddress() {
        return Address;
    }



}




