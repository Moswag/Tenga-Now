package com.cytex.moswag.model.entities;

public class Order {

    private String description;
    private String amount;
    private String date;
    private String status;

    public Order(){

    }

    public Order(String description, String amount, String date, String status) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
