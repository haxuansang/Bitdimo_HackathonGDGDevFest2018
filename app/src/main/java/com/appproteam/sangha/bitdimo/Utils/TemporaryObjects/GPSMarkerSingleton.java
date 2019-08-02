package com.appproteam.sangha.bitdimo.Utils.TemporaryObjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GPSMarkerSingleton {
    private static GPSMarkerSingleton gpsMarkerSingleton;
    public ArrayList<GPSMarkerDetect> listGPSMarkerDetect = new ArrayList<>();
    public static GPSMarkerSingleton getInstance(){
        if (gpsMarkerSingleton==null) {
            gpsMarkerSingleton = new GPSMarkerSingleton();
        }
        return gpsMarkerSingleton;
    }


    public ArrayList<GPSMarkerDetect> getGpsMarkerDetect() {
        return listGPSMarkerDetect;
    }

    public void insertGpsMarkerDetect(GPSMarkerDetect gpsMarkerDetect) {
        listGPSMarkerDetect.add(gpsMarkerDetect);
    }
}
