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
 * @author Moswag
 */
public class ProductCategoryModel {

    private String companyName;
    private String companyAddress;
    private String companyLocation;
    private String companyImageUrl;
    private String companyID;
    private String ecocash;

    public ProductCategoryModel(String companyName, String companyAddress, String companyLocation, String companyImageUrl, String companyID, String ecocash) {
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyLocation = companyLocation;
        this.companyImageUrl = companyImageUrl;
        this.companyID = companyID;
        this.ecocash = ecocash;
    }

    public ProductCategoryModel(String companyName, String companyAddress, String companyLocation, String companyImageUrl) {
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyLocation = companyLocation;
        this.companyImageUrl = companyImageUrl;
    }

    public ProductCategoryModel() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public String getCompanyImageUrl() {
        return companyImageUrl;
    }

    public void setCompanyImageUrl(String companyImageUrl) {
        this.companyImageUrl = companyImageUrl;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getEcocash() {
        return ecocash;
    }

    public void setEcocash(String ecocash) {
        this.ecocash = ecocash;
    }
}
