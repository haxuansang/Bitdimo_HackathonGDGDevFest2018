package com.appproteam.sangha.bitdimo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;

import com.appproteam.sangha.bitdimo.Utils.MyLocation;
import com.appproteam.sangha.bitdimo.Utils.ResizeBitmap;
import com.appproteam.sangha.bitdimo.Utils.TemporaryObjects.GPSMarkerDetect;
import com.appproteam.sangha.bitdimo.Utils.TemporaryObjects.GPSMarkerSingleton;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.GeoApiContext;
import com.mukesh.tinydb.TinyDB;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.appproteam.sangha.bitdimo.CameraActivity.tinyDB;
import static com.appproteam.sangha.bitdimo.CameraActivity.uri;

public class JourneyMap extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapClickListener, View.OnClickListener, RoutingListener {
    private static final String TAG = "sangha123";
    private StorageReference mStorageRef;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 1111;
    private Button btnChoose;
    public ProgressDialog progressDialog;
    MediaController mediaController;
    SeekBar seekBar;
    private PlayerView playerView;
    private boolean playWhenReady;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    SupportMapFragment mapFragment;
    GeoApiContext geoApiContext;
    final String vidAddress = "https://firebasestorage.googleapis.com/v0/b/wegoapp-935c3.appspot.com/o/videos%2F111111.mp4?alt=media&token=6eab1849-d2dd-46f0-8370-e94d96362555";
    private SimpleExoPlayer player;
    LocationManager locationManager;
    private GoogleMap mMap;
    private List<Polyline> polylines;
    private static final int LOCATION_REQUEST_CODE = 101;
    public static Marker currentMarker;
    public int typeOfView = 0;
    public String latidude, longtitude;
    Button btn_submit, btn_getCurentLocation;
    EditText editTextPlace;
    private Location currentLocation;
    private String currentPlaceName;
    private boolean isFirst = true;
    private List<LatLng> listLatLng = new ArrayList<>();
    private List<LatLng> listRouteLatLng = new ArrayList<>();
    Location location;
    private Intent mapIntent;
    private String placeNameToSearch;
    private ProgressDialog progressDialogMap;
    private ImageButton btn_choice_view;
    private LinearLayout bottomSheetDialog;
    private BottomSheetBehavior bottomSheetBehavior;
    private ArrayList<Route> mRoute;
    private String currentPlace = "";
    public static Location choosenLocation;
    Thread threadControl;
    volatile boolean activityStopped = false;
    LatLng endPoint = new LatLng(16.191078, 108.127618);
    public int i = 0;
    public long lastPositionPlayer = 0;
    public long beginPositionPlayer = 0;
    public boolean isStoppedThreadGetRoute = false;
    public LatLng mainLocation;
    int count = 0;
    List<Location> listLocation;
    List<Long> listTime;
    TinyDB tinyDB;
    private boolean started = false;
    private Handler handler = new Handler();
    int countCallPolyLine=0;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(!activityStopped && countCallPolyLine==0) {
                addPolylineGPS(listRouteLatLng);
               // bikeRunning();
                stop();
            }
            if(started) {
                startChecking();
            }
        }
    };

    public void stop() {
        started = false;
        handler.removeCallbacks(runnable);
    }

    public void startChecking() {
        started = true;
        handler.postDelayed(runnable, 1000);
    }
    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark, R.color.colorPrimary, R.color.primary_material_light_1, R.color.accent, R.color.primary_dark_material_light};

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());
        if(listLatLng.size()>1 && listTime.size()>1) {
            updateVideoBar();
        }
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                if (playbackState == ExoPlayer.STATE_ENDED) {
                    Log.e(TAG, "onPlayerStateChanged: 1");
                    player.seekTo(0);
                    player.setPlayWhenReady(true);

                } else if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    Log.e(TAG, "onPlayerStateChanged: 2");
                    Toast.makeText(getApplicationContext(), "Buffering..", Toast.LENGTH_SHORT).show();
                } else if (playbackState == ExoPlayer.STATE_READY) {
                    Log.e(TAG, "onPlayerStateChanged: 3");
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        playerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
     //  Uri newUri = Uri.parse(uri);
        MediaSource mediaSource = buildMediaSourcec(uri);
        player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSourcec(Uri uri) {
        DataSource.Factory dataSourceFactory = new FileDataSourceFactory();
        return new ExtractorMediaSource(uri, dataSourceFactory,
                new DefaultExtractorsFactory(), null, null);
    }

    private void callPolyline(LatLng start, LatLng endPoint) {
        Log.d(TAG, "callPolyline: " +start.latitude +"\t"+start.longitude +"\t"+endPoint.latitude+"\t"+endPoint.longitude);
        Routing routing = new Routing.Builder().key("AIzaSyC1rU8F0fBtYFA3Vsj28v3w_025sLGHX0I")
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(start, endPoint)
                .build();
        routing.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUi();
        activityStopped = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
        activityStopped = true;
    }


    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
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

    private void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                break;

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        tinyDB = new TinyDB(this);
        String listLocationString = this.tinyDB.getString("ListLocation");

        String listTimeString = this.tinyDB.getString("ListTime");
        Type type = new TypeToken<List<Location>>(){}.getType();
        Type typeTime = new TypeToken<List<Long>>(){}.getType();
        Gson gson = new Gson();
        listLocation = gson.fromJson(listLocationString, type);
        for (Location location : listLocation) {
            Log.d(TAG, "onCreate: "+ location.getLatitude()+ "\t" + location.getLongitude());
        }
        gson = new Gson();
        listTime = gson.fromJson(listTimeString,typeTime);
        Log.d(TAG, "onCreate: " + listLocation.size() + "\t" + listTime.size());
        //Toast.makeText(this, "" + listLocation.size() + "\t" + listTime.size(), Toast.LENGTH_SHORT).show();
        createListLocation();
        geoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        playerView = findViewById(R.id.myVideo);
        mediaController = new MediaController(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Video");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        btnChoose = (Button) findViewById(R.id.button);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        statusCheck();
        // if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo();
            }
        });

    }

    private void createListLocation() {
        for (Location location: listLocation){
            listLatLng.add(new LatLng(location.getLatitude(),location.getLongitude()));
        }

    }

    private void bikeRunning() {
        MarkerOptions markerOptions = new MarkerOptions().position(listLatLng.get(0)).title(currentPlace);
        currentMarker = mMap.addMarker(markerOptions);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.carmarker);
        currentMarker.setIcon(BitmapDescriptorFactory.fromBitmap(ResizeBitmap.getScaledDownBitmap(bitmap, 170, true)));
        final ArrayList<GPSMarkerDetect> list = GPSMarkerSingleton.getInstance().getGpsMarkerDetect();

        if (count > 0) {
            count = 0;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    final GPSMarkerDetect gpsMarkerDetect = list.get(count);
                    final List<LatLng> listLat = gpsMarkerDetect.getListMarkerDetect();
                    long timedelaya = (gpsMarkerDetect.getEndPositionPlayer() - gpsMarkerDetect.getStartPositionPlayer()) / listLat.size();
                    for (final LatLng latLng : listLat) {
                        SystemClock.sleep(timedelaya);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AddToMap(latLng);
                            }
                        });
                    }
                    count++;
                }
                while (count < list.size());
            }
        }).start();

    }


    private void updateVideoBar() {

        threadControl = new Thread(new Runnable() {
            public void run() {
                do {
                    if (!activityStopped /*&& player.getCurrentPosition() != 0*/)
                        playerView.post(new Runnable() {
                            public void run() {

                                LatLng currentMarker = listLatLng.get(i);
                                LatLng endMarker = listLatLng.get(i + 1);
                                Log.d(TAG, "testmarker: " +currentMarker.latitude +"\t"+currentMarker.longitude +"\t"+endMarker.latitude+"\t"+endMarker.longitude);

                                mainLocation = endMarker;
                                beginPositionPlayer = listTime.get(i);
                                lastPositionPlayer = listTime.get(i+1);
                                if (currentMarker.latitude!=endMarker.latitude || currentMarker.longitude!=endMarker.longitude) {
                                    countCallPolyLine++;
                                    callPolyline(currentMarker, endMarker);
                                    activityStopped = true;
                                    Log.d(TAG, "count route"+ countCallPolyLine);
                                }
                                i++;
                                if(i>=listLocation.size()-2 && countCallPolyLine==0){
                                    if (!activityStopped) {
                                        addPolylineGPS(listRouteLatLng);
                                       // bikeRunning();
                                    }
                                    else {
                                        receiveRoute();
                                    }
                                }
                            }
                        });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (true && i < listLatLng.size()-2);
            }
        });
        threadControl.start();
    }

    private void receiveRoute() {
        startChecking();
    }

    private void chooseVideo() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_TAKE_GALLERY_VIDEO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                progressDialog.show();
                Uri selectedImageUri = data.getData();
                //filemanagerstring = getPath(selectedImageUri);
                uploadVideo(selectedImageUri);
            }
        }
    }

    private void uploadVideo(Uri uri) {
        long id = Long.valueOf(uri.getLastPathSegment());
        StorageReference ref = mStorageRef.child("videos/" + id);
        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(JourneyMap.this, "Success!", Toast.LENGTH_SHORT).show();
                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(JourneyMap.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("uploaded " + progress + "%");
            }
        });
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void AddToMap(final LatLng latLng) {

        //Fetching the last known location using the Fus

        //MarkerOptions are used to create a new Marker.You can specify location, title etc with MarkerOptions
        currentMarker.setPosition(latLng);

//        editTextPlace.setText(getPlacename(location));
    }


    private String getPlacename(Location location) {
        Geocoder myLocation = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> myList = null;
        try {
            myList = myLocation.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = (Address) myList.get(0);
        String addressStr = "";
        addressStr += address.getAddressLine(0) + ", ";
        addressStr += address.getAddressLine(1) + ", ";
        addressStr += address.getAddressLine(2);
        String main = addressStr;
        Log.d("maps", "getPlacename: " + addressStr.indexOf("null"));
        if (addressStr.indexOf("null") > 0 && addressStr.indexOf("null") < addressStr.length()) {
            main = addressStr.substring(0, addressStr.indexOf("null") - 2);
        }
        currentPlaceName = main;
        return main;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
        if (typeOfView == 0) {
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("Vị trí của bạn");
            currentMarker = mMap.addMarker(markerOptions);
        } else {
            animateCameraMap(location);
            // AddToMap(location);
        }

        Log.d("sangha", "locationaaaa: " + location.getLongitude() + "\t" + location.getLatitude());
        //   drawPolyline(new LatLng(location.getLatitude(),location.getLongitude()));
        locationManager.removeUpdates(this);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Log.d(TAG, "onRoutingFailure: "+e);
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
       GPSMarkerDetect gpsMarkerDetect = new GPSMarkerDetect(beginPositionPlayer, lastPositionPlayer, arrayList.get(0).getPoints(), mainLocation);
        if (gpsMarkerDetect != null)
              GPSMarkerSingleton.getInstance().insertGpsMarkerDetect(gpsMarkerDetect);
       //addPolylineGPS(arrayList.get(0).getPoints());
        countCallPolyLine--;
        Log.d(TAG, "onRoutingSuccess: " + this.i);
        listRouteLatLng.addAll(arrayList.get(0).getPoints());
        activityStopped = false;
        Log.d(TAG, "route khi tru di"+ countCallPolyLine);

        //addPolylineGPS(arrayList.get(0).getPoints());
    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       /* if (location == null) {
            Log.d("sangha", "locationb");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 10, this);
        } else {
            Log.d("sangha", "locationa: " + location.getLongitude() + "\t" + location.getLatitude());

            animateCameraMap(location);
            AddToMap(location);

        }*/
/*        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                Log.e(TAG, "gotLocation: "+ location.getLatitude() +"\t" + location.getLongitude() );
                drawPolyline(new LatLng(location.getLatitude(),location.getLongitude()));

            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);
        */
        Location location = listLocation.get(0);
        animateCameraMap(location);

    }

    private void addPolylineGPS(List<LatLng> list) {

        int i = 0;
        //In case of more than 5 alternative routes
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(getResources().getColor(COLORS[0]));
        polyOptions.width(10 + i * 3);
        polyOptions.addAll(list);
        mMap.addPolyline(polyOptions);
    }

    private void animateCameraMap(Location mLocation) {
        LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        mMap.animateCamera(cameraUpdate);
    }
}
