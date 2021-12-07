package com.wms.datacollectionterminal.entities;

import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductEntity {
    private Long id;
    private String productName;
    private double width;
    private double height;
    private double length;
    private double weight;
    private Date date;
    private double price;
    private int countOnWarehouse;
    private String
            barCode;

    public ProductEntity() {
    }

    public ProductEntity(Long id, String productName,
                         double width, double height,
                         double length, double weight,
                         Date date, double price) {
        this.id = id;
        this.productName = productName;
        this.width = width;
        this.height = height;
        this.length = length;
        this.weight = weight;
        this.date = date;
        this.price = price;
    }

    public ProductEntity(JSONObject json){
        try {
            this.id = json.getLong("id");
            this.productName = json.getString("product_name");
            this.width = json.getInt("width");
            this.height = json.getInt("height");
            this.length = json.getInt("length");
            this.weight = json.getInt("weight");
            this.price = json.getInt("price");
            this.countOnWarehouse = json.getInt("count_on_warehouse");
            this.barCode = json.getString("bar_code");
        }catch (Exception e){
            Log.e("test", e.getMessage());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCountOnWarehouse() {
        return countOnWarehouse;
    }

    public void setCountOnWarehouse(int countOnWarehouse) {
        this.countOnWarehouse = countOnWarehouse;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
