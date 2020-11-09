package com.traidev.mcfresh.Category.Shops;



import com.google.gson.annotations.SerializedName;

public class SingleShopViewModel {

    @SerializedName("id")
    private String ID;

    @SerializedName("title")
    private String Title;

    @SerializedName("desc")
    private String Msg;

    @SerializedName("thumb")
    private String Thumbnil;


    public String getID() {
        return ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getThumbnil() {
        return Thumbnil;
    }

    public String getMsg() {
        return Msg;
    }


}




