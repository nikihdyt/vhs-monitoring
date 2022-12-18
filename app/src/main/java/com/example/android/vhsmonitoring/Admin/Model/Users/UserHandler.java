package com.example.android.vhsmonitoring.Admin.Model.Users;

public class UserHandler extends UserData {
    private String customerId, pertaminaId;

    public UserHandler() {
        // empty constructor
    }

    public UserHandler(String customerId, String pertaminaId) {
        this.customerId = customerId;
        this.pertaminaId = pertaminaId;
    }

    public UserHandler(String id, String name, String password, String code, String role, String icons, String customerId, String pertaminaId) {
        super(id, name, password, code, role, icons);
        this.customerId = customerId;
        this.pertaminaId = pertaminaId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPertaminaId() {
        return pertaminaId;
    }

    public void setPertaminaId(String pertaminaId) {
        this.pertaminaId = pertaminaId;
    }
}
