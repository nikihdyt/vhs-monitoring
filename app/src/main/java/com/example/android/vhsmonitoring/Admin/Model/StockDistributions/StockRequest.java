package com.example.android.vhsmonitoring.Admin.Model.StockDistributions;

public class StockRequest {
    private String id, id_pertamina, id_handler, id_stock, date_sent, address, customerName, customerLogo, customerStockType;
    private boolean status;

    public StockRequest() {
        // empty constructor
    }

    public StockRequest(String id, String id_pertamina, String id_handler, String id_stock, String date_sent, String address, String customerName, String customerLogo, String customerStockType, boolean status) {
        this.id = id;
        this.id_pertamina = id_pertamina;
        this.id_handler = id_handler;
        this.id_stock = id_stock;
        this.date_sent = date_sent;
        this.address = address;
        this.customerName = customerName;
        this.customerLogo = customerLogo;
        this.customerStockType = customerStockType;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_pertamina() {
        return id_pertamina;
    }

    public void setId_pertamina(String id_pertamina) {
        this.id_pertamina = id_pertamina;
    }

    public String getId_handler() {
        return id_handler;
    }

    public void setId_handler(String id_handler) {
        this.id_handler = id_handler;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerLogo() {
        return customerLogo;
    }

    public void setCustomerLogo(String customerLogo) {
        this.customerLogo = customerLogo;
    }

    public String getCustomerStockType() {
        return customerStockType;
    }

    public void setCustomerStockType(String customerStockType) {
        this.customerStockType = customerStockType;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
