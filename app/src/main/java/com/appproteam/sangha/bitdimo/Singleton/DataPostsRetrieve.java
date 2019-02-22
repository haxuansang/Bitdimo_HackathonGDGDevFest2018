package com.appproteam.sangha.bitdimo.Singleton;

import com.appproteam.sangha.bitdimo.Retrofit.ExplorePost;

import java.util.ArrayList;
import java.util.List;

public class DataPostsRetrieve {
    public static DataPostsRetrieve dataPostsRetrieve;
    List<ExplorePost> listExplore = new ArrayList<>();
    List<OutstandingPost> listOutstanding = new ArrayList<>();

    public static DataPostsRetrieve getInstance()
    {
        if(dataPostsRetrieve ==null)
            dataPostsRetrieve = new DataPostsRetrieve();
        return dataPostsRetrieve;
    }

    public List<ExplorePost> getListExplore() {
        return listExplore;
    }

    public void setListExplore(List<ExplorePost> listExplore) {
        this.listExplore = listExplore;
    }

    public List<OutstandingPost> getListOutstanding() {
        return listOutstanding;
    }

    public void setListOutstanding(List<OutstandingPost> listOutstanding) {
        this.listOutstanding = listOutstanding;
    }

    public void addElement(ExplorePost explorePost) {
        this.listExplore.add(explorePost);
    }
}
