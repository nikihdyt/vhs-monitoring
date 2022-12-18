package com.example.android.vhsmonitoring.Admin.Model;

public class ClaimContract {
    private String id, amount, sentDate, handlerId;
    private boolean approval;

    public ClaimContract() {
        // empty constructor
    }

    public ClaimContract(String id, String amount, String sentDate, boolean approval, String handlerId) {
        this.amount = amount;
        this.sentDate = sentDate;
        this.approval = approval;
        this.id = id;
        this.handlerId = handlerId;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }
}
