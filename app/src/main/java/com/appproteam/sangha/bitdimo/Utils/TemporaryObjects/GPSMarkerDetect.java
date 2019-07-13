package com.appproteam.sangha.bitdimo.Utils.TemporaryObjects;







import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class GPSMarkerDetect {
    long startPositionPlayer, endPositionPlayer;
    List<LatLng> listMarkerDetect = new ArrayList<>();
    LatLng mainLocation;

    public LatLng getMainLocation() {
        return mainLocation;
    }

    public void setMainLocation(LatLng mainLocation) {
        this.mainLocation = mainLocation;
    }

    public GPSMarkerDetect(long startPositionPlayer, long endPositionPlayer, List<LatLng> listMarkerDetect, LatLng mainLocation) {
        this.startPositionPlayer = startPositionPlayer;
        this.endPositionPlayer = endPositionPlayer;
        this.listMarkerDetect = listMarkerDetect;
        this.mainLocation = mainLocation;
    }

    public long getStartPositionPlayer() {
        return startPositionPlayer;
    }

    public void setStartPositionPlayer(long startPositionPlayer) {
        this.startPositionPlayer = startPositionPlayer;
    }

    public long getEndPositionPlayer() {
        return endPositionPlayer;
    }

    public void setEndPositionPlayer(long endPositionPlayer) {
        this.endPositionPlayer = endPositionPlayer;
    }

    public List<LatLng> getListMarkerDetect() {
        return listMarkerDetect;
    }

    public void setListMarkerDetect(List<LatLng> listMarkerDetect) {
        this.listMarkerDetect = listMarkerDetect;
    }
    public LatLng getLastPoint(){
        return listMarkerDetect.get(listMarkerDetect.size()-1);
    }
}
