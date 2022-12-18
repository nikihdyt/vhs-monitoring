package com.example.android.vhsmonitoring.Admin.Model.Users;

public class UserPertamina extends UserData {
    private String[] customerId, handlerId;

    public UserPertamina(String[] customerId, String[] handlerId) {
        this.customerId = customerId;
        this.handlerId = handlerId;
    }

    public UserPertamina(String id, String name, String password, String code, String role, String icons, String[] customerId, String[] handlerId) {
        super(id, name, password, code, role, icons);
        this.customerId = customerId;
        this.handlerId = handlerId;
    }

    public String[] getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String[] customerId) {
        this.customerId = customerId;
    }

    public String[] getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String[] handlerId) {
        this.handlerId = handlerId;
    }
}
