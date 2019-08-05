package com.appproteam.sangha.bitdimo;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.appproteam.sangha.bitdimo.Presenter.Objects.UserFake;
import com.appproteam.sangha.bitdimo.View.Adapter.AdapterSpinner;
import com.appproteam.sangha.bitdimo.View.Adapter.ViewPagerAdapter;
import com.appproteam.sangha.bitdimo.View.Fragments.AddFragment;
import com.appproteam.sangha.bitdimo.View.Fragments.ExploreFragment;
import com.appproteam.sangha.bitdimo.View.Fragments.InformationFragment;
import com.appproteam.sangha.bitdimo.View.Fragments.ManageFragment;
import com.appproteam.sangha.bitdimo.View.Fragments.OutStandingFragment;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView mTextMessage;
    private Toolbar toolbar;
    private Spinner spinnerCity;
    private ViewPager viewPager;

    public static List<UserFake> listUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* getDataOfOutStandingPlaces();
        getDataOfExplorePlaces();*/
        listUser = new ArrayList<>();
        listUser.add(new UserFake("Sáng Hà","https://scontent.fdad3-3.fna.fbcdn.net/v/t1.0-9/41428499_1386227774846872_6500137452911460352_o.jpg?_nc_cat=111&_nc_ht=scontent.fdad3-3.fna&oh=ed2daa33d6c6c2e108e11a2dfaa5ca30&oe=5C7AC3D6"));
        listUser.add(new UserFake("Anh Hoàng","https://wikicachlam.com/wp-content/uploads/2018/05/hinh-anh-co-don-2.jpg"));
        listUser.add(new UserFake("Ngọc Anh","http://static2.yan.vn/YanNews/2167221/201704/20170428-104537-samsung-galaxy-j7-selfie_600x450.jpg"));
        listUser.add(new UserFake("Trâm Anh","https://ss-images.catscdn.vn/2018/09/28/3726966/cao-vy.jpg"));
        listUser.add(new UserFake("Như Hạnh","https://scontent.fdad3-3.fna.fbcdn.net/v/t1.0-9/39298797_1383451028452419_2947118911117590528_n.jpg?_nc_cat=109&_nc_ht=scontent.fdad3-3.fna&oh=d89139c6a95f6d0387ff547fe0cf8edf&oe=5C7956BE"));
        listUser.add(new UserFake("Minh Hưng","https://scontent.fdad3-3.fna.fbcdn.net/v/t1.0-9/37224598_1797407803708552_1432383490275409920_n.jpg?_nc_cat=111&_nc_ht=scontent.fdad3-3.fna&oh=4e9d33a23cd3411a748b78a212afcde2&oe=5C7376E5"));
        listUser.add(new UserFake("Thanh Hằng","https://scontent.fdad3-2.fna.fbcdn.net/v/t1.0-9/32312928_1033281130158281_3071064928476463104_n.jpg?_nc_cat=105&_nc_ht=scontent.fdad3-2.fna&oh=4d96b2117844fd3837591b64a486f011&oe=5C71FCD4"));
        listUser.add(new UserFake("Hà Giang","https://scontent.fdad3-2.fna.fbcdn.net/v/t1.0-9/45105866_2209618792694515_4317930488971919360_n.jpg?_nc_cat=101&_nc_ht=scontent.fdad3-2.fna&oh=bd11090b5c2cab78a44c3a178d4e7cd2&oe=5C8479F5"));
        listUser.add(new UserFake("Quân Hiếu","https://scontent.fdad3-3.fna.fbcdn.net/v/t1.0-9/42948900_2089898391324315_3956117667953246208_n.jpg?_nc_cat=111&_nc_ht=scontent.fdad3-3.fna&oh=776956b68d32964a10ee6caedc841b17&oe=5C6C69FE"));
        listUser.add(new UserFake("Lê Huy","https://scontent.fdad3-1.fna.fbcdn.net/v/t1.0-9/22195577_2428453153960322_7321663096301704326_n.jpg?_nc_cat=110&_nc_ht=scontent.fdad3-1.fna&oh=8021b3a03967d9b88406d61f8b259fa3&oe=5C6F5C78"));
        listUser.add(new UserFake("Huyền Trang","https://scontent.fdad3-3.fna.fbcdn.net/v/t1.0-9/33862786_790167667843131_7461004469765931008_n.jpg?_nc_cat=111&_nc_ht=scontent.fdad3-3.fna&oh=85d22711db0473adc70ef6cecd216503&oe=5C7BE193"));
        listUser.add(new UserFake("Hữu Hải","https://scontent.fdad3-3.fna.fbcdn.net/v/t1.0-9/40102028_1878605292247056_2987411025818550272_n.jpg?_nc_cat=100&_nc_ht=scontent.fdad3-3.fna&oh=d4838a586dbaba55ad6bc4d264ff2e19&oe=5CB151D4"));
        listUser.add(new UserFake("Hoàng Việt","https://scontent.fdad3-3.fna.fbcdn.net/v/t1.0-9/41545315_1843513382430279_1988500344002314240_n.jpg?_nc_cat=100&_nc_ht=scontent.fdad3-3.fna&oh=b446c1be4eaeedb05a3549f82292d963&oe=5C76E49A"));
        listUser.add(new UserFake("Linh Ny","https://scontent.fdad3-2.fna.fbcdn.net/v/t1.0-1/46030527_1969984369752647_677782475379834880_n.jpg?_nc_cat=105&_nc_ht=scontent.fdad3-2.fna&oh=a2e28786f7b119a4a0d381b714b7013b&oe=5C7F4D4C"));
        listUser.add(new UserFake("Thiện Bảo","https://scontent.fdad3-1.fna.fbcdn.net/v/t1.0-9/30124223_1834357213297023_8966075151055071504_n.jpg?_nc_cat=110&_nc_ht=scontent.fdad3-1.fna&oh=d920906a44177022392b1b30a05c39c5&oe=5CB0D333"));
        listUser.add(new UserFake("Duy Thịnh","https://scontent.fdad3-2.fna.fbcdn.net/v/t1.0-9/38755029_976170879211396_8009135347201998848_n.jpg?_nc_cat=105&_nc_ht=scontent.fdad3-2.fna&oh=0ff760a08db71115051c267ff3e01678&oe=5C7B0166"));
        listUser.add(new UserFake("Hà Xuân Sáng","https://scontent.fdad3-3.fna.fbcdn.net/v/t1.0-9/46400402_1431629916973324_3330668291038380032_n.jpg?_nc_cat=109&_nc_ht=scontent.fdad3-3.fna&oh=7722d6140f93200d2baa4147cf38e4cf&oe=5C64C70A"));


        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        Fresco.initialize(getApplicationContext());

        createView();
        createSpinner();
        createViewPager();


    }



    private void createViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(OutStandingFragment.newInstance());
        adapter.addFragment(ExploreFragment.newInstance());
        adapter.addFragment(AddFragment.newInstance());
        adapter.addFragment(ManageFragment.newInstance());
        adapter.addFragment(InformationFragment.newInstance());
        viewPager.setAdapter(adapter);

    }

    private void createSpinner() {
        String[] listCity =getResources().getStringArray(R.array.listcity);
        ArrayList<String> listOfCity = new ArrayList<>();
        for (int i=0;i<listCity.length;i++)
        {
            listOfCity.add(listCity[i]);
        }
        AdapterSpinner adapterSpinner  = new AdapterSpinner(getBaseContext(),listOfCity);
        spinnerCity.setAdapter(adapterSpinner);



    }

    private void createView() {


        mTextMessage = (TextView) findViewById(R.id.message);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        spinnerCity = (Spinner)findViewById(R.id.spinner_city);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigation.getMenu().getItem(position).setChecked(true);

                if(position==3)
                {
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_manage);
                    spinnerCity.setVisibility(View.GONE);
                }
                else
                if(position==4)
                {
                    toolbar.getMenu().clear();
                    spinnerCity.setVisibility(View.GONE);


                }
                else
                {
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_toolbar);
                    if(spinnerCity.getVisibility()!=View.VISIBLE)
                    spinnerCity.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.navigation_outstanding:
                viewPager.setCurrentItem(0);
                break;
            case R.id.navigation_explore:
                viewPager.setCurrentItem(1);
                break;
            case R.id.navigation_add:
                viewPager.setCurrentItem(2);
                break;
            case R.id.navigation_manage:
                viewPager.setCurrentItem(3);
                break;
            case R.id.navigation_information:
                viewPager.setCurrentItem(4);
                break;

        }
      return true;
    }
}
