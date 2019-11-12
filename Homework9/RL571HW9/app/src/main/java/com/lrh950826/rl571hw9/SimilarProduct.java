package com.lrh950826.rl571hw9;

import java.io.Serializable;

public class SimilarProduct implements Serializable {

    private String title;
    private String picture;
    private String shippingCost;
    private String daysLeft;
    private String price;
    private String productUrl;

    public SimilarProduct(String title, String picture, String shippingCost, String daysLeft, String price, String productUrl){
        this.title = title;
        this.picture = picture;
        this.shippingCost = shippingCost;
        this.daysLeft = daysLeft;
        this.price = price;
        this.productUrl = productUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPicture() {
        return picture;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public String getDaysLeft() {
        return daysLeft;
    }

    public String getPrice() {
        return price;
    }

    public String getProductUrl() {
        return productUrl;
    }

}
