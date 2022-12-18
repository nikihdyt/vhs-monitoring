package com.example.android.vhsmonitoring.Admin.Model.Users;
import java.io.Serializable;

public class UserData implements Serializable {
    private String id, name, password, code, role, icons;

    public UserData() {
        // empty constructor
    }

    public UserData(String id, String name, String password, String code, String role, String icons) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.code = code;
        this.role = role;
        this.icons = icons;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }
}
