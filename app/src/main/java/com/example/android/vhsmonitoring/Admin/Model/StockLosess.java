package com.example.android.vhsmonitoring.Admin.Model;

public class StockLosess {
    private String id, id_stock;
    private int amount;

    public StockLosess() {
        // empty constructor
    }

    public StockLosess(String id, String id_stock, int amount) {
        this.id = id;
        this.id_stock = id_stock;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_stock() {
        return id_stock;
    }

    public void setId_stock(String id_stock) {
        this.id_stock = id_stock;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void addLosessAmount(int loseAmount) {
        setAmount(getAmount() + loseAmount);
    }
}
