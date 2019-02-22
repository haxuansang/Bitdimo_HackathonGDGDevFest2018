package com.appproteam.sangha.bitdimo.Singleton;

import com.appproteam.sangha.bitdimo.Retrofit.ObjectRetrofit.CurrentUser;

public class CurrentDataUser {
    CurrentUser currentUser;
    public static CurrentDataUser currentDataUser;
    public static CurrentDataUser getInstance()
    {
        if (currentDataUser==null)
            currentDataUser = new CurrentDataUser();
        return currentDataUser;
    }
    public void setCurrentUser(CurrentUser currentUser)
    {
        this.currentUser = currentUser;
    }
    public CurrentUser getCurrentUser()
    {
        return this.currentUser;
    }
}
