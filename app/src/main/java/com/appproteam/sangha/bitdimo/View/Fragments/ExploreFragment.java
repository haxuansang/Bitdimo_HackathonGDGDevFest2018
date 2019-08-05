package com.appproteam.sangha.bitdimo.View.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appproteam.sangha.bitdimo.LoginActivity;
import com.appproteam.sangha.bitdimo.R;
import com.appproteam.sangha.bitdimo.Retrofit.ExplorePost;
import com.appproteam.sangha.bitdimo.Singleton.DataPostsRetrieve;
import com.appproteam.sangha.bitdimo.View.Adapter.AdapterExplore;
import com.appproteam.sangha.bitdimo.View.Adapter.AdapterExploreLinear;
import com.appproteam.sangha.bitdimo.View.Adapter.AdapterFilter;
import com.appproteam.sangha.bitdimo.View.CallBack.ChangeDataExplore;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
    RecyclerView rv_explore;
    RecyclerView rv_filter;
    public static AdapterExplore adapterExplore;
    RecyclerView rv_explore_linear;
    Button btnGrid,btnLinear,btnFilter;
    List<ExplorePost> listExploreLinear;
    AdapterFilter adapterFilter;
    public static AdapterExploreLinear adapterExploreLinear;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ExploreFragment() {
        // Required empty public constructor
    }


    public static ExploreFragment newInstance(/*String param1, String param2*/) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
    /*    args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_explore, container, false);
        getDataOfExplorePlaces();

        return view;
    }
    private void getDataOfExplorePlaces() {
        LoginActivity.mSOService.getExplorePosts("filter=number_of_like").enqueue(new Callback<List<ExplorePost>>() {
            @Override
            public void onResponse(Call<List<ExplorePost>> call, Response<List<ExplorePost>> response) {
                if(response.isSuccessful())
                {
                    DataPostsRetrieve.getInstance().setListExplore(response.body());
                    createView();

                }
            }

            @Override
            public void onFailure(Call<List<ExplorePost>> call, Throwable t) {

            }
        });
    }

    private void createView() {
        btnLinear = (Button)view.findViewById(R.id.btn_linearviewexplore);
        btnGrid = (Button)view.findViewById(R.id.btn_gridviewexplore);
        btnFilter = (Button)view.findViewById(R.id.btn_explore_filter);
        btnFilter.setOnClickListener(this);
        rv_explore=(RecyclerView)view.findViewById(R.id.rv_explore_gridview);
        rv_filter= (RecyclerView)view.findViewById(R.id.rv_filter);
        btnLinear.setOnClickListener(this);
        btnGrid.setOnClickListener(this);
        //listExploreLinear= DataPostsRetrieve.getInstance().getListExplore();

        //fakeDataForLinear();

        adapterExplore = new AdapterExplore(getContext(),DataPostsRetrieve.getInstance().getListExplore(),getActivity());
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv_explore.setLayoutManager(gridLayoutManager);
        rv_explore.setAdapter(adapterExplore);
        //
        adapterExploreLinear = new AdapterExploreLinear(getContext(),DataPostsRetrieve.getInstance().getListExplore(),getActivity());
        rv_explore_linear= (RecyclerView) view.findViewById(R.id.rv_explore_linear);
        rv_explore_linear.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_explore_linear.setAdapter(adapterExploreLinear);
        //
        adapterFilter = new AdapterFilter(getContext(),getActivity());
        adapterFilter.registerCallback(new ChangeDataExplore() {
            @Override
            public void followTime() {
                //adapterExplore.updateList();
                //adapterExploreLinear.updateList();
            }

            @Override
            public void followRating() {
                //adapterExplore.updateList();
                //adapterExploreLinear.updateList();
            }
        });
        rv_filter.setHasFixedSize(true);
        rv_filter.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_filter.setAdapter(adapterFilter);


    }

   /* private void fakeDataForLinear() {
      listExploreLinear = new ArrayList<>();

      List<String> listOfPlaces = new ArrayList<>();
      //0
      listOfPlaces.add("https://yeudanang.biz/wp-content/uploads/2018/11/45041668_481346935706730_7229797798877593600_o.jpg");
      listOfPlaces.add("https://yeudanang.biz/wp-content/uploads/2018/11/45152551_481346865706737_1362042573190856704_n.jpg");
      listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(0).getNameOfUser()
              ,"18/11/2018 15h00","Mãi luôn tươi trẻ khi ở Đà Nẵng"
              ,MainActivity.listUser.get(0).getUrlOfImages()
              ,100,
              6,
              listOfPlaces,new MapsObject(16.1197212,108.2691571),"Sơn Trà, Đà Nẵng"));


      //1
        listOfPlaces= new ArrayList<>();
        listOfPlaces.add("http://dulichdanang.xyz/wp-content/uploads/2016/10/cau-dn-7-Copy.jpg");
        listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(1).getNameOfUser()
                ,"18/11/2018 15h00","Hội thanh xuân của tôi, những tri kỉ mà tôi may mắn có được!"
                ,MainActivity.listUser.get(1).getUrlOfImages()
                ,100,
                6,
                listOfPlaces,new MapsObject(16.051211, 108.229903),"Cầu Nguyễn Văn Trỗi, Hải Châu"));

        //2
        listOfPlaces= new ArrayList<>();
        listOfPlaces.add("https://yeudanang.biz/wp-content/uploads/2018/02/3-2.jpg");
        listOfPlaces.add("https://yeudanang.biz/wp-content/uploads/2018/02/5-2.jpg");
        listOfPlaces.add("https://yeudanang.biz/wp-content/uploads/2018/02/2-2.jpg");
        listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(2).getNameOfUser()
                ,"18/11/2018 15h00","Mùa đông năm ấy.."
                ,MainActivity.listUser.get(2).getUrlOfImages()
                ,120,
                6,
                listOfPlaces,new MapsObject(15.966689, 108.284588),"Phim trường Cocobay, Hòa Hải, Ngũ Hành Sơn"));

        //3
        listOfPlaces= new ArrayList<>();
        listOfPlaces.add("https://www.vntrip.vn/cam-nang/wp-content/uploads/2018/08/ghenh-da-nam-o-9-e1534498635906.jpg");
        listOfPlaces.add("https://cdn3.ivivu.com/2016/10/ghenh-bang-ivivu-4.png");

        listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(3).getNameOfUser()
                ,"18/11/2018 15h00","Sóng bắt đầu từ gió.. \n Gió bắt đầu từ đâu?"
                ,MainActivity.listUser.get(3).getUrlOfImages()
                ,120,
                6,
                listOfPlaces,new MapsObject(16.117447, 108.132251),"Ghềnh đá Nam Ô, Hòa Hiệp Nam, Liên Chiểu"));


        //4
        listOfPlaces= new ArrayList<>();
        listOfPlaces.add("https://yeudanang.biz/wp-content/uploads/2018/10/superturtle53.jpg");
        listOfPlaces.add("https://media.foody.vn/images/img_8224.jpg");
        listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(4).getNameOfUser()
                ,"11/11/2018 7h00","Một chuyến đi đáng nhớ, yêu quá Tây Giang ơiiii"
                ,MainActivity.listUser.get(4).getUrlOfImages()
                ,120,
                6,
                listOfPlaces,new MapsObject(15.884697, 107.489013),"Tây Giang, Quảng Nam"));

        //5
        listOfPlaces= new ArrayList<>();
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/ba_naDinh_Huu_Tri2.jpg");
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/ba_na_trishtrinhspams.jpg");

        listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(5).getNameOfUser()
                ,"12/11/2018 15h00","Lạc ở chốn tiên cảnh.."
                ,MainActivity.listUser.get(5).getUrlOfImages()
                ,120,
                6,
                listOfPlaces,new MapsObject(15.997712, 107.988033),"Bà Nà Hills, Hòa Vang"));

        //6
        listOfPlaces= new ArrayList<>();
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/ba_na_Pham_Mai_Linh.jpg");
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/ba_na.jpg");


        listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(6).getNameOfUser()
                ,"13/11/2018 14h00","Check in khu vườn bí mật :))"
                ,MainActivity.listUser.get(6).getUrlOfImages()
                ,120,
                6,
                listOfPlaces,new MapsObject(15.997712, 107.988033),"Bà Nà Hills, Hòa Vang"));

        //7

        listOfPlaces= new ArrayList<>();
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/ba_na_cau_vangchch_chloe.jpg");
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/ba_na_cau_vangjenrin98.jpg");
        listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(7).getNameOfUser()
                ,"17/11/2018 21h38","Bà Nà đẹp thật chứ huhu"
                ,MainActivity.listUser.get(7).getUrlOfImages()
                ,120,
                6,
                listOfPlaces,new MapsObject(15.997712, 107.988033),"Bà Nà Hills, Hòa Vang"));

        //8
        listOfPlaces= new ArrayList<>();
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/cau_tinh_yeuThuy_Ngoc_Nguyen.jpg");
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/cau_tinh_yeuHanh_Sun.jpg");
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/cau_tinh_yeuPham_Mai_Linh_1.jpg");
        listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(8).getNameOfUser()
                ,"13/11/2018 20h38","Đi cầu Tình Yêu với các “tình yêu” :)))"
                ,MainActivity.listUser.get(8).getUrlOfImages()
                ,120,
                6,
                listOfPlaces,new MapsObject(16.063245, 108.229619),"Cầu Tình Yêu, Trần Hưng Đạo, Sơn Trà"));

        //9

        listOfPlaces= new ArrayList<>();
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/cong_vien_bien_dong__haphuoong.jpg");
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/cong_vien_bien_dong__nanaaaaa_1.jpg");
        listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(9).getNameOfUser()
                ,"15/11/2018 20h38","Biển xanh, nắng vàng, và...bồ câu "
                ,MainActivity.listUser.get(9).getUrlOfImages()
                ,183,
                6,
                listOfPlaces,new MapsObject(16.078208, 108.241900),"Công viên Biển Đông, Võ Nguyên Giáp, Phước Mỹ"));

        //10
        listOfPlaces = new ArrayList<>();
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/deo_hai_van__hokhanhquynh_.jpg");
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/deo_hai_van__hanhminhon.jpg");
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/deo_hai_van__dtdquynh__.jpg");
        listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(10).getNameOfUser()
                ,"14/11/2018 18h54","Không quan trọng bạn đi đến đầu, mà là đi với ai!"
                ,MainActivity.listUser.get(10).getUrlOfImages()
                ,190,
                6,
                listOfPlaces,new MapsObject(16.152641, 108.132775),"Cây cô đơn, đèo Hải Vân, Hòa Khánh Nam"));


        //11
        listOfPlaces = new ArrayList<>();
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/deo_hai_van__hongphat1991.jpg");
        listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(11).getNameOfUser()
                ,"14/11/2018 18h54","On the top of Da Nang city"
                ,MainActivity.listUser.get(11).getUrlOfImages()
                ,150,
                6,
                listOfPlaces,new MapsObject(16.152641, 108.132775),"Cây cô đơn, đèo Hải Vân, Hòa Khánh Nam"));

        //12
        listOfPlaces = new ArrayList<>();
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/son_tra_Dong_Den.jpg");
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/son_tra_xuong_cang_tien_sa_Pham_mai_Linh.jpg");
        listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(12).getNameOfUser()
                ,"14/11/2018 18h54","Ở Đà Nẵng mà không tìm đến đây thì hơi phí đấy nhé"
                ,MainActivity.listUser.get(12).getUrlOfImages()
                ,167,
                6,
                listOfPlaces,new MapsObject(16.116094, 108.273378),"Bán đảo Sơn Trà"));

        //13
        listOfPlaces = new ArrayList<>();
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/thuan_phuoc_Field_meoowchann.jpg");
        listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(13).getNameOfUser()
                ,"14/11/2018 19h24","Phía sau một cô gái :D"
                ,MainActivity.listUser.get(13).getUrlOfImages()
                ,121,
                6,
                listOfPlaces,new MapsObject(16.073363, 108.161287),"Nguyễn Sinh Sắc, Liên Chiểu"));

        //14
        listOfPlaces = new ArrayList<>();
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/thuan_phuoc_Field_Dinh_Huu_Tri5.jpg");
        listOfPlaces.add("https://znews-photo.zadn.vn/w1/Uploaded/mdf_eioxrd/2018_07_24/thuan_phuoc_Field_Dinh_Huu_Tri2.jpg");
        listExploreLinear.add(new ExplorePlaceLinear(MainActivity.listUser.get(14).getNameOfUser()
                ,"15/11/2018 20h24","Phim trường Island,  Nại Hiên Đông, Sơn Trà"
                ,MainActivity.listUser.get(14).getUrlOfImages()
                ,119,
                6,
                listOfPlaces,new MapsObject(16.101409, 108.224424),"Phim trường Island,  Nại Hiên Đông, Sơn Trà"));

        adapterExploreLinear = new AdapterExploreLinear(getContext(),listExploreLinear,getActivity());
        rv_explore_linear= (RecyclerView) view.findViewById(R.id.rv_explore_linear);
        rv_explore_linear.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_explore_linear.setAdapter(adapterExploreLinear);
    }
*/
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_gridviewexplore:
                if(rv_explore.getVisibility()!=View.VISIBLE)
                rv_explore.setVisibility(View.VISIBLE);
                rv_explore_linear.setVisibility(View.INVISIBLE);
                rv_filter.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_linearviewexplore:
                if(rv_explore_linear.getVisibility()!=View.VISIBLE)
                    rv_explore_linear.setVisibility(View.VISIBLE);
                rv_explore.setVisibility(View.INVISIBLE);
                rv_filter.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_explore_filter:
                rv_explore.setVisibility(View.INVISIBLE);
                rv_explore_linear.setVisibility(View.INVISIBLE);
                rv_filter.setVisibility(View.VISIBLE);
                break;

        }
    }
}
