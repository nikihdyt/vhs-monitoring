package com.example.android.vhsmonitoring.Admin.Model.StockDistributions;

public class StockTransactions {
    private String id, id_handler, id_pertamina, id_customer, id_stock, date_sent, date_approved, status, type;
    private int amount;

    public StockTransactions() {
        // empty constructor
    }

    public StockTransactions(String id, String id_handler, String id_pertamina, String id_customer, String id_stock, String date_sent, String date_approved, String status, String type, int amount) {
        this.id = id;
        this.id_handler = id_handler;
        this.id_pertamina = id_pertamina;
        this.id_customer = id_customer;
        this.id_stock = id_stock;
        this.date_sent = date_sent;
        this.date_approved = date_approved;
        this.status = status;
        this.type = type;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_handler() {
        return id_handler;
    }

    public void setId_handler(String id_handler) {
        this.id_handler = id_handler;
    }

    public String getId_pertamina() {
        return id_pertamina;
    }

    public void setId_pertamina(String id_pertamina) {
        this.id_pertamina = id_pertamina;
    }

    public String getId_customer() {
        return id_customer;
    }

    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public String getId_stock() {
        return id_stock;
    }

    public void setId_stock(String id_stock) {
        this.id_stock = id_stock;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(String date_sent) {
        this.date_sent = date_sent;
    }

    public String getDate_approved() {
        return date_approved;
    }

    public void setDate_approved(String date_approved) {
        this.date_approved = date_approved;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
