package com.traidev.mcfresh.HomeMenus.notify;

import com.google.gson.annotations.SerializedName;

public class NotifyViewModel {

    @SerializedName("id")
    private String ID;

    @SerializedName("title")
    private String Title;

    @SerializedName("msg")
    private String Msg;

    @SerializedName("thumb")
    private String Thumbnil;
    @SerializedName("status")

    private String Status;


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


    public String getStatus() {
        return Status;
    }


}




