package com.example.android.vhsmonitoring.Admin.Model;

public class NotificationsData {
    private String id, id_user, id_stock, id_stockDistributions, messages, date;
    private boolean readStatus;

    public NotificationsData() {
        // empty constructor
    }

    public NotificationsData(String id, String id_user, String id_stock, String id_stockDistributions, String messages, String date, boolean readStatus) {
        this.id = id;
        this.id_user = id_user;
        this.id_stock = id_stock;
        this.id_stockDistributions = id_stockDistributions;
        this.messages = messages;
        this.date = date;
        this.readStatus = readStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_stock() {
        return id_stock;
    }

    public void setId_stock(String id_stock) {
        this.id_stock = id_stock;
    }

    public String getId_stockDistributions() {
        return id_stockDistributions;
    }

    public void setId_stockDistributions(String id_stockDistributions) {
        this.id_stockDistributions = id_stockDistributions;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }
}
