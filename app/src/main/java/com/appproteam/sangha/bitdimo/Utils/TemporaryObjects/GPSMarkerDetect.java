package com.appproteam.sangha.bitdimo.Utils.TemporaryObjects;







import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class GPSMarkerDetect {
    long startPositionPlayer, endPositionPlayer;
    List<LatLng> listMarkerDetect = new ArrayList<>();

    public GPSMarkerDetect(long startPositionPlayer, long endPositionPlayer, List<LatLng> listMarkerDetect) {
        this.startPositionPlayer = startPositionPlayer;
        this.endPositionPlayer = endPositionPlayer;
        this.listMarkerDetect = listMarkerDetect;
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
}
