package com.appproteam.sangha.bitdimo.Presenter.Objects;



public class RoadObject {
    String nameOfRoad;
    String Distance;
    String Duration;

    public RoadObject(String nameOfRoad, String distance, String duration) {
        this.nameOfRoad = nameOfRoad;
        Distance = distance;
        Duration = duration;
    }

    public String getNameOfRoad() {
        return nameOfRoad;
    }

    public void setNameOfRoad(String nameOfRoad) {
        this.nameOfRoad = nameOfRoad;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }
}
