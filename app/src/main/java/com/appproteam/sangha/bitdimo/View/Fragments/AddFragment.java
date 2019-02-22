package com.appproteam.sangha.bitdimo.View.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appproteam.sangha.bitdimo.CustomPhotoGalleryActivity;
import com.appproteam.sangha.bitdimo.MainActivity;
import com.appproteam.sangha.bitdimo.MapsActivity;
import com.appproteam.sangha.bitdimo.Presenter.Objects.MapsObject;
import com.appproteam.sangha.bitdimo.R;
import com.appproteam.sangha.bitdimo.Retrofit.ExplorePost;
import com.appproteam.sangha.bitdimo.Singleton.DataGalery;
import com.appproteam.sangha.bitdimo.Singleton.DataPostsRetrieve;
import com.appproteam.sangha.bitdimo.View.Adapter.AdapterAdd;
import com.appproteam.sangha.bitdimo.MultipleImagesSelector.ImagesSelectorActivity;
import com.appproteam.sangha.bitdimo.MultipleImagesSelector.SelectorSettings;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;



import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;


public class AddFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static int count =0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2807;
    private static final int REQUEST_CODE_CHOOSE =1997 ;
    final int PICK_IMAGE_MULTIPLE = 1;
    AdapterAdd adapterAdd;
    String imageEncoded;
    List<String> imagesPathList;
    private CardView btn_add,btn_add_location;
    private RecyclerView rv_addpicture;
    View view;
    List<Uri> mSelected;
    List<Uri> listUri;
    Button buttonPost,buttonExit;
    ProgressDialog progressDialog;
    EditText edt_contentPost;
    RelativeLayout rl_loadingpost;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> mResults =new ArrayList<>();
    private List<String> listMaps = new ArrayList<>();


    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     //* @param param1 Parameter 1.
   //  * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(/*String param1, String param2*/) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_add, container, false);
        initView();
        return  view;
    }

    private void initView() {
        btn_add=(CardView) view.findViewById(R.id.cv_addpicture);
        btn_add_location=(CardView)view.findViewById(R.id.cv_addlocation);
        rv_addpicture=(RecyclerView)view.findViewById(R.id.rv_add_picture);
        buttonPost = (Button)view.findViewById(R.id.btn_add_status);
        buttonExit = (Button)view.findViewById(R.id.btn_exit_status);
        edt_contentPost=(EditText)view.findViewById(R.id.edt_status_feeling);
        rl_loadingpost = (RelativeLayout)view.findViewById(R.id.rl_loadingpost);
        buttonExit.setOnClickListener(this);
        buttonPost.setOnClickListener(this);
        btn_add_location.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.cv_addpicture:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED || getActivity().checkSelfPermission(Manifest.permission.WRITE_SETTINGS)
                            != PackageManager.PERMISSION_GRANTED ) {

                        // Should we show an explanation?
                        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            // Explain to the user why we need to read the contacts
                        }

                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_SETTINGS},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                        // app-defined int constant that should be quite unique


                    }
                }

                    Intent intent = new Intent(getActivity(), ImagesSelectorActivity.class);
    // max number of images to be selected
                    intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
    // min size of image which will be shown; to filter tiny images (mainly icons)
                    intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
    // show camera or not
                    intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
    // pass current selected images as the initial value
                    intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
    // start the selector
                    startActivityForResult(intent, REQUEST_CODE_CHOOSE);
                    break;
                    case R.id.cv_addlocation:
                        Intent mapsIntent = new Intent(getActivity(),MapsActivity.class);
                        mapsIntent.putExtra("typeofview",1);
                        startActivity(mapsIntent);
                        break;
            case R.id.btn_add_status:
                createUri(DataGalery.getInstance().listEncodeImage);
                if(listUri!=null && listUri.size()>0)
                        upFirebase();
                break;

            case R.id.btn_exit_status:
                adapterAdd.resetList();
                edt_contentPost.setText("");
                listMaps.clear();
                break;

            }



        }

    private void pickMultiImage() {
        Intent intent = new Intent(getActivity(),CustomPhotoGalleryActivity.class);
        startActivityForResult(intent,PICK_IMAGE_MULTIPLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: aaaaaaa");
            mResults = new ArrayList<>();
            mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
            DataGalery.getInstance().putListEncodeImages(mResults);
            Log.d(TAG, "onActivityResult: aaaaaaasize" + mResults.size());

            if(adapterAdd==null)
            {
                Log.d(TAG, "onActivityResult: aaaaaaa11111");
                adapterAdd = new AdapterAdd(mResults,getContext(),getActivity());
                rv_addpicture.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                rv_addpicture.setAdapter(adapterAdd);
            }
            else {
                adapterAdd.updateNewList(mResults);
                Log.d(TAG, "onActivityResult: aaaaaaa22222");

            }
        }





    }
    private void createUri(List<String> listOfURL)
    {
        listUri = new ArrayList<>();
        for (String a : listOfURL)
        {
            Uri uri = Uri.fromFile(new File(a));
            listUri.add(uri);

        }


    }
    private void upFirebase()
    {
        rl_loadingpost.setVisibility(View.VISIBLE);
        for (int i =0 ; i< listUri.size();i++) {
            StorageReference ref = MainActivity.storageReference.child("images/"+i);
            ref.putFile(listUri.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            /*Uri uriCurrent =taskSnapshot.getMetadata().getReference().getDownloadUrl().getResult();
                            for (ExplorePlaceLinear explorePlaceLinear :listExploreLinear)
                            {
                                explorePlaceLinear.setUrlImageOfUser(uriCurrent.toString());
                            }

                            ManageFragment.this.adapterManager.notifyDataSetChanged();

*/

                    Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String photoStringLink = uri.toString();
                            System.out.println(photoStringLink);
                            listMaps.add(photoStringLink);
                            count++;
                            checkSizeArray(count);

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    count=0;
                    Toast.makeText(getContext(), "Đăng bài viết thất bại!", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
    }

    private void checkSizeArray(int count) {
        if (count>=listUri.size()) {
            adapterAdd.resetList();
            edt_contentPost.setText("");
            Toast.makeText(getActivity(), "Đăng bài viết thành công!", Toast.LENGTH_LONG).show();
            rl_loadingpost.setVisibility(View.GONE);
            count=0;
           /* ExplorePost explorePost = new ExplorePost("Hà Xuân Sáng","3/1/2019",edt_contentPost.getText().toString(),"https://scontent.fdad3-3.fna.fbcdn.net/v/t1.0-9/46400402_1431629916973324_3330668291038380032_n.jpg?_nc_cat=109&_nc_ht=scontent.fdad3-3.fna&oh=7722d6140f93200d2baa4147cf38e4cf&oe=5C64C70A",0,0,listMaps,new MapsObject(MapsActivity.choosenLocation.getLatitude(),MapsActivity.choosenLocation.getLongitude()),"");
            DataPostsRetrieve.getInstance().addElement(explorePost);
            ExploreFragment.adapterExplore.notifyDataSetChanged();
            ExploreFragment.adapterExploreLinear.notifyDataSetChanged();*/
            listMaps.clear();
        }

    }


}
