package com.appproteam.sangha.bitdimo.View.Fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appproteam.sangha.bitdimo.LoginActivity;
import com.appproteam.sangha.bitdimo.MainActivity;
import com.appproteam.sangha.bitdimo.Presenter.Objects.MapsObject;
import com.appproteam.sangha.bitdimo.Presenter.Objects.OutStandingPlace;
import com.appproteam.sangha.bitdimo.R;
import com.appproteam.sangha.bitdimo.Retrofit.ExplorePost;
import com.appproteam.sangha.bitdimo.Singleton.DataPostsRetrieve;
import com.appproteam.sangha.bitdimo.Singleton.OutstandingPost;
import com.appproteam.sangha.bitdimo.View.Adapter.AdapterOutStanding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OutStandingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutStandingFragment extends Fragment {
    ProgressBar progressBar;
    RecyclerView recyclerView;
    View view;
    List<OutstandingPost> listOfOutstandingPlaces= new ArrayList<>();
    ArrayList<String> list[] = new ArrayList[15];
    public  int count=0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public OutStandingFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OutStandingFragment newInstance(/*String param1, String param2*/) {
        OutStandingFragment fragment = new OutStandingFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
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

    private void getDataOfOutStandingPlaces() {
        LoginActivity.mSOService.getOutstandingPosts("filter=number_of_like").enqueue(new Callback<List<OutstandingPost>>() {
            @Override
            public void onResponse(Call<List<OutstandingPost>> call, Response<List<OutstandingPost>> response) {
                if(response.isSuccessful())
                {


                    DataPostsRetrieve.getInstance().setListOutstanding(response.body());
                    createView();

                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<OutstandingPost>> call, Throwable t) {

            }
        });
    }

    private void checkCountRetrieve(int count) {
        if(count==2) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_out_standing, container, false);
        //fakeData();
        progressBar = (ProgressBar)view.findViewById(R.id.progressbar_loadingdata);

        getDataOfOutStandingPlaces();



        return view;
    }

  /*  private void fakeData() {
        listOfOutstandingPlaces=  new ArrayList<>();
        for (int i =0; i<list.length ; i++)
        list[i] = new ArrayList<>();

        // Bà Nà



        list[2].add("https://hodadi.s3.amazonaws.com/production/blogs/pictures/000/000/033/original/Ba-Na-Hills-Da-Nang-nam-o-dau.jpg?1502082828");
        list[2].add("https://media1.nguoiduatin.vn/media/vuong-thi-thao/2018/07/27/cau-vang-sun-world-ba-na-hills-13.jpg");
        list[2].add("http://anbinhtravel.com/uploaded/Goc-du-lich/dia-diem-du-lich-o-da-nang/khu-lang-phap.jpg");
        list[2].add("http://anbinhtravel.com/uploaded/Goc-du-lich/dia-diem-du-lich-o-da-nang/fantasy-park-da-nang.jpg");
        list[2].add("http://anbinhtravel.com/uploaded/Goc-du-lich/dia-diem-du-lich-o-da-nang/ba-na-hills.jpg");
        list[2].add("https://znews-photo.zadn.vn/w660/Uploaded/jaroin/2017_10_29/22DQT_Duong_len_tien_canh__Ba_na_hill.jpg");
        list[2].add("http://vietliketravel.vn/images/tours/2017/05/08/original/gallery-header-wpcf_780x440_1494236163.jpg");
        list[2].add("https://blog.traveloka.com/vn/wp-content/uploads/sites/9/2016/10/cam-nang-du-lich-da-nang-ba-na-hills-tu-a-toi-z-moi-nhat-1.jpg");
        list[2].add("https://dathanhtravel.com/data/images/Ba%20na%20hill%282%29.jpg");
        list[2].add("https://rivertownhoian.com/wp-content/uploads/2017/01/bana-hill-2.jpg");
        listOfOutstandingPlaces.add(new OutStandingPlace("Bà Nà Hills","Địa điểm: Hòa Ninh, Hòa Vang, Đà Nẵng",234,6,list[2],new MapsObject(16.0265452,108.0304153)));
        //Rạn nam ô



        list[3].add("https://image.tienphong.vn/w665/Uploaded/2018/vsfsfy_fymqwflzsfz/2018_03_25/3_raan.jpg");
        list[3].add("http://anbinhtravel.com/uploaded/Goc-du-lich/dia-diem-du-lich-o-da-nang/ran-nam-o-da-nang.jpg");
        list[3].add("https://static.abay.vn/ve-dep-cua-ran-nam-o.jpg");
        list[3].add("http://dacsanhuongda.vn/uploads/images/cam_nam_du_lich/2(6).jpg");
        listOfOutstandingPlaces.add(new OutStandingPlace("Rạn Nam Ô","Địa điểm: Nam Ô , Liên Chiểu, Đà Nẵng",24,6,list[3],new MapsObject(16.1158382,108.1284699)));

        // Cầu Rồng:
        list[1].add("https://static.vix.com/es/sites/default/files/styles/large/public/p/puente-da-nang-1.png?itok=JZh9NMxa");
        list[1].add("https://image.thanhnien.vn/665/uploaded/nguyentu/2018_02_03/danang-6_vnlb.jpg");
        list[1].add("http://www.namdanangland.vn/wp-content/uploads/2018/09/Cau-Rong_da-Nang11.jpg");
        list[1].add("https://cdn3.ivivu.com/2017/05/ngam-cau-rong-phun-lua-phun-nuoc-trai-nghiem-phai-thu-khi-den-du-lich-Da-Nang-ivivu-1.jpg");
        list[1].add("http://www.vietnam-tourism.com/imguploads/news/2015/T2/15CauRongdem.jpg");
        list[1].add("https://www.vntrip.vn/cam-nang/wp-content/uploads/2017/07/cau-rong-3-e1505464182836.jpg");
        listOfOutstandingPlaces.add(new OutStandingPlace("Cầu Rồng Đà Nẵng (Dragon Bridge)","Địa điểm: Nguyễn Văn Linh, Phước Ninh, Sơn Trà",34,6,list[1],new MapsObject(16.0610422,108.225783717)));




        //Ghềnh Bàng

        list[11].add("http://anbinhtravel.com/uploaded/Goc-du-lich/dia-diem-du-lich-o-da-nang/ghenh-bang-da-nang.jpg");
        list[11].add("https://dananghomestay.net/wp-content/uploads/2018/03/ghenh-bang-da-nang-min-min.jpg");
        list[11].add("https://d3tosvr3yotk6r.cloudfront.net/Content/UserUploads/Images/Events/3/ImageContent/ghenh-bang-da-nang-diem-den-moi-2.png");
        listOfOutstandingPlaces.add(new OutStandingPlace("Ghềnh Bàng","Địa điểm: Hoàng Sa, Thọ Quang, Sơn Trà",64,6,list[11],new MapsObject(16.1124387,108.3094435)));

        //Chùa Linh ứng


        list[4].add("https://znews-photo.zadn.vn/w660/Uploaded/jaroin/2017_10_29/ban2.jpg");
        list[4].add("https://taidanang.r.worldssl.net/wp-content/uploads/2016/09/tuong-quan-the-am-1.jpg");
        list[4].add("https://taidanang.r.worldssl.net/wp-content/uploads/2016/09/linh-thieng-chua-linh-ung-ban-dao-son-tra-5.jpg");
        list[4].add("http://dulichngayhe.com/wp-content/uploads/2017/08/23/08/EJJYP.jpg");
        list[4].add("https://vtv1.mediacdn.vn/thumb_w/650/2018/7/7/img1120-crop-15309491545912073757721.jpg");
        listOfOutstandingPlaces.add(new OutStandingPlace("Chùa Linh Ứng (Linh Ứng Pagoda)","Địa điểm: Hoàng Sa, Thọ Quang, Sơn Trà, Đà Nẵng",23,6,list[4],new MapsObject(16.0995214,108.2757285)));


        //Ngũ Hành Sơn


        list[5].add("https://znews-photo.zadn.vn/w660/Uploaded/jaroin/2017_10_29/son2.jpg");
        list[5].add("https://bizweb.dktcdn.net/100/183/505/files/ngu-hanh-son-1.jpg?v=1493111384340");
        list[5].add("https://vi.thefirsttravels.com/wp-content/uploads/2018/07/tour-hoi-an-ngu-hanh-son-1.jpg");
        list[5].add("http://toursdulichninhbinh.com/wp-content/uploads/2017/12/dong-thien-duong.jpg");
        list[5].add("https://charistravel.vn/wp-content/uploads/2016/12/nguhanhson13.jpg");
        listOfOutstandingPlaces.add(new OutStandingPlace("Ngũ Hành Sơn","Địa điểm: 52 Huyền Trân Công Chúa, Hoà Hải, Ngũ Hành Sơn",75,6,list[5],new MapsObject(16.0045181,108.2595895)));

        //Biển Mỹ Khê


        list[6].add("https://owa.bestprice.vn/images/destinations/uploads/bai-bien-my-khe-542bc385afcf9.jpg");
        list[6].add("https://znews-photo.zadn.vn/w660/Uploaded/jaroin/2017_10_29/19DQTBinh_minh_DN.jpg");
        list[6].add("http://www.cheapflightslab.com/wp-content/uploads/2016/10/db4ed-baros-maldives.jpg");
        list[6].add("https://toquoc.mediacdn.vn/Upload/Article/doanvankhanh/2016/4/29/rez_35_bien-my-khe.jpg");
        listOfOutstandingPlaces.add(new OutStandingPlace("Biển Mỹ Khê (Mỹ Khê Beach)","Địa điểm: Phước Mỹ, Đà Nẵng",67,6,list[6],new MapsObject(16.0639257,108.2379384)));

        //Cầu Tình Yêu



        list[7].add("http://www.dulichdanang.biz.vn/_/rsrc/1486448631044/diem-du-lich/cau-tinh-yeu-da-nang/cau-tinh-yeu-da-nang.jpg");
        list[7].add("https://canhdongbattanstudio.com/wp-content/uploads/2018/07/C43A1206_1.jpg");
        list[7].add("http://www.dulichdanang.biz.vn/_/rsrc/1486449087327/diem-du-lich/cau-tinh-yeu-da-nang/cautinhyeudanang.jpg");
        listOfOutstandingPlaces.add(new OutStandingPlace("Cầu Tình Yêu (Love Bridge)","Địa điểm: Trần Hưng Đạo, An Hải Trung, Sơn Trà",28,6,list[7],new MapsObject(16.0632691,108.2274244)));

        //Cá chép hóa rồng
        list[8].add("http://photo.danangfantasticity.com/wp-content/uploads/2018/09/Nguy%E1%BB%85n-C%E1%BA%A3nh-Ph%C3%BAc-C%C3%A1-Ch%C3%A9p-h%C3%B3a-R%E1%BB%93ng.jpg");
        list[8].add("http://media.baodansinh.vn/Images/2017/08/24/chuluongpv/3yh5.jpg");
        list[8].add("https://vietjet.net/includes/uploads/2017/01/tuong-ca-chep-hoa-rong.jpg");
        listOfOutstandingPlaces.add(new OutStandingPlace("Cá Chép Hóa Rồng","Địa điểm: Trần Hưng Đạo, An Hải Trung, Sơn Trà",47,6,list[8],new MapsObject(16.0632691,108.2274244)));

        //Cầu Sông Hàn:
        list[9].add("https://znews-photo.zadn.vn/w860/Uploaded/Sotntb/2017_10_26/danang__cau_quay_song_han_01_zing_1.jpg");
        list[9].add("http://vietnamtourism.gov.vn/images/DaNang-CausongHan.jpg");
        list[9].add("http://dulichvietvui.com.vn/image/data/da-nang/cau-song-han-quay-may-gio/Cau-song-Han-quay-luc-may-gio-1.jpg");
        listOfOutstandingPlaces.add(new OutStandingPlace("Cầu Sông Hàn","Địa điểm: An Hải Bắc, Sơn Trà, Đà Nẵng",35,6,list[9],new MapsObject(16.0721367,108.224561)));

        //Asia Park:
        list[10].add("https://congly.vn/data/news/2016/7/15/120/asia-ve-dem.jpg");
        list[10].add("https://sungroup.danang.vn/wp-content/uploads/2016/11/Asia-Park-Chau-A-4.jpg");
        list[10].add("http://media.tapchigiaothong.vn/files/cong.thanh/2016/03/14/toan-canh-asia-park-ve-dem-2256.jpg");
        listOfOutstandingPlaces.add(new OutStandingPlace("Công Viên Châu Á (Asia Park)","Địa điểm: Phan Đăng Lưu, Hoà Cường Bắc, Hải Châu",73,6,list[10],new MapsObject(16.0392633,108.2263448)));


        //Làng Vân

        list[12].add("http://anbinhtravel.com/uploaded/Goc-du-lich/dia-diem-du-lich-o-da-nang/lang-van-da-nang.jpg");
        list[12].add("http://www.baodanang.vn/dataimages/201705/original/images1369528_18.jpg");
        list[12].add("http://a9.vietbao.vn/images/vn999/120/2017/07/20170715-cam-trai-tai-ngoi-lang-bo-hoang-tai-da-nang-1.jpg");
        listOfOutstandingPlaces.add(new OutStandingPlace("Làng Vân","Địa điểm: Đèo Hải Vân, Hoà Hiệp Bắc, Liên Chiểu",191,6,list[12],new MapsObject(16.1664514,108.143546)));

        // Bãi Rạng

        list[13].add("http://anbinhtravel.com/uploaded/Goc-du-lich/dia-diem-du-lich-o-da-nang/bai-rang-da-nang.jpg");
        list[13].add("https://mytourcdn.com/upload_images/Image/Location/20_12_2016/16/du-lich-bai-rang-da-nang-mon-qua-tao-hoa-mytour-1.jpg");
        list[13].add("https://cdn3.ivivu.com/2015/09/kham-pha-bai-rang-ivivu-8.jpg");
        listOfOutstandingPlaces.add(new OutStandingPlace("Ghềnh Bàng","Địa điểm: Hoàng Sa, Thọ Quang, Sơn Trà",252,6,list[13],new MapsObject(16.1124387,108.3094435)));

        // Đỉnh bàn cờ

        list[14].add("http://www.talofofo.vn/StoreData/PageData/222/dinh-ban-co.jpg");
        list[14].add("https://www.justgola.com/media/a/00/19/105098_og_1.jpeg");
        list[14].add("https://znews-photo-td.zadn.vn/w860/Uploaded/jac_iik/2015_10_30/Anh_5.jpg");
        listOfOutstandingPlaces.add(new OutStandingPlace("Đỉnh Bàn Cờ","Địa điểm: Thọ Quang, Sơn Trà, Đà Nẵng",52,6,list[14],new MapsObject(16.1199862,108.2759459)));


















    }
*/
    private void createView() {
        recyclerView = (RecyclerView)view.findViewById(R.id.rv_outstanding);

        listOfOutstandingPlaces=DataPostsRetrieve.getInstance().getListOutstanding();
        AdapterOutStanding adapterOutStanding = new AdapterOutStanding(getActivity(),getContext(),listOfOutstandingPlaces);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterOutStanding);
    }

}
