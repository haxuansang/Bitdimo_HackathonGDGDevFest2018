package com.appproteam.sangha.bitdimo.Presenter.Objects;

import java.util.ArrayList;
import java.util.List;

public class OutStandingPlace {
    public String Address;
    public String namePlace;
    public int countOfLikes;
    public int countOfComments;
    public ArrayList<String> urlImagesList;
    MapsObject mapsObject;

    public MapsObject getMapsObject() {
        return mapsObject;
    }

    public void setMapsObject(MapsObject mapsObject) {
        this.mapsObject = mapsObject;
    }

    public OutStandingPlace( String namePlace,String address, int countOfLikes, int countOfComments, ArrayList<String> urlImagesList, MapsObject mapsObject) {

        Address = address;
        this.namePlace = namePlace;
        this.countOfLikes = countOfLikes;
        this.countOfComments = countOfComments;
        this.urlImagesList = urlImagesList;
        this.mapsObject = mapsObject;
    }

    public OutStandingPlace()
    {

    }
    public ArrayList<String> getUrlImagesList() {
        return urlImagesList;
    }

    public void setUrlImagesList(ArrayList<String> urlImagesList) {
        this.urlImagesList = urlImagesList;
    }



    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public OutStandingPlace(String address, String namePlace, int countOfLikes, int countOfComments) {
        Address = address;
        this.namePlace = namePlace;
        this.countOfLikes = countOfLikes;
        this.countOfComments = countOfComments;
    }

    public String getNamePlace() {

        return namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }

    public int getCountOfLikes() {
        return countOfLikes;
    }

    public void setCountOfLikes(int countOfLikes) {
        this.countOfLikes = countOfLikes;
    }

    public int getCountOfComments() {
        return countOfComments;
    }

    public void setCountOfComments(int countOfComments) {
        this.countOfComments = countOfComments;
    }
}
