package com.traidev.mcfresh.HomeMenus.orders;



import com.google.gson.annotations.SerializedName;

public class OrderViewModa {


    @SerializedName("title")
    private String Title;

    @SerializedName("desc")
    private String Msg;

    @SerializedName("thumb")
    private String Thumbnil;

    @SerializedName("price")
    private String price;

    @SerializedName("qty")
    private String qty;


    public String getTitle() {
        return Title;
    }
    public String getPrice() {
        return price;
    }


    public String getThumbnil() {
        return Thumbnil;
    }

    public String getQty() {
        return qty;
    }

    public String getMsg() {
        return Msg;
    }


}




