package com.traidev.mcfresh.HomeMenus.delivery;

import androidx.lifecycle.ViewModel;

import com.google.gson.annotations.SerializedName;

public class DeliveryModal extends ViewModel {

    @SerializedName("pick")
    private String pic;

    @SerializedName("orderId")
    private String orderId;

    @SerializedName("drop")
    private String drop;

    @SerializedName("oTime")
    private String time;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("oAmount")
    private String amount;

    @SerializedName("oType")
    private String type;

    @SerializedName("status")
    private String status;

    public String getPic() {
        return pic;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDrop() {
        return drop;
    }


    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
    public String getMobile() {
        return mobile;
    }


    public String getAmount() {
        return amount;
    }


}