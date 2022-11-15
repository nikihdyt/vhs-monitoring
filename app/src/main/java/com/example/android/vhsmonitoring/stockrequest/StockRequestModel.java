package com.example.android.vhsmonitoring.stockrequest;

public class StockRequestModel {
    private int custLogo;
    private String custName;
    private String stokType;

    public int getCustLogo() {
        return custLogo;
    }

    public String getCustName() {
        return custName;
    }

    public String getStockType() {
        return stokType;
    }

    public void setCustLogo(int custLogo) {
        this.custLogo = custLogo;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void setStokType(String stokType) {
        this.stokType = stokType;
    }
}
