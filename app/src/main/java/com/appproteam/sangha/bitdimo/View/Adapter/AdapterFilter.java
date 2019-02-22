package com.appproteam.sangha.bitdimo.View.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appproteam.sangha.bitdimo.R;
import com.appproteam.sangha.bitdimo.View.CallBack.ChangeDataExplore;

public class AdapterFilter extends RecyclerView.Adapter<AdapterFilter.ViewholderFilter> {
    Context context;
    Activity activity;
    ChangeDataExplore changeDataExplore;
    public AdapterFilter(Context context, Activity activity)
    {
        this.context= context;
        this.activity=activity;
    }
    public void registerCallback(ChangeDataExplore changeDataExplore)
    {
        this.changeDataExplore = changeDataExplore;
    }

    @NonNull
    @Override
    public AdapterFilter.ViewholderFilter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_filter,parent,false);
        return new ViewholderFilter(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFilter.ViewholderFilter holder, final int position) {
            if(position==0)
            {
                holder.imv_filter.setBackgroundResource(R.drawable.ic_rating);
                holder.tv_filter.setText("Sắp xếp bài đăng theo lượt thích");
            }
            else
            {
                holder.imv_filter.setBackgroundResource(R.drawable.ic_stopwatch);
                holder.tv_filter.setText("Sắp xếp bài đăng mới nhất");
            }
            holder.cv_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                            if (position==0)
                                 changeDataExplore.followRating();
                              else
                                 changeDataExplore.followTime();

                }
            });

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewholderFilter extends RecyclerView.ViewHolder{
        ImageView imv_filter;
        TextView tv_filter;
        CardView cv_filter;
        public ViewholderFilter(View itemView) {
            super(itemView);
            imv_filter = (ImageView)itemView.findViewById(R.id.iv_filter);
            tv_filter = (TextView)itemView.findViewById(R.id.tv_filter);
            cv_filter = (CardView)itemView.findViewById(R.id.cv_filter);
        }
    }
}
