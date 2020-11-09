package com.traidev.mcfresh.Utility;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefManager {

    public static final String SHARED_PREF_NAME = "mySharedPreffFladoAgra";

    public static SharedPrefManager mInstance;
    private Context ctx;

    public SharedPrefManager(Context ctx)
    {
        this.ctx = ctx;
    }


    public static synchronized SharedPrefManager getInstance(Context ctx)
    {
        if(mInstance == null)
        {
            mInstance = new SharedPrefManager(ctx);
        }
        return mInstance;
    }


    public void saveUser(String name, String mobile,String Uid)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name",name);
        editor.putString("mobile",mobile);
        editor.putString("uid",Uid);
        editor.apply();
    }

    public void setCity(String city)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("city",city);
        editor.apply();
    }

    public void setLocation(String location)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("location",location);
        editor.apply();
    }

    public void setCart(int item)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("item",item);
        editor.apply();
    }


    public void setEmergency(boolean check)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("edelivery",check);
        editor.apply();
    }


    public User getsUser()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString("name",null),
                sharedPreferences.getString("mobile",null),
                sharedPreferences.getString("uid",null),
                sharedPreferences.getString("city",null),
                sharedPreferences.getString("location","Agra"),
                sharedPreferences.getInt("item",0),
                sharedPreferences.getBoolean("edelivery",false)
        );

    }

    public void logoutUser()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }

}
