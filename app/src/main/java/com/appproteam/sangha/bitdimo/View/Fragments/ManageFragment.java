package com.appproteam.sangha.bitdimo.View.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.appproteam.sangha.bitdimo.EditActivity;
import com.appproteam.sangha.bitdimo.MainActivity;
import com.appproteam.sangha.bitdimo.Presenter.Objects.ExplorePlace;
import com.appproteam.sangha.bitdimo.Presenter.Objects.ExplorePlaceLinear;
import com.appproteam.sangha.bitdimo.Presenter.Objects.MapsObject;
import com.appproteam.sangha.bitdimo.R;
import com.appproteam.sangha.bitdimo.Retrofit.ExplorePost;
import com.appproteam.sangha.bitdimo.View.Adapter.AdapterManager;
import com.appproteam.sangha.bitdimo.View.CallBack.ChangeUserInformation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CircleImageView civ_user;
    private View view;
    private RecyclerView rvManage;
    private Button btn_edit;
    List<ExplorePost> listExploreLinear;
    AdapterManager adapterManager;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ManageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
    // * @param param1 Parameter 1.
   //  * @param param2 Parameter 2.
     * @return A new instance of fragment ManageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageFragment newInstance(/*String param1, String param2*/) {
        ManageFragment fragment = new ManageFragment();
        Bundle args = new Bundle();
       /* args.putString(ARG_PARAM1, param1);
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

    private void createView() {

        rvManage=(RecyclerView)view.findViewById(R.id.rv_manage);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_manage, container, false);
        createView();
        fakeData();
        return view;
    }

    private void fakeData() {

        listExploreLinear = new ArrayList<>();
        List<String> listOfPlaces = new ArrayList<>();
        listOfPlaces.add("https://yeudanang.biz/wp-content/uploads/2018/11/45041668_481346935706730_7229797798877593600_o.jpg");
        listOfPlaces.add("https://yeudanang.biz/wp-content/uploads/2018/11/45152551_481346865706737_1362042573190856704_n.jpg");
        listExploreLinear.add(new ExplorePost(MainActivity.listUser.get(16).getNameOfUser()
                ,"18/11/2018 15h00","Mãi luôn tươi trẻ khi ở Đà Nẵng"
                ,MainActivity.listUser.get(16).getUrlOfImages()
                ,100,
                6,
                listOfPlaces,new MapsObject(16.1197212,108.2691571),"Sơn Trà, Đà Nẵng"));


        //1
        listOfPlaces= new ArrayList<>();
        listOfPlaces.add("http://dulichdanang.xyz/wp-content/uploads/2016/10/cau-dn-7-Copy.jpg");
        listExploreLinear.add(new ExplorePost(MainActivity.listUser.get(16).getNameOfUser()
                ,"18/11/2018 15h00","Hội thanh xuân của tôi, những tri kỉ mà tôi may mắn có được!"
                ,MainActivity.listUser.get(16).getUrlOfImages()
                ,100,
                6,
                listOfPlaces,new MapsObject(16.051211, 108.229903),"Cầu Nguyễn Văn Trỗi, Hải Châu"));

        //2
        listOfPlaces= new ArrayList<>();
        listOfPlaces.add("https://yeudanang.biz/wp-content/uploads/2018/02/3-2.jpg");
        listOfPlaces.add("https://yeudanang.biz/wp-content/uploads/2018/02/5-2.jpg");
        listOfPlaces.add("https://yeudanang.biz/wp-content/uploads/2018/02/2-2.jpg");
        listExploreLinear.add(new ExplorePost(MainActivity.listUser.get(16).getNameOfUser()
                ,"18/11/2018 15h00","Mùa đông năm ấy.."
                ,MainActivity.listUser.get(16).getUrlOfImages()
                ,120,
                6,
                listOfPlaces,new MapsObject(15.966689, 108.284588),"Phim trường Cocobay, Hòa Hải, Ngũ Hành Sơn"));
            adapterManager = new AdapterManager(getContext(),listExploreLinear,getActivity());
            adapterManager.registerCallback(new ChangeUserInformation() {
                                                @Override
                                                public void ChangeImageUser(CircleImageView userImage) {
                                                    civ_user=userImage;
                                                    loadImageFromLibrary();
                                                }

                @Override
                public void EditUser() {
                    getActivity().startActivity(new Intent(getActivity(),EditActivity.class));

                }
            });
                    rvManage.setLayoutManager(new LinearLayoutManager(getContext()));







        rvManage.setAdapter(adapterManager);

        }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ci_user_manage:
                loadImageFromLibrary();
        }
    }

    private void loadImageFromLibrary() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), 7171);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode==7171)
            {
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Đăng ảnh");
                progressDialog.setMessage("Vui lòng chờ...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                StorageReference ref = MainActivity.storageReference.child("images/"+"admin");

                final Uri uri = data.getData();
                if(uri!=null) {
                    try {

                        final  Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Tải lên thành công", Toast.LENGTH_SHORT).show();
                            Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String photoStringLink = uri.toString();
                                    adapterManager.updateImageUser(photoStringLink);
                                }
                            });

                           // adapterManager.updateImageUser(uriCurrent);
                            /*
                            for (ExplorePlaceLinear explorePlaceLinear :listExploreLinear)
                            {
                                explorePlaceLinear.setUrlImageOfUser(uriCurrent.toString());
                            }

                            ManageFragment.this.adapterManager.notifyDataSetChanged();

*/


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Tải lên thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Đã đăng "+progress +"%");
                        }
                    });

                }

            }
        }


    }

}
