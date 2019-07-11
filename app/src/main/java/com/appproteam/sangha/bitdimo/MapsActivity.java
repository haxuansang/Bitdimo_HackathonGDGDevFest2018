package com.appproteam.sangha.bitdimo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;

import android.support.annotation.NonNull;

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Toast;


import com.appproteam.sangha.bitdimo.Presenter.Objects.RoadObject;
import com.appproteam.sangha.bitdimo.View.Adapter.AdapterBottomSheet;
import com.appproteam.sangha.bitdimo.View.CallBack.MapActions;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapClickListener, View.OnClickListener, RoutingListener {
    LocationManager locationManager;
    private GoogleMap mMap;
    private List<Polyline> polylines;
    private List<LatLng> listGPSTracking;
    private static final int LOCATION_REQUEST_CODE = 101;
    SupportMapFragment mapFragment;
    public static Marker  currentMarker;
    public int typeOfView=0;
    public String latidude,longtitude;
    Button btn_submit,btn_getCurentLocation;
    EditText editTextPlace;
    private Location currentLocation;
    private String currentPlaceName;
    private boolean isFirst=true;
    Location location;
    private Intent mapIntent;
    private String placeNameToSearch;
    GeoApiContext geoApiContext;
    private ProgressDialog progressDialog;
    private ImageButton btn_choice_view;
    private LinearLayout bottomSheetDialog;
    private BottomSheetBehavior bottomSheetBehavior;
    private ArrayList<Route> mRoute;
    private String currentPlace="";
    public static Location choosenLocation;

    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark,R.color.colorPrimary,R.color.primary_material_light_1,R.color.accent,R.color.primary_dark_material_light};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fakeGPS();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        geoApiContext= new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        btn_submit=(Button)findViewById(R.id.btn_submit_map);
        btn_submit.setOnClickListener(this);
        btn_getCurentLocation=(Button)findViewById(R.id.btn_getCurrentLocation);
        btn_getCurentLocation.setOnClickListener(this);
        editTextPlace=(EditText)findViewById(R.id.edt_placename);
        btn_choice_view=(ImageButton)findViewById(R.id.choice_street);
        editTextPlace.setClickable(false);
        editTextPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                        if(!editTextPlace.getText().toString().equals(currentPlaceName))
                            btn_submit.setText("Tìm");
            }
        });
        mapIntent = getIntent();
        typeOfView=mapIntent.getIntExtra("typeofview",0);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mapFragment= (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


    }

    private void fakeGPS() {
    }


    private void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Xin hãy bật GPS để có trải nghiệm tốt nhất!")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (location == null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 10, this);
        } else {


            Log.d("sangha", "locationaaaa: "+location.getLongitude()+"\t"+location.getLatitude());
            this.currentLocation=location;
            if(typeOfView==0) {
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("Vị trí của bạn");
                mMap.addMarker(markerOptions);
            }
            else  {
                animateCameraMap(location);
                AddToMap(location);
            }


        }
        if(typeOfView==0) {
            longtitude = mapIntent.getStringExtra("longtitude");
            latidude = mapIntent.getStringExtra("latitude");
            currentPlace = mapIntent.getStringExtra("nameofplace");

            LatLng latLng = new LatLng(Double.parseDouble(latidude), Double.parseDouble(longtitude));
            Location newLocation = new Location("");
            newLocation.setLongitude(latLng.longitude);
            newLocation.setLatitude(latLng.latitude);
            AddToMap(newLocation);
            animateCameraMap(newLocation);
            editTextPlace.setEnabled(false);
        }
        else {
            btn_submit.setText("Chọn");
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        statusCheck();
        if (typeOfView == 1)
        mMap.setOnMapClickListener(this);
        mMap.setOnMyLocationClickListener(this);

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                break;

        }
    }
    private void animateCameraMap(Location mLocation)
    {
        LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
    }

    private void AddToMap(Location location) {

        //Fetching the last known location using the Fus
        if(currentMarker!=null)
            currentMarker.remove();
        //MarkerOptions are used to create a new Marker.You can specify location, title etc with MarkerOptions

        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(currentPlace);
        currentMarker=mMap.addMarker(markerOptions);
        editTextPlace.setText(getPlacename(location));



    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }



    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation=location;
        if(typeOfView==0) {
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("Vị trí của bạn");
            currentMarker = mMap.addMarker(markerOptions);
        }
        else  {
            animateCameraMap(location);
            AddToMap(location);
        }


        Log.d("sangha", "locationaaaa: "+location.getLongitude()+"\t"+location.getLatitude());
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Location newLocation = new Location("");
        newLocation.setLongitude(latLng.longitude);
        newLocation.setLatitude(latLng.latitude);
        if (currentLocation!=null)
        btn_getCurentLocation.setVisibility(View.VISIBLE);
        AddToMap(newLocation);
        choosenLocation=newLocation;
    }
    public static Marker returnCurrentMarker()
    {
        return currentMarker;
    }
    private String getPlacename(Location location)
    {
        Geocoder myLocation = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> myList = null;
        try {
            myList = myLocation.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = (Address) myList.get(0);
        String addressStr = "";
        addressStr += address.getAddressLine(0) + ", ";
        addressStr += address.getAddressLine(1) + ", ";
        addressStr += address.getAddressLine(2);
        String main= addressStr;
        Log.d("maps", "getPlacename: "+addressStr.indexOf("null"));
        if(addressStr.indexOf("null")>0 && addressStr.indexOf("null")<addressStr.length()) {
            main = addressStr.substring(0,addressStr.indexOf("null")-2);
        }
        currentPlaceName=main;
        return  main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_getCurrentLocation:
                if(btn_getCurentLocation.getVisibility()==View.VISIBLE)
                {   btn_getCurentLocation.setVisibility(View.GONE);
                    AddToMap(location);
                    animateCameraMap(this.currentLocation);
                    btn_submit.setText("Chọn");
                }
                break;
            case R.id.btn_submit_map:
                if (btn_submit.getText().toString().equals("Tìm"))
            {
                    btn_submit.setText("Chọn");
                    placeNameToSearch = editTextPlace.getText().toString();
                    searchPlaceName(placeNameToSearch);


            }
            else

                 if(btn_submit.getText().toString().equals("Dẫn đường"))

                 {


                     if (currentLocation != null) {

                        progressDialog = new ProgressDialog(this);
                         progressDialog.setMessage("Tìm đường");
                         progressDialog.show();
                          LatLng start = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                          LatLng end = new LatLng(Double.parseDouble(latidude), Double.parseDouble(longtitude));
                         Routing routing = new Routing.Builder().key("AIzaSyC1rU8F0fBtYFA3Vsj28v3w_025sLGHX0I")
                                 .travelMode(AbstractRouting.TravelMode.DRIVING)
                                 .withListener(this)
                                 .alternativeRoutes(true)
                                 .waypoints(start, end)
                                 .build();
                         routing.execute();
                         }
                     else Toast.makeText(this, "Đang chờ xác nhận vị trí của bạn", Toast.LENGTH_SHORT).show();
                     }
                     else
                 {
                     this.finish();
                    // Toast.makeText(this, ""+choosenLocation.getLatitude()+"\n"+choosenLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                 }
                    break;
                 }
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }

    private void searchPlaceName(String placeNameToSearch) {
        List<Address>addressList = null;
        if (placeNameToSearch!= null || !placeNameToSearch.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(placeNameToSearch, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                Location currentLocation = new Location("");
                currentLocation.setLatitude(latLng.latitude);
                currentLocation.setLongitude(latLng.longitude);
                animateCameraMap(currentLocation);
                AddToMap(currentLocation);
            }
            catch (Exception E)
            {
                Toast.makeText(this, "Không tìm kiếm thấy địa điểm", Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        progressDialog.dismiss();
        Log.d("sanghaaa", "onRoutingFailure: "+e.toString());
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        progressDialog.dismiss();
        btn_choice_view.setVisibility(View.VISIBLE);
        mRoute=route;

        List<RoadObject> listRoadObjects = new ArrayList<>();

        bottomSheetDialog = (LinearLayout)findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetDialog);
        bottomSheetBehavior.setHideable(true);
               RecyclerView rv_bottom = (RecyclerView)bottomSheetDialog.findViewById(R.id.rv_bottom_sheetlayout);
               bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        btn_choice_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_HIDDEN)
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    else   bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        mMap.moveCamera(center);

        if (polylines != null)
            if (polylines.size() > 0) {
                for (Polyline poly : polylines) {
                    poly.remove();
                }
            }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            listRoadObjects.add(new RoadObject("",String.valueOf(route.get(i).getDistanceValue()),String.valueOf(route.get(i).getDurationValue())));
        }
        AdapterBottomSheet adapterBottomSheet = new AdapterBottomSheet(listRoadObjects, getBaseContext(), MapsActivity.this, new MapActions() {
            @Override
            public void chooseRoad(int position) {
                if (polylines != null)
                    if (polylines.size() > 0) {
                        for (Polyline poly : polylines) {
                            poly.remove();
                        }
                    }

                int colorIndex = position % COLORS.length;

                PolylineOptions polyOptions = new PolylineOptions();
                polyOptions.color(getResources().getColor(COLORS[colorIndex]));
                polyOptions.width(10 + position * 3);
                polyOptions.addAll(mRoute.get(position).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylines.add(polyline);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                Toast.makeText(getApplicationContext(), "Con đường " + (position + 1) + ": Khoảng cách " + mRoute.get(position).getDistanceValue() + "m, Thời gian di chuyển " + mRoute.get(position).getDurationValue()/60 +" phút", Toast.LENGTH_SHORT).show();
            }
        });
        rv_bottom.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rv_bottom.setAdapter(adapterBottomSheet);
    }


    @Override
    public void onRoutingCancelled() {
        progressDialog.dismiss();
    }
    @Override public boolean dispatchTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(bottomSheetBehavior!=null)
            if (bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED) {

                Rect outRect = new Rect();
                bottomSheetDialog.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY()))
                   bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }

        return super.dispatchTouchEvent(event);
    }
    private void hideKeyBoard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
