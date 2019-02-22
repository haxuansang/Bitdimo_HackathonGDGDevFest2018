package com.appproteam.sangha.bitdimo.Presenter.Objects;

public class MapsObject {
    double Latitude=0,Longtitude=0;

    public MapsObject(double latitude, double longtitude) {
        Latitude = latitude;
        Longtitude = longtitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(double longtitude) {
        Longtitude = longtitude;
    }
}
