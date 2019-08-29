package com.wms.datacollectionterminal.entities;

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
}
