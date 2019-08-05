package com.appproteam.sangha.bitdimo.View.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appproteam.sangha.bitdimo.MapsActivity;
import com.appproteam.sangha.bitdimo.Presenter.Objects.CommentsObject;
import com.appproteam.sangha.bitdimo.Presenter.Objects.ExplorePlaceLinear;
import com.appproteam.sangha.bitdimo.Presenter.Objects.OutStandingPlace;
import com.appproteam.sangha.bitdimo.R;
import com.appproteam.sangha.bitdimo.Retrofit.ExplorePost;
import com.appproteam.sangha.bitdimo.Singleton.DataDetailPost;
import com.appproteam.sangha.bitdimo.Singleton.DataPostsRetrieve;
import com.appproteam.sangha.bitdimo.Singleton.OutstandingPost;
import com.appproteam.sangha.bitdimo.ZoomImageActivity;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.borjabravo.readmoretextview.ReadMoreTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPostDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_OF_POST=1;
    private final int TYPE_OF_COMMENT=2;
    private final int TYPE_OF_OUTSTANDING=0;
    public List<CommentsObject> listComments;
    public Context mContext;
    public Activity mActivity;
    public ExplorePost explorePlaceLinear;
    public OutstandingPost outStandingPlace;
    public AdapterPostDetail(List<CommentsObject> listComments, Context mContext, Activity mActivity, ExplorePost explorePlaceLinear,OutstandingPost outStandingPlace) {
        this.listComments = listComments;
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.explorePlaceLinear=explorePlaceLinear;
        this.outStandingPlace = outStandingPlace;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType)
        {
            case TYPE_OF_POST:
                return new ViewHolderPostDetail(LayoutInflater.from(mContext).inflate(R.layout.explore_element_linear,parent,false));
            case TYPE_OF_COMMENT:
                return  new ViewholderComments(LayoutInflater.from(mContext).inflate(R.layout.element_comments,parent,false));
            case TYPE_OF_OUTSTANDING:
                return  new ViewHolderOutStanding(LayoutInflater.from(mContext).inflate(R.layout.outstanding_element,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType())
            {
                case  TYPE_OF_POST:
                    final ViewHolderPostDetail viewHolderPostDetail  = (ViewHolderPostDetail)holder;
                    viewHolderPostDetail.sliderLayout.removeAllSliders();
                    Glide.with(mContext).load(explorePlaceLinear.getAvatar()).into(viewHolderPostDetail.cv_User);
                    viewHolderPostDetail.tv_NameOfUser.setText(explorePlaceLinear.getUsername());
                    viewHolderPostDetail.tv_TimeOfPost.setText(explorePlaceLinear.getCreatedAt());
                    viewHolderPostDetail.tv_address.setText(explorePlaceLinear.getAddress());

                    for (String a : explorePlaceLinear.getImages())
                    {
                        TextSliderView textSliderView = new TextSliderView(mContext);
                        textSliderView.image(a).setScaleType(BaseSliderView.ScaleType.FitCenterCrop).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                /*Dialog dialog = new Dialog(mActivity);
                                dialog.setContentView(R.layout.zoomimage);
                                dialog.setCanceledOnTouchOutside(true);
                                ImageView imageView = (ImageView) dialog.findViewById(R.id.imv_zoom);
                                Glide.with(mContext).load(slider.getUrl()).fitCenter().into(imageView);
                                imageView.setOnTouchListener(new ImageMatrixTouchHandler(mContext));
                                dialog.show();*/

                                Intent intent  = new Intent(mActivity,ZoomImageActivity.class);
                                intent.putExtra("urlImage",slider.getUrl());
                                mActivity.startActivity(intent);
                            }
                        });
                        viewHolderPostDetail.sliderLayout.addSlider(textSliderView);
                    }
                    viewHolderPostDetail.sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    viewHolderPostDetail.sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    viewHolderPostDetail.sliderLayout.setDuration(5000);
                    viewHolderPostDetail.sliderLayout.setCustomAnimation(new ChildAnimation());
                    viewHolderPostDetail.readMoreTextView.setText(explorePlaceLinear.getContent());
                    viewHolderPostDetail.tv_countOfLikes.setText(String.valueOf(explorePlaceLinear.getNumberOfLike()));
                    viewHolderPostDetail.tv_countOfComments.setText(String.valueOf(explorePlaceLinear.getNumberOfComment()));

                    viewHolderPostDetail.imv_Like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                viewHolderPostDetail.imv_Like.setBackgroundResource(R.drawable.ic_likered);
                            //DataPostsRetrieve.getInstance().getListExplore().get(position).setLike(true);

                        }
                    });
                    viewHolderPostDetail.imv_Comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    viewHolderPostDetail.ib_maps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mActivity.startActivity(new Intent(mActivity,MapsActivity.class).putExtra("typeofview",0).putExtra("latitude",String.valueOf(explorePlaceLinear.getLatitude())).putExtra("longtitude",String.valueOf(explorePlaceLinear.getLongtitude())).putExtra("nameofplace",explorePlaceLinear.getAddress()));
                        }
                    });
                    break;
                case TYPE_OF_OUTSTANDING:
                    final ViewHolderOutStanding viewHolderOutStanding = (ViewHolderOutStanding) holder;
                    viewHolderOutStanding.sliderLayout.removeAllSliders();
                    viewHolderOutStanding.nameOfPlace.setText(outStandingPlace.getAreaname());
                    viewHolderOutStanding.Address.setText(outStandingPlace.getAddress());
                    for (String a : outStandingPlace.getImages())
                    {
                        TextSliderView textSliderView = new TextSliderView(mContext);
                        textSliderView.image(a).setScaleType(BaseSliderView.ScaleType.FitCenterCrop).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {

                            /*    Dialog dialog = new Dialog(mActivity);
                                dialog.setContentView(R.layout.zoomimage);
                                dialog.setCanceledOnTouchOutside(true);
                                ImageView imageView = (ImageView) dialog.findViewById(R.id.imv_zoom);
                                Glide.with(mContext).load(slider.getUrl()).fitCenter().into(imageView);
                                imageView.setOnTouchListener(new ImageMatrixTouchHandler(mContext));
                                dialog.show();*/

                            }
                        });
                        viewHolderOutStanding.sliderLayout.addSlider(textSliderView);
                    }
                   viewHolderOutStanding.sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    viewHolderOutStanding.sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    viewHolderOutStanding.sliderLayout.setDuration(5000);
                    viewHolderOutStanding.sliderLayout.setCustomAnimation(new ChildAnimation());
                    viewHolderOutStanding.ib_maps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mActivity.startActivity(new Intent(mActivity,MapsActivity.class).putExtra("typeofview",0).putExtra("latitude",String.valueOf(outStandingPlace.getLatitude())).putExtra("longtitude",String.valueOf(outStandingPlace.getLongtitude())).putExtra("nameofplace",outStandingPlace.getPlacename()));
                        }
                    });
                    break;
                case  TYPE_OF_COMMENT:
                    final  CommentsObject commentsObject = listComments.get(position-1);
                    final ViewholderComments viewholderComments = (ViewholderComments)holder;
                    Glide.with(mContext).load(commentsObject.getUrlImageUserComment()).into(viewholderComments.civ_user);
                    viewholderComments.tv_nameof_user_comment.setText(commentsObject.getNameOfUserComment());
                    viewholderComments.tv_content_comment.setText(commentsObject.getContentOfComment());
                    break;

            }
    }

    @Override
    public int getItemCount() {
        return listComments.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0) {
            if (DataDetailPost.getInstance().getTypeOfPost() == 0)
                return TYPE_OF_OUTSTANDING;
                     else return TYPE_OF_POST;
        }
         else return TYPE_OF_COMMENT;

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    public class ViewHolderPostDetail extends  RecyclerView.ViewHolder{
        CircleImageView cv_User;
        TextView tv_NameOfUser;
        TextView tv_TimeOfPost;
        TextView tv_address;
        SliderLayout sliderLayout;
        ReadMoreTextView readMoreTextView;
        ImageView imv_Like,imv_Comment;
        Button btn_save;
        TextView tv_countOfLikes,tv_countOfComments;
        ImageButton ib_maps;
        public ViewHolderPostDetail(View itemView) {
            super(itemView);
            cv_User=(CircleImageView)itemView.findViewById(R.id.iv_user);
            tv_NameOfUser=(TextView)itemView.findViewById(R.id.tv_nameOfUser);
            tv_TimeOfPost=(TextView)itemView.findViewById(R.id.tv_time);
            sliderLayout=(SliderLayout) itemView.findViewById(R.id.slideImage);
            readMoreTextView=(ReadMoreTextView)itemView.findViewById(R.id.description_readmore);
            imv_Like= (ImageView)itemView.findViewById(R.id.iv_countsOfLikes_linear);
            imv_Comment=(ImageView)itemView.findViewById(R.id.imv_OfComments_linear);
            btn_save= (Button)itemView.findViewById(R.id.ib_bookmark_linear);
            tv_countOfLikes=(TextView)itemView.findViewById(R.id.tv_countsOfLikes_linear);
            tv_countOfComments=(TextView)itemView.findViewById(R.id.tv_countsOfComments_linear);
            ib_maps=(ImageButton)itemView.findViewById(R.id.ib_maps_linear);
            tv_address = (TextView)itemView.findViewById(R.id.tv_detail_location_post);


        }
    }
    public class ViewholderComments extends  RecyclerView.ViewHolder{
        CircleImageView civ_user;
        TextView tv_nameof_user_comment;
        TextView tv_content_comment;
        public ViewholderComments(View itemView) {
            super(itemView);
            civ_user=(CircleImageView) itemView.findViewById(R.id.iv_user_comments);
            tv_nameof_user_comment = (TextView) itemView.findViewById(R.id.tv_nameOfUser_comments);
            tv_content_comment = (TextView) itemView.findViewById(R.id.tv_content_comments);

        }
    }
    public class ViewHolderOutStanding extends RecyclerView.ViewHolder{
        TextView nameOfPlace,Address,countOfComments,countOfLikes;
        SliderLayout sliderLayout;
        ImageButton ib_maps;
        ImageView ib_like;



        public ViewHolderOutStanding(View itemView) {
            super(itemView);
            nameOfPlace = (TextView)itemView.findViewById(R.id.nameOfPlace);
            Address = (TextView)itemView.findViewById(R.id.addressOfPlace);
            countOfComments = (TextView) itemView.findViewById(R.id.tv_countsOfComments);
            countOfLikes = (TextView)itemView.findViewById(R.id.tv_countsOfLikes);
            sliderLayout = (SliderLayout)itemView.findViewById(R.id.iv_pictureOfPlace);
            ib_maps=(ImageButton)itemView.findViewById(R.id.imb_location);
            ib_like= (ImageView)itemView.findViewById(R.id.iv_countsOfLikes);



        }

    }
}
