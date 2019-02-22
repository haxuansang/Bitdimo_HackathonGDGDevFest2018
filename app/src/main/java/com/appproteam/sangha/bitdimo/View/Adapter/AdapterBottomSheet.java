package com.appproteam.sangha.bitdimo.View.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appproteam.sangha.bitdimo.Presenter.Objects.RoadObject;
import com.appproteam.sangha.bitdimo.R;
import com.appproteam.sangha.bitdimo.View.CallBack.MapActions;

import java.util.List;

public class AdapterBottomSheet extends RecyclerView.Adapter<AdapterBottomSheet.viewHolderBottom> {
    List<RoadObject> listRoadObject;
    Context mContext;
    Activity mActivity;
    MapActions mapActions;

    public AdapterBottomSheet(List<RoadObject> listRoadObject, Context mContext, Activity mActivity,MapActions mapActions) {
        this.listRoadObject = listRoadObject;
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mapActions = mapActions;
    }

    @NonNull
    @Override
    public AdapterBottomSheet.viewHolderBottom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolderBottom(LayoutInflater.from(parent.getContext()).inflate(R.layout.element_bottomsheet,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBottomSheet.viewHolderBottom holder, final int position) {

        RoadObject roadObject = listRoadObject.get(position);
        holder.tv_nameOfRoad.setText("Con đường thứ " + (position+1));
        holder.tv_distance.setText(roadObject.getDistance()+" m");
        int time =  Integer.parseInt(roadObject.getDuration())/60;
        holder.tv_duration.setText(time +" phút");
        holder.cv_road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapActions.chooseRoad(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listRoadObject.size();
    }
    class viewHolderBottom extends RecyclerView.ViewHolder{
        TextView tv_nameOfRoad,tv_distance,tv_duration;
        CardView cv_road;
        public viewHolderBottom(View itemView) {
            super(itemView);
            tv_nameOfRoad=itemView.findViewById(R.id.tv_nameOfRoad);
            tv_distance = itemView.findViewById(R.id.tv_distance_main);
            tv_duration = itemView.findViewById(R.id.tv_time_rod);
            cv_road = itemView.findViewById(R.id.cv_road);

        }
    }
}
