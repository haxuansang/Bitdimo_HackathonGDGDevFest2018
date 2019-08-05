package com.appproteam.sangha.bitdimo.View.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.appproteam.sangha.bitdimo.DetailPostActivity;
import com.appproteam.sangha.bitdimo.MapsActivity;
import com.appproteam.sangha.bitdimo.Presenter.Objects.ExplorePlace;
import com.appproteam.sangha.bitdimo.Presenter.Objects.ExplorePlaceLinear;
import com.appproteam.sangha.bitdimo.R;
import com.appproteam.sangha.bitdimo.Retrofit.ExplorePost;
import com.appproteam.sangha.bitdimo.Singleton.DataDetailPost;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterExploreLinear extends RecyclerView.Adapter<AdapterExploreLinear.ViewHolderExplorerLinear> {
    Context mContext;
    List<ExplorePost> listOfExplorePlaceLinear;
    Activity mActivity;

    public AdapterExploreLinear(Context mContext, List<ExplorePost> listOfExplorePlaceLinear, Activity mActivity) {
        this.mContext = mContext;
        this.listOfExplorePlaceLinear = listOfExplorePlaceLinear;
        this.mActivity = mActivity;
    }
    public void updateList(List<ExplorePost> newList)
    {
        // update list
    }
    @NonNull
    @Override

    public AdapterExploreLinear.ViewHolderExplorerLinear onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_element_linear,parent,false);
        ViewHolderExplorerLinear viewHolderExplorerLinear = new ViewHolderExplorerLinear(view);
        return viewHolderExplorerLinear;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterExploreLinear.ViewHolderExplorerLinear holder, final int position) {

        List<String> listObjects = listOfExplorePlaceLinear.get(position).getImages();
        holder.sliderLayout.removeAllSliders();
        for (String a : listObjects)
        {
            TextSliderView textSliderView = new TextSliderView(mContext);
            textSliderView.image(a).setScaleType(BaseSliderView.ScaleType.FitCenterCrop).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    goToDetailActivity(position);

                }
            });
            holder.sliderLayout.addSlider(textSliderView);
        }

        holder.sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        holder.sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        holder.sliderLayout.setDuration(5000);

        holder.sliderLayout.setCustomAnimation(new ChildAnimation());
        holder.cardViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              goToDetailActivity(position);

            }
        });
        holder.readMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetailActivity(position);
            }
        });
        holder.btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity,MapsActivity.class).putExtra("typeofview",0).putExtra("latitude",String.valueOf(listOfExplorePlaceLinear.get(position).getLatitude())).putExtra("longtitude",String.valueOf(listOfExplorePlaceLinear.get(position).getLongtitude())).putExtra("nameofplace",listOfExplorePlaceLinear.get(position).getAddress()));
            }
        });
        ExplorePost explorePlaceLinear = listOfExplorePlaceLinear.get(position);
        holder.tv_nameOfUser.setText(explorePlaceLinear.getUsername());
        holder.tv_time.setText(explorePlaceLinear.getCreatedAt());
        holder.tv_address.setText(explorePlaceLinear.getAddress());
        holder.readMoreTextView.setText(explorePlaceLinear.getContent());
        Glide.with(mContext).load(explorePlaceLinear.getAvatar()).into(holder.cv_imageOfUser);


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return listOfExplorePlaceLinear.size();
    }

    public class ViewHolderExplorerLinear extends  RecyclerView.ViewHolder{
        SliderLayout sliderLayout;
        ReadMoreTextView readMoreTextView;
        CardView cardViewPost ;
        ImageView btn_maps;
        TextView tv_nameOfUser,tv_time,tv_address;
        CircleImageView cv_imageOfUser;

        public ViewHolderExplorerLinear(View itemView) {
            super(itemView);
            sliderLayout=(SliderLayout) itemView.findViewById(R.id.slideImage);
            readMoreTextView=(ReadMoreTextView)itemView.findViewById(R.id.description_readmore);
            cardViewPost = (CardView) itemView.findViewById(R.id.cv_post);
            btn_maps = (ImageView)itemView.findViewById(R.id.ib_maps_linear);
            tv_nameOfUser = (TextView) itemView.findViewById(R.id.tv_nameOfUser);
            tv_address = (TextView) itemView.findViewById(R.id.tv_detail_location_post);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            cv_imageOfUser = (CircleImageView)itemView.findViewById(R.id.iv_user);


        }
    }
    private void goToDetailActivity(int position)
    {
        Intent intent = new Intent(mActivity,DetailPostActivity.class);
        DataDetailPost.getInstance().setObjectPostDetail(listOfExplorePlaceLinear.get(position));
        DataDetailPost.getInstance().setTypeOfPost(1);
        mActivity.startActivity(intent);

    }
}
