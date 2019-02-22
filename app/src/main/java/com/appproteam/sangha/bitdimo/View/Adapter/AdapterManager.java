package com.appproteam.sangha.bitdimo.View.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appproteam.sangha.bitdimo.DetailPostActivity;
import com.appproteam.sangha.bitdimo.MainActivity;
import com.appproteam.sangha.bitdimo.Presenter.Objects.ExplorePlace;
import com.appproteam.sangha.bitdimo.Presenter.Objects.ExplorePlaceLinear;
import com.appproteam.sangha.bitdimo.R;
import com.appproteam.sangha.bitdimo.Retrofit.ExplorePost;
import com.appproteam.sangha.bitdimo.Retrofit.ObjectRetrofit.CurrentUser;
import com.appproteam.sangha.bitdimo.Singleton.CurrentDataUser;
import com.appproteam.sangha.bitdimo.Singleton.DataDetailPost;
import com.appproteam.sangha.bitdimo.Singleton.DataGalery;
import com.appproteam.sangha.bitdimo.View.CallBack.ChangeUserInformation;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.http.Url;

public class AdapterManager extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<ExplorePost> listOfExplorePlaceLinear;
    Activity mActivity;
    private final static int TYPE_HEADER=1;
    private final static int TYPE_RECYCLERVIEW=2;
    ChangeUserInformation changeUserInformation;

    public void registerCallback(ChangeUserInformation changeUserInformation)
    {
        this.changeUserInformation = changeUserInformation;
    }


    public AdapterManager(Context mContext, List<ExplorePost> listOfExplorePlaceLinear, Activity mActivity) {
        this.mContext = mContext;
        this.listOfExplorePlaceLinear = listOfExplorePlaceLinear;
        this.mActivity = mActivity;

    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return TYPE_HEADER;
        else return  TYPE_RECYCLERVIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType)
        {
            case TYPE_HEADER:
                 view = LayoutInflater.from(parent.getContext()).inflate(R.layout.headerview_manage,parent,false);
                return new ViewholderHeader(view);

            case TYPE_RECYCLERVIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_element_linear, parent, false);
                return new ViewHolderManage(view);

        }
        return  null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType())
        {
            case TYPE_RECYCLERVIEW:
                final ExplorePost explorePlaceLinear = listOfExplorePlaceLinear.get(position-1);
                final ViewHolderManage viewHolderManage =(ViewHolderManage)holder;
                List<String> listObjects = listOfExplorePlaceLinear.get(position-1).getImages();
                viewHolderManage.sliderLayout.removeAllSliders();
                for (String a : listObjects)
                {
                    TextSliderView textSliderView = new TextSliderView(mContext);
                    textSliderView.image(a).setScaleType(BaseSliderView.ScaleType.FitCenterCrop).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {

                            goToDetailActivity(position);
                        }
                    });

                    viewHolderManage.sliderLayout.addSlider(textSliderView);
                }
                viewHolderManage.sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                viewHolderManage.sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                viewHolderManage.sliderLayout.setDuration(5000);
                viewHolderManage.sliderLayout.setCustomAnimation(new ChildAnimation());
                Glide.with(mContext).load(CurrentDataUser.getInstance().getCurrentUser().getAvatar()).into(viewHolderManage.cv_ImageOfUser);
                viewHolderManage.tv_Time.setText(explorePlaceLinear.getCreatedAt());
                viewHolderManage.tv_Address.setText(explorePlaceLinear.getAddress());
                viewHolderManage.readMoreTextView.setText(explorePlaceLinear.getContent());
                viewHolderManage.tv_countOfComment.setText(String.valueOf(explorePlaceLinear.getNumberOfComment()));
                viewHolderManage.tv_countOfLike.setText(String.valueOf(explorePlaceLinear.getNumberOfLike()));
                viewHolderManage.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToDetailActivity(position);
                    }
                });
                viewHolderManage.readMoreTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToDetailActivity(position);
                    }
                });
               break;
            case TYPE_HEADER:
                final ViewholderHeader viewholderHeader  = (ViewholderHeader)holder;
                viewholderHeader.civ_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       if(changeUserInformation!=null) changeUserInformation.ChangeImageUser(viewholderHeader.civ_user);
                    }
                });
                viewholderHeader.btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(changeUserInformation!=null) changeUserInformation.EditUser();
                    }
                });
                if(CurrentDataUser.getInstance().getCurrentUser()!=null && !"".equals(CurrentDataUser.getInstance().getCurrentUser().getAvatar()))
                Glide.with(mContext).load(CurrentDataUser.getInstance().getCurrentUser().getAvatar()).into(viewholderHeader.civ_user);

        }

    }



    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return listOfExplorePlaceLinear.size()+1;
    }

    public class ViewHolderManage extends  RecyclerView.ViewHolder{
        SliderLayout sliderLayout;
        ReadMoreTextView readMoreTextView;
        CircleImageView cv_ImageOfUser;
        TextView tv_Address,tv_Time,tv_countOfLike,tv_countOfComment;
        CardView cardView;
        public ViewHolderManage(View itemView) {
            super(itemView);
            sliderLayout = (SliderLayout) itemView.findViewById(R.id.slideImage);
            readMoreTextView = (ReadMoreTextView) itemView.findViewById(R.id.description_readmore);
            tv_Address = (TextView) itemView.findViewById(R.id.tv_detail_location_post);
            tv_Time = (TextView) itemView.findViewById(R.id.tv_time);
            cv_ImageOfUser = (CircleImageView)itemView.findViewById(R.id.iv_user);
            tv_countOfComment=(TextView)itemView.findViewById(R.id.tv_countsOfComments_linear);
            tv_countOfLike=(TextView)itemView.findViewById(R.id.tv_countsOfLikes_linear);
            cardView = (CardView)itemView.findViewById(R.id.cv_post);
        }
    }
    public class ViewholderHeader extends  RecyclerView.ViewHolder{
        CircleImageView civ_user;
        Button btn_edit;
        public ViewholderHeader(View itemView) {
            super(itemView);
            civ_user=(CircleImageView) itemView.findViewById(R.id.ci_user_manage);
            btn_edit=(Button)itemView.findViewById(R.id.btn_edit_manage);
        }
    }
    private void goToDetailActivity(int position)
    {
        Intent intent = new Intent(mActivity,DetailPostActivity.class);
        DataDetailPost.getInstance().setObjectPostDetail(listOfExplorePlaceLinear.get(position-1));
        DataDetailPost.getInstance().setTypeOfPost(1);
        mActivity.startActivity(intent);

    }
    public void updateImageUser(String url)
    {
        CurrentDataUser.getInstance().getCurrentUser().setAvatar(url);
        notifyDataSetChanged();
    }
}