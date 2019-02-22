package com.appproteam.sangha.bitdimo.Singleton;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutstandingPost {
    @SerializedName("placename")
    @Expose
    public String placename;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("areaname")
    @Expose
    public String areaname;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("number_of_like")
    @Expose
    public Integer numberOfLike;
    @SerializedName("number_of_comment")
    @Expose
    public Integer numberOfComment;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("longtitude_of_place")
    @Expose
    public String longtitude;
    @SerializedName("latitude_of_place")
    @Expose
    public String latitude;
    @SerializedName("images")
    @Expose
    public List<String> images = null;



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
    public String getPlacename() {
        return placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }
}
