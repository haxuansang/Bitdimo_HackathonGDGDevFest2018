package com.appproteam.sangha.bitdimo.Retrofit;

import java.util.List;

import com.appproteam.sangha.bitdimo.Presenter.Objects.MapsObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExplorePost {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("urlavatar")
    @Expose
    private String avatar;
    @SerializedName("areaname")
    @Expose
    private String areaname;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("number_of_like")
    @Expose
    private Integer numberOfLike;
    @SerializedName("number_of_comment")
    @Expose
    private Integer numberOfComment;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("longtitude")
    @Expose
    private String longtitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("images")
    @Expose
    private List<String> images = null;
    public boolean liked=false;
    public ExplorePost()
    {

    }
    public ExplorePost(String nameOfUser, String s, String s1, String urlOfImages, int i, int i1, List<String> listOfPlaces, MapsObject mapsObject, String s2) {
        this.username=nameOfUser;
        this.createdAt=s;
        this.content=s1;
        this.avatar=urlOfImages;
        this.numberOfLike=i;
        this.numberOfComment=i1;
        this.images=listOfPlaces;
        this.latitude=String.valueOf(mapsObject.getLatitude());
        this.longtitude=String.valueOf(mapsObject.getLongtitude());
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getNumberOfLike() {
        return numberOfLike;
    }

    public void setNumberOfLike(Integer numberOfLike) {
        this.numberOfLike = numberOfLike;
    }

    public Integer getNumberOfComment() {
        return numberOfComment;
    }

    public void setNumberOfComment(Integer numberOfComment) {
        this.numberOfComment = numberOfComment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setLike(boolean b) {
        liked= b;
    }
}