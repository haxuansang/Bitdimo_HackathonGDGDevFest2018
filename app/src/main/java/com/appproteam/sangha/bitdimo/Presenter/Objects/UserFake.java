package com.appproteam.sangha.bitdimo.Presenter.Objects;

public class UserFake {
    String nameOfUser;
    String urlOfImages;

    public String getNameOfUser() {
        return nameOfUser;
    }

    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
    }

    public String getUrlOfImages() {
        return urlOfImages;
    }

    public void setUrlOfImages(String urlOfImages) {
        this.urlOfImages = urlOfImages;
    }

    public UserFake(String nameOfUser, String urlOfImages) {

        this.nameOfUser = nameOfUser;
        this.urlOfImages = urlOfImages;
    }
}
