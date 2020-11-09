package com.traidev.mcfresh.HomeMenus.orders;

import com.google.gson.annotations.SerializedName;

public class ModelOrderHistory {

    @SerializedName("shopIcon")
    private String icon;

    @SerializedName("orderId")
    private String orderId;

    @SerializedName("shopTitle")
    private String title;

    @SerializedName("shopAddress")
    private String address;

    @SerializedName("oTime")
    private String time;

    @SerializedName("desc")
    private String desc;

    @SerializedName("oItem")
    private String item;

    @SerializedName("oStatus")
    private String status;

    @SerializedName("oAmount")
    private String amount;

    @SerializedName("payType")
    private String paytype;


    public String getIcon() {
        return icon;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPaytype() {
        return paytype;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getTime() {
        return time;
    }



    public String getItem() {
        return item;
    }

    public String getStatus() {
        return status;
    }

    public String getAmount() {
        return amount;
    }

    public String getDesc() {
        return desc;
    }

}
