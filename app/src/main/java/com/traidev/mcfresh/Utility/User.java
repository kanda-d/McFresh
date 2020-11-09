package com.traidev.mcfresh.Utility;


public class User {

    private String mobile;
    private String name;
    private String uid;
    private String city;
    private String location;
    private int item;
    private boolean edel;

    public User(String name,String mobile,String uid,String city,String location,int item,boolean edel)
    {
        this.name = name;
        this.mobile = mobile;
        this.uid = uid;
        this.city = city;
        this.location = location;
        this.item = item;
        this.edel = edel;
    }


    public String getName() {
        return name;
    }
    public String getMobile() {return mobile;}
    public String getUid() {return uid;}
    public String getCity() {return city;}
    public String getLocation() {return location;}
    public int getItem() {return item;}

    public boolean getEdel() {return edel;}



}

