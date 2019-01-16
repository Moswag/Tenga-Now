/*
 * Copyright (c) 2018. http://moswag@cytex.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Webster Moswa <webstermoswa11@Gmail.com>, 2018.
 */

/**
 *
 */
package com.cytex.moswag.model.entities;

/**
 * The Class Product used as model for Products.
 *
 * @author Moswag
 */
public class Product {


    /**
     * The item short desc.
     */
    private String description;

    /**
     * The discount.
     */
    private String discount;

    /**
     * The sell mrp.
     */
    private String salePrice;

    /**
     * The image url.
     */
    private String imageUrl;

    /**
     * The item name.
     */
    private String productName;

    private String productId;

    private String Quantity;

    private String companyID;

    public Product(){

    }

    public Product(String description, String discount, String salePrice, String imageUrl, String productName, String productId, String Quantity,String companyID) {
        this.description = description;
        this.discount = discount;
        this.salePrice = salePrice;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.productId = productId;
        this.Quantity=Quantity;
        this.companyID=companyID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }


    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }
}
