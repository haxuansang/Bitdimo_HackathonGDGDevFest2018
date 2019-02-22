package com.appproteam.sangha.bitdimo.View.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appproteam.sangha.bitdimo.DetailPostActivity;
import com.appproteam.sangha.bitdimo.Presenter.Objects.ExplorePlace;
import com.appproteam.sangha.bitdimo.Presenter.Objects.ExplorePlaceLinear;
import com.appproteam.sangha.bitdimo.R;
import com.appproteam.sangha.bitdimo.Retrofit.ExplorePost;
import com.appproteam.sangha.bitdimo.Singleton.DataDetailPost;
import com.appproteam.sangha.bitdimo.SquareImageView;
import com.bumptech.glide.Glide;
import java.util.List;
import java.util.Random;

public class AdapterExplore extends RecyclerView.Adapter<AdapterExplore.ViewHolderExplore> {
    List<ExplorePost> listObjectsPlaceExploreLinear;
    Context mContext;
    Activity mActivity;
    public final static String EXTRA_ANIMAL_IMAGE_TRANSITION_NAME="transition";

    public AdapterExplore(Context mContext,List<ExplorePost> listObjectsPlace,Activity mActivity)
    {
        this.mContext=mContext;
        this.listObjectsPlaceExploreLinear=listObjectsPlace;
        this.mActivity=mActivity;
    }
    @NonNull
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public AdapterExplore.ViewHolderExplore onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_element,parent,false);
        ViewHolderExplore viewHolderExplore = new ViewHolderExplore(view);
        return  viewHolderExplore;
    }
    public void updateList(List<ExplorePost> newList)
    {
        // update list
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderExplore holder, final int position) {

        ViewCompat.setTransitionName(holder.ib_explore,EXTRA_ANIMAL_IMAGE_TRANSITION_NAME);
        Random random = new Random();
        int randomPosition=0;
        if (listObjectsPlaceExploreLinear.get(position).getImages().size()>1)
             randomPosition = random.nextInt(listObjectsPlaceExploreLinear.get(position).getImages().size()-1);
        Glide.with(mContext).load(listObjectsPlaceExploreLinear.get(position).getImages().get(randomPosition)).centerCrop().into(holder.ib_explore);
        holder.ib_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,DetailPostActivity.class);
                DataDetailPost.getInstance().setObjectPostDetail(listObjectsPlaceExploreLinear.get(position));
                intent.putExtra("stringTransition", EXTRA_ANIMAL_IMAGE_TRANSITION_NAME);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        mActivity,holder.ib_explore,ViewCompat.getTransitionName(holder.ib_explore));
                DataDetailPost.getInstance().setObjectPostDetail(listObjectsPlaceExploreLinear.get(position));
                DataDetailPost.getInstance().setTypeOfPost(1);
                mActivity.startActivity(intent,options.toBundle());
            }
        });
        /*Zoomy.Builder builder = new Zoomy.Builder(mActivity).target(holder.ib_explore);
        builder.register();*/
    }



    @Override
    public int getItemCount() {
        return listObjectsPlaceExploreLinear.size();
    }

    public class ViewHolderExplore extends RecyclerView.ViewHolder {
        SquareImageView ib_explore;

        public ViewHolderExplore(View itemView) {
            super(itemView);
            ib_explore=(SquareImageView) itemView.findViewById(R.id.ib_explore);


        }
    }

        public int calculateNoOfColumns(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int noOfColumns = (int) (dpWidth / 180);
            return noOfColumns;
        }

    }

