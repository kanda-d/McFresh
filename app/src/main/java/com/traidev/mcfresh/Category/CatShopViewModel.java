package com.traidev.mcfresh.Category;



import com.google.gson.annotations.SerializedName;

public class CatShopViewModel {

    @SerializedName("id")
    private String ID;

    @SerializedName("title")
    private String Title;

    @SerializedName("desc")
    private String Msg;

    @SerializedName("thumb")
    private String Thumbnil;

    @SerializedName("discount")
    private String Diccount;

    @SerializedName("rating")
    private String Rating;

    @SerializedName("favShop")
    private boolean favCheck;


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

    public String getDiccount() {
        return Diccount;
    }

    public String getRating() {
        return Rating;
    }

    public boolean getCondition() {
        return favCheck;
    }


}




