package com.examplmakecodeeasy.questionprep;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class WithdrawRequest {
    private String userId;
    private String emailAddress;
    private String requesstBy;


    public WithdrawRequest(String uid, String paypal, String name) {
    }

    public WithdrawRequest(String userId, String emailAddress, String requesstBy, Date createdAt) {
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.requesstBy = requesstBy;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRequesstBy() {
        return requesstBy;
    }

    public void setRequesstBy(String requesstBy) {
        this.requesstBy = requesstBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @ServerTimestamp

    private Date createdAt;



}
