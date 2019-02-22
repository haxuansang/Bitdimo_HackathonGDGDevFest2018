package com.appproteam.sangha.bitdimo.Singleton;

import java.util.ArrayList;
import java.util.List;

public class DataGalery {
    public static DataGalery dataGalery;
    public List<String> listEncodeImage =new ArrayList<>();



    public static DataGalery getInstance()
    {
        if (dataGalery==null)
            dataGalery = new DataGalery();
        return  dataGalery;
    }
    public void putListEncodeImages(List<String> listImages)
    {
        this.listEncodeImage.clear();

        this.listEncodeImage.addAll(listImages);
    }
}

