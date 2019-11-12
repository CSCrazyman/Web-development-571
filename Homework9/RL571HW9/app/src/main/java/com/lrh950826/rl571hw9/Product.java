package com.lrh950826.rl571hw9;

import java.io.Serializable;

public class Product implements Serializable {

    private String title;

    private String itemId;

    private String pictureUrl;

    private String zipcode;

    private String shippingCost;

    private String condition;

    private String price;

    private boolean isWanted;

    private String titleS;

    public Product(String title, String itemId, String pictureUrl, String zipcode, String shippingCost,
                   String condition, String price, boolean isWanted) {
        this.title = title;
        this.itemId = itemId;
        this.pictureUrl = pictureUrl;
        this.zipcode = zipcode;
        this.shippingCost = shippingCost;
        this.condition = condition;
        this.price = price;
        this.isWanted = isWanted;
        this.titleS = title;
    }

    public String getTitle() {
        return title;
    }

    public String getItemId() {
        return itemId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public String getCondition() {
        return condition;
    }

    public String getPrice() {
        return price;
    }

    public String getTitleS() {
        return titleS;
    }

    public boolean getWanted() {
        return isWanted;
    }

    public void change() { this.isWanted = !this.isWanted; }

    public void setTitleS(String titleS) {
        this.titleS = titleS;
    }

}

