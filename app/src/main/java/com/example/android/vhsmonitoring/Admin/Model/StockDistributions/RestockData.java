package com.example.android.vhsmonitoring.Admin.Model.StockDistributions;

// restock combine arrived stock data and sent data from pertamina
public class RestockData {
    private String id, id_handler, id_pertamina, id_customer, address, date_sent, date_arrived, status;
    private int amount_sent, amount_arrived;
    private boolean approval_handler, approval_pertamina;

    public RestockData(String id, String id_handler, String id_pertamina, String id_customer, String address, String date_sent, String date_arrived, String status, int amount_sent, int amount_arrived, boolean approval_handler, boolean approval_pertamina) {
        this.id = id;
        this.id_handler = id_handler;
        this.id_pertamina = id_pertamina;
        this.id_customer = id_customer;
        this.address = address;
        this.date_sent = date_sent;
        this.date_arrived = date_arrived;
        this.status = status;
        this.amount_sent = amount_sent;
        this.amount_arrived = amount_arrived;
        this.approval_handler = approval_handler;
        this.approval_pertamina = approval_pertamina;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(String date_sent) {
        this.date_sent = date_sent;
    }

    public String getDate_arrived() {
        return date_arrived;
    }

    public void setDate_arrived(String date_arrived) {
        this.date_arrived = date_arrived;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount_sent() {
        return amount_sent;
    }

    public void setAmount_sent(int amount_sent) {
        this.amount_sent = amount_sent;
    }

    public int getAmount_arrived() {
        return amount_arrived;
    }

    public void setAmount_arrived(int amount_arrived) {
        this.amount_arrived = amount_arrived;
    }

    public boolean isApproval_handler() {
        return approval_handler;
    }

    public void setApproval_handler(boolean approval_handler) {
        this.approval_handler = approval_handler;
    }

    public boolean isApproval_pertamina() {
        return approval_pertamina;
    }

    public void setApproval_pertamina(boolean approval_pertamina) {
        this.approval_pertamina = approval_pertamina;
    }

    public void checkStatus() {
        if (approval_handler && approval_pertamina) {
            setStatus("Approved");
        } else if (!approval_pertamina) {
            setStatus("Approval Needed (Pertamina)");
        }
    }
}
