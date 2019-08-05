package com.appproteam.sangha.bitdimo.View.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.appproteam.sangha.bitdimo.DetailPostActivity;
import com.appproteam.sangha.bitdimo.MainActivity;
import com.appproteam.sangha.bitdimo.MapsActivity;
import com.appproteam.sangha.bitdimo.Presenter.Objects.OutStandingPlace;
import com.appproteam.sangha.bitdimo.R;
import com.appproteam.sangha.bitdimo.Singleton.DataDetailPost;
import com.appproteam.sangha.bitdimo.Singleton.OutstandingPost;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.List;

public class AdapterOutStanding extends RecyclerView.Adapter<AdapterOutStanding.ViewHolderOutStanding> {
    List<OutstandingPost> listObjectsPlace;
    Context mContext;
    Activity mActivity;
    List<String> listObjects = new ArrayList<>();
    public AdapterOutStanding(Activity mActivity,Context mContext, List<OutstandingPost> listObjectsPlace)
    {
        this.mContext=mContext;
        this.listObjectsPlace=listObjectsPlace;
        this.mActivity = mActivity;
    }
    @NonNull
    @Override
    public AdapterOutStanding.ViewHolderOutStanding onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outstanding_element,parent,false);
        return new ViewHolderOutStanding(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOutStanding.ViewHolderOutStanding holder,final int position) {
           final OutstandingPost outStandingPlace= listObjectsPlace.get(position);
           holder.sliderLayout.removeAllSliders();
           holder.nameOfPlace.setText(outStandingPlace.getPlacename());
           holder.addressOfPlace.setText("Địa điểm: "+outStandingPlace.getAddress());
           holder.countOfLikes.setText(String.valueOf(outStandingPlace.getNumberOfLike().toString()));
           holder.countOfComments.setText(String.valueOf(outStandingPlace.getNumberOfComment().toString()));
           holder.btn_maps.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  mActivity.startActivity(new Intent(mActivity,MapsActivity.class).putExtra("typeofview",0).putExtra("latitude",outStandingPlace.getLatitude()).putExtra("longtitude",outStandingPlace.getLongtitude()).putExtra("nameofplace",outStandingPlace.getAreaname()));
               }
           });
           this.listObjects = outStandingPlace.getImages();

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
          holder.cardView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  goToDetailActivity(position);
              }
          });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return listObjectsPlace.size();
    }

    public class ViewHolderOutStanding extends RecyclerView.ViewHolder {
        TextView nameOfPlace,addressOfPlace,countOfLikes,countOfComments;
        ImageButton btn_maps;
        SliderLayout sliderLayout;
        CardView cardView;

        public ViewHolderOutStanding(View itemView) {
            super(itemView);
            nameOfPlace = itemView.findViewById(R.id.nameOfPlace);
            addressOfPlace = itemView.findViewById(R.id.addressOfPlace);
            countOfLikes = itemView.findViewById(R.id.tv_countsOfLikes);
            countOfComments = itemView.findViewById(R.id.tv_countsOfComments);
            btn_maps=(ImageButton)itemView.findViewById(R.id.imb_location);
            sliderLayout = (SliderLayout)itemView.findViewById(R.id.iv_pictureOfPlace);
            cardView = (CardView)itemView.findViewById(R.id.cv_outstanding);


        }
    }
    private void goToDetailActivity(int position)
    {
        Intent intent = new Intent(mActivity,DetailPostActivity.class);
        DataDetailPost.getInstance().setObjectOutStanding(listObjectsPlace.get(position));
        DataDetailPost.getInstance().setTypeOfPost(0);
        mActivity.startActivity(intent);

    }
}
