package com.example.android.vhsmonitoring.Admin.Model.Users;

public class UserCustomer extends UserData {
    private String stockId, handlerId, pertaminaId;

    public UserCustomer() {
        // empty constructor
    }

    public UserCustomer(String stockId, String handlerId, String pertaminaId) {
        this.stockId = stockId;
        this.handlerId = handlerId;
        this.pertaminaId = pertaminaId;
    }

    public UserCustomer(String id, String name, String password, String code, String role, String icons, String stockId, String handlerId, String pertaminaId) {
        super(id, name, password, code, role, icons);
        this.stockId = stockId;
        this.handlerId = handlerId;
        this.pertaminaId = pertaminaId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getPertaminaId() {
        return pertaminaId;
    }

    public void setPertaminaId(String pertaminaId) {
        this.pertaminaId = pertaminaId;
    }
}
