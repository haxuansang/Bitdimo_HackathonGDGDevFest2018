package com.appproteam.sangha.bitdimo.Singleton;

import com.appproteam.sangha.bitdimo.Presenter.Objects.ExplorePlace;
import com.appproteam.sangha.bitdimo.Presenter.Objects.ExplorePlaceLinear;
import com.appproteam.sangha.bitdimo.Presenter.Objects.OutStandingPlace;
import com.appproteam.sangha.bitdimo.Retrofit.ExplorePost;

public class DataDetailPost {
    public static DataDetailPost dataDetailPost;
    public ExplorePost explorePlaceLinear;
    public OutstandingPost outStandingPlace;
    public String url;

    private int typeOfPost;
    public static DataDetailPost getInstance()
    {
        if (dataDetailPost==null)
            dataDetailPost = new DataDetailPost();
        return dataDetailPost;
    }
    public void setObjectPostDetail(ExplorePost explorePlaceLinear)
    {
        if(this.explorePlaceLinear!=null)
            this.explorePlaceLinear = new ExplorePost();
        this.explorePlaceLinear = explorePlaceLinear;
    }

    public ExplorePost getExplorePlaceLinear() {
        return explorePlaceLinear;
    }
    public void setObjectOutStanding (OutstandingPost outStanding)
    {
        if(this.outStandingPlace!=null)
            this.outStandingPlace = new OutstandingPost();
        this.outStandingPlace = outStanding;
    }

    public OutstandingPost getOutStandingPlace() {
        return outStandingPlace;
    }
    public void setTypeOfPost(int type)
    {
        this.typeOfPost=type;
    }

    public static DataDetailPost getDataDetailPost() {
        return dataDetailPost;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTypeOfPost() {
        return typeOfPost;

    }
}
