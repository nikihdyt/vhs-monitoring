package com.example.android.vhsmonitoring.Admin.Model.StockDistributions;

public class DailyPickupData extends StockTransactions {
    private boolean approval_handler, approval_pertamina, approval_customer;

    public DailyPickupData() {
        // empty constructor
    }

    public DailyPickupData(boolean approval_handler, boolean approval_pertamina, boolean approval_customer) {
        this.approval_handler = approval_handler;
        this.approval_pertamina = approval_pertamina;
        this.approval_customer = approval_customer;
    }

    public DailyPickupData(String id, String id_handler, String id_pertamina, String id_customer, String id_stock, String date_sent, String date_approved, String status, String type, int amount, boolean approval_handler, boolean approval_pertamina, boolean approval_customer) {
        super(id, id_handler, id_pertamina, id_customer, id_stock, date_sent, date_approved, status, type, amount);
        this.approval_handler = approval_handler;
        this.approval_pertamina = approval_pertamina;
        this.approval_customer = approval_customer;
    }

    public void checkStatus() {
        if (approval_handler && approval_pertamina && approval_customer) {
            setStatus("Approved");
        } else if (!approval_pertamina && approval_customer && approval_handler) {
            setStatus("Approval Needed (Pertamina)");
        } else if (!approval_customer && approval_pertamina && approval_handler) {
            setStatus("Approval Needed (Customer)");
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

    public boolean isApproval_customer() {
        return approval_customer;
    }

    public void setApproval_customer(boolean approval_customer) {
        this.approval_customer = approval_customer;
    }
}
