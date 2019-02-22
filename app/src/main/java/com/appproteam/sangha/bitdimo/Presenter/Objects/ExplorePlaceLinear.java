package com.appproteam.sangha.bitdimo.Presenter.Objects;

import java.util.ArrayList;
import java.util.List;

public class ExplorePlaceLinear {
    String nameOfUser;
    String timeOfPost;
    String contentPost;
    String urlImageOfUser;
    long countOfLike;
    long countOfComments;
    MapsObject mapsObject;
    String address;
    public ExplorePlaceLinear()
    {

    }

    public String getAddress() {
        return address;
    }

    public String getNameOfUser() {
        return nameOfUser;

    }

    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
    }

    public String getTimeOfPost() {
        return timeOfPost;
    }

    public void setTimeOfPost(String timeOfPost) {
        this.timeOfPost = timeOfPost;
    }

    public String getContentPost() {
        return contentPost;
    }

    public void setContentPost(String contentPost) {
        this.contentPost = contentPost;
    }

    public String getUrlImageOfUser() {
        return urlImageOfUser;
    }

    public void setUrlImageOfUser(String urlImageOfUser) {
        this.urlImageOfUser = urlImageOfUser;
    }

    public long getCountOfLike() {
        return countOfLike;
    }

    public void setCountOfLike(Long countOfLike) {
        this.countOfLike = countOfLike;
    }

    public long getCountOfComments() {
        return countOfComments;
    }

    public void setCountOfComments(Long ocuntOfComments) {
        this.countOfComments= ocuntOfComments;
    }

    public ExplorePlaceLinear(String nameOfUser, String timeOfPost, String contentPost, String urlImageOfUser, long countOfLike, long ocuntOfComments, List<String> listURL, MapsObject mapsObject,String address) {
        this.nameOfUser = nameOfUser;
        this.timeOfPost = timeOfPost;
        this.contentPost = contentPost;
        this.urlImageOfUser = urlImageOfUser;
        this.countOfLike = countOfLike;
        this.countOfComments = ocuntOfComments;
        this.listURL = listURL;
        this.mapsObject = mapsObject;
        this.address = address;
    }

    List<String> listURL = new ArrayList<>();

    public ExplorePlaceLinear(List<String> listURL) {
        this.listURL = listURL;
    }

    public List<String> getListURL() {
        return listURL;
    }

    public void setListURL(List<String> listURL) {
        this.listURL = listURL;
    }

    public MapsObject getMapsObject() {
        return mapsObject;
    }
}
