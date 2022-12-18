package com.example.android.vhsmonitoring.Admin.Model;

public class StockData {
    private String id, type, buyerId, handlerId, managerId, tank_status;
    private int price, tank_rest, sold;

    public StockData() {
        // empty constructor
    }

    public StockData(String id, String type, String buyerId, String handlerId, String managerId, String tank_status, int price, int tank_rest, int sold) {
        this.id = id;
        this.type = type;
        this.buyerId = buyerId;
        this.handlerId = handlerId;
        this.managerId = managerId;
        this.tank_status = tank_status;
        this.price = price;
        this.tank_rest = tank_rest;
        this.sold = sold;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getTank_status() {
        return tank_status;
    }

    public void setTank_status(String tank_status) {
        this.tank_status = tank_status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTank_rest() {
        return tank_rest;
    }

    public void setTank_rest(int tank_rest) {
        this.tank_rest = tank_rest;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }
}
