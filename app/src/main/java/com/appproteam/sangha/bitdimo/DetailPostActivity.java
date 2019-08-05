package com.appproteam.sangha.bitdimo;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appproteam.sangha.bitdimo.Presenter.Objects.CommentsObject;
import com.appproteam.sangha.bitdimo.Presenter.Objects.ExplorePlaceLinear;
import com.appproteam.sangha.bitdimo.Presenter.Objects.UserFake;
import com.appproteam.sangha.bitdimo.Retrofit.ExplorePost;
import com.appproteam.sangha.bitdimo.Singleton.DataDetailPost;
import com.appproteam.sangha.bitdimo.View.Adapter.AdapterExplore;
import com.appproteam.sangha.bitdimo.View.Adapter.AdapterPostDetail;

import java.util.ArrayList;
import java.util.List;

public class DetailPostActivity extends AppCompatActivity {
    Intent mIntent;
    ExplorePost explorePlaceLinear;
    RecyclerView rvPostDetail;
    AdapterPostDetail adapterPostDetail;
    List<CommentsObject> commentsObjectList;
    EditText edt_comment;
    ImageButton imb_sendComment;
    ImageView imv_transition;
    ImageView imv_maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       /* supportPostponeEnterTransition();*/
        initView();
        Bundle extras = getIntent().getExtras();
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          getWindow().getSharedElementEnterTransition().setDuration(100);
            getWindow().getSharedElementReturnTransition().setDuration(100)
                    .setInterpolator(new DecelerateInterpolator());
            if(extras.getString("stringTransition")!=null) {
                {
                    String imageTransitionName = extras.getString("stringTransition");
                    imv_transition.setTransitionName(imageTransitionName);
                    supportStartPostponedEnterTransition();
                }
            }

        }
*/
        if (DataDetailPost.getInstance()!=null)
            explorePlaceLinear=DataDetailPost.getInstance().getExplorePlaceLinear();

        fakeData();
        adapterPostDetail = new AdapterPostDetail(commentsObjectList,getBaseContext(),this,explorePlaceLinear,DataDetailPost.getInstance().getOutStandingPlace());
        rvPostDetail.setLayoutManager(new LinearLayoutManager(this));
        rvPostDetail.setAdapter(adapterPostDetail);

    }

    private void fakeData() {
        List<UserFake> listFakeUser=  new ArrayList<>();
        listFakeUser.add(new UserFake("Hà Xuân Sáng","https://scontent.fdad3-1.fna.fbcdn.net/v/t1.0-9/16105729_956911674445153_4841664560651054894_n.jpg?_nc_cat=106&_nc_ht=scontent.fdad3-1.fna&oh=5188b459f9776dcdd322c726db66cf8a&oe=5C75F7D1"));
        listFakeUser.add(new UserFake("Martin Nguyễn","https://scontent.fdad3-1.fna.fbcdn.net/v/t1.0-9/42294020_1530042427097942_1759509601360805888_n.jpg?_nc_cat=110&_nc_ht=scontent.fdad3-1.fna&oh=0e9db9acc9f221f3dff85c73f8c28efb&oe=5C6E89E5"));
        listFakeUser.add(new UserFake("Tứ Nguyễn","https://scontent.fdad3-1.fna.fbcdn.net/v/t1.0-9/40108995_2060114107583206_3178832246412935168_n.jpg?_nc_cat=102&_nc_ht=scontent.fdad3-1.fna&oh=5a82c646199066d0c5948bbaee7618a7&oe=5C6BDFA0"));
        listFakeUser.add(new UserFake("Hương Giang","https://scontent.fdad3-2.fna.fbcdn.net/v/t1.0-9/44841958_330146804438230_8173558343244185600_n.jpg?_nc_cat=107&_nc_ht=scontent.fdad3-2.fna&oh=6d92e0719fddf7e716e7bc096fd231b9&oe=5C72022D"));
        listFakeUser.add(new UserFake("Minh Cương","https://scontent.fdad3-3.fna.fbcdn.net/v/t1.0-1/43192716_1543444769088782_3429154145993490432_o.jpg?_nc_cat=111&_nc_ht=scontent.fdad3-3.fna&oh=89ddfd061bce12335c57d5446410feea&oe=5CB26F4C"));
        listFakeUser.add(new UserFake("Nguyễn Thị Thu Thảo","https://scontent.fdad3-3.fna.fbcdn.net/v/t1.0-1/c0.107.987.987/45299900_2231077917167471_2420404892397993984_o.jpg?_nc_cat=100&_nc_ht=scontent.fdad3-3.fna&oh=3745a031bf0308b022c16258fc99ad94&oe=5C678A0C"));
        commentsObjectList = new ArrayList<>();

        commentsObjectList.add(new CommentsObject(listFakeUser.get(0).getUrlOfImages(),listFakeUser.get(0).getNameOfUser(),"Cái bài viết này ảnh toàn đẹp không à. Muốn một lần đặt chân tới đây để chụp ảnh quá. "));
        commentsObjectList.add(new CommentsObject(listFakeUser.get(1).getUrlOfImages(),listFakeUser.get(1).getNameOfUser(),"Đây chứa những kí ức khó phai của tôi, thật đáng trân trọng những khoảnh khắc ấy"));
        commentsObjectList.add(new CommentsObject(listFakeUser.get(2).getUrlOfImages(),listFakeUser.get(2).getNameOfUser(),"Hôm nay mình đi tới đây cùng với gia đình, đúng là thiên đường. "));
        commentsObjectList.add(new CommentsObject(listFakeUser.get(3).getUrlOfImages(),listFakeUser.get(3).getNameOfUser(),"Để sắm con máy Nikon để chụp như này đã "));
        commentsObjectList.add(new CommentsObject(listFakeUser.get(4).getUrlOfImages(),listFakeUser.get(4).getNameOfUser(),"Ước gì có người yêu để mình cùng nắm tay đi chơi những nơi này"));
        commentsObjectList.add(new CommentsObject(listFakeUser.get(5).getUrlOfImages(),listFakeUser.get(5).getNameOfUser(),"Có một người bạn giới thiệu chỗ này đến tôi và thật bất ngờ, tôi cực yêu quý nơi này. Cảm ơn người bạn của tôi <3"));



    }

    private void initView() {
        imv_transition = (ImageView)findViewById(R.id.imv_transition);
        rvPostDetail = (RecyclerView)findViewById(R.id.rv_post_detail);
        edt_comment = (EditText)findViewById(R.id.content_comment);
        imb_sendComment  = (ImageButton) findViewById(R.id.sendComment);





        imb_sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentsObject currentComment = new CommentsObject(MainActivity.listUser.get(16).getUrlOfImages(),"Hà Xuân Sáng",edt_comment.getText().toString());
                commentsObjectList.add(currentComment);
                adapterPostDetail.notifyDataSetChanged();
                edt_comment.setText("");
                hideKeyBoard();
                scrollSmooth();
            }
        });


    }
    private void hideKeyBoard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void scrollSmooth() {
        if(adapterPostDetail.getItemCount()>0)
            rvPostDetail.smoothScrollToPosition(adapterPostDetail.getItemCount()-1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case  android.R.id.home:
                this.finish();
                break;
        }
        return  true;
    }
}
