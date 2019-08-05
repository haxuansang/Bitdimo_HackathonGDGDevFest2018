package com.appproteam.sangha.bitdimo.View.Adapter;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appproteam.sangha.bitdimo.R;
import com.appproteam.sangha.bitdimo.Singleton.DataGalery;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterAdd extends RecyclerView.Adapter<AdapterAdd.viewHolderAdd> {
    List<String> listOfEncodeImage;
    Context context;
    Activity mActivity;

    public AdapterAdd(List<String> listOfEncodeImage, Context context, Activity mActivity) {
        this.listOfEncodeImage = listOfEncodeImage;
        this.context = context;
        this.mActivity =mActivity;
    }
    public void updateNewList(List<String> newlist) {
        listOfEncodeImage.clear();
        listOfEncodeImage.addAll(newlist);
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public viewHolderAdd onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_picture_element,parent,false);
        return new viewHolderAdd(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderAdd holder, final int position) {
        String encodeImage = listOfEncodeImage.get(position);
        Glide.with(context).load(encodeImage).fitCenter().into(holder.imageView);
        holder.imb_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOfEncodeImage.remove(position);
                DataGalery.getInstance().listEncodeImage.remove(position);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfEncodeImage.size();
    }
    class viewHolderAdd extends RecyclerView.ViewHolder{
        ImageButton imb_remove;
        ImageView imageView;
        public viewHolderAdd(View itemView) {
            super(itemView);
            imb_remove=(ImageButton)itemView.findViewById(R.id.imv_remove);
            imageView=(ImageView)itemView.findViewById(R.id.imv_picture);

        }
    }
    public void resetList()
    {
        this.listOfEncodeImage.clear();
        notifyDataSetChanged();
    }
}
