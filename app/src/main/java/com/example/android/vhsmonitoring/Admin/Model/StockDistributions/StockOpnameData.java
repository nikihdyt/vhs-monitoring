package com.example.android.vhsmonitoring.Admin.Model.StockDistributions;

public class StockOpnameData extends StockTransactions {
    private boolean approval_handler, approval_pertamina;

    public StockOpnameData() {
        // empty constructor
    }

    public StockOpnameData(boolean approval_handler, boolean approval_pertamina) {
        this.approval_handler = approval_handler;
        this.approval_pertamina = approval_pertamina;
    }

    public StockOpnameData(String id, String id_handler, String id_pertamina, String id_customer, String id_stock, String date_sent, String date_approved, String status, String type, int amount, boolean approval_handler, boolean approval_pertamina) {
        super(id, id_handler, id_pertamina, id_customer, id_stock, date_sent, date_approved, status, type, amount);
        this.approval_handler = approval_handler;
        this.approval_pertamina = approval_pertamina;
    }

    public void checkStatus() {
        if (approval_handler && approval_pertamina) {
            setStatus("Approved");
        } else if (!approval_pertamina) {
            setStatus("Approval Needed (Pertamina)");
        }
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
}
