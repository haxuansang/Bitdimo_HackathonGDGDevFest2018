package com.appproteam.sangha.bitdimo.Retrofit.ObjectRetrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestUser {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("CurrentUser")
    @Expose
    private List<CurrentUser> currentUser = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CurrentUser> getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(List<CurrentUser> currentUser) {
        this.currentUser = currentUser;
    }

}