package com.judy.shopmanager;

/**
 * Created by Judy T Raj on 14-11-2016.
 */

public class Item {
    private String Type;
    private String Name;
    private String Brand;
    private String Cost;
    private String Date;
    private String Store;
    private String Idno;
    public  Item(){}

    public Item(String idno,String brand,String name, String type,String date,String cost, String store) {
        Brand = brand;
        Idno=idno;
        Name=name;
        Type=type;
        Cost=cost;
        Date=date;
        Store=store;
    }

    public String getName() {
        return Name;
    }

    public String getIdno() {
        return Idno;
    }


    public String getCost() {
        return Cost;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getBrand() {
        return Brand;
    }

    public String getStore() {
        return Store;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public String getDate() {
        return Date;
    }

    public void setIdno(String idno) {
        Idno = idno;
    }

    public void setStore(String store) {
        Store = store;

    }

    public void setCost(String cost) {
        Cost = cost;


    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public void setType(String type) {
        Type = type;
    }
}

