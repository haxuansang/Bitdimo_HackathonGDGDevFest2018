package com.appproteam.sangha.bitdimo;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
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
import android.graphics.Color;
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
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
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


public class JourneyMap extends AppCompatActivity implements OnMapReadyCallback{
    private static final String TAG = "sangha123";
    private StorageReference mStorageRef;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 1111;
    private Button btnChoose;
    public ProgressDialog progressDialog;
    MediaController mediaController;
    SeekBar seekBar;
    private PlayerView playerView;
    private BitmapDescriptor userPositionMarkerBitmapDescriptor;
    private boolean playWhenReady;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    SupportMapFragment mapFragment;
    GeoApiContext geoApiContext;
    private SimpleExoPlayer player;
    LocationManager locationManager;
    private GoogleMap mMap;
    private List<Polyline> polylines;
    private static final int LOCATION_REQUEST_CODE = 101;
    public static Marker currentMarker;
    public int typeOfView = 0;
    private boolean isFirst = true;
    private List<LatLng> listLatLng = new ArrayList<>();
    private List<LatLng> listRouteLatLng = new ArrayList<>();
    Location location;
    volatile boolean activityStopped = false;
    volatile boolean isTracking = false;
    volatile boolean isSeeking = false;
    public int i = 0;
    int count = 0;
    List<Location> listLocation;
    List<Long> listTime;
    TinyDB tinyDB;
    private boolean started = false;
    private Handler handler = new Handler();
    int countCallPolyLine = 0;
    private Uri uri;
    private Marker userPositionMarker;

    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark, R.color.colorPrimary, R.color.primary_material_light_1, R.color.accent, R.color.primary_dark_material_light};
    private Circle locationAccuracyCircle;
    private boolean mExoPlayerFullscreen = false;

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());


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

                if (playWhenReady && playbackState == ExoPlayer.STATE_READY) {
                    isTracking = true;
                } else if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    isTracking = false;
                    processSeekTracking();
                    Log.e(TAG, "onPlayerStateChanged: buffer." );
                } else {
                    isTracking = false;
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
        String uriString = tinyDB.getString("UriVideo");
        uri = Uri.parse(uriString);
        MediaSource mediaSource = buildMediaSourcec(uri);
        player.prepare(mediaSource, true, false);

    }
    private void processSeekTracking() {
        count = 0;
        long time = player.getCurrentPosition();
        while (time > listTime.get(count)) {
            count++;
        }
        if(count>0) {
            count--;
        }
        isTracking = true;

    }

    private MediaSource buildMediaSourcec(Uri uri) {
        DataSource.Factory dataSourceFactory = new FileDataSourceFactory();
        return new ExtractorMediaSource(uri, dataSourceFactory,
                new DefaultExtractorsFactory(), null, null);
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
        Type type = new TypeToken<List<Location>>() {
        }.getType();
        Type typeTime = new TypeToken<List<Long>>() {
        }.getType();
        Gson gson = new Gson();
        listLocation = gson.fromJson(listLocationString, type);
        for (Location location : listLocation) {
            Log.d(TAG, "onCreate: " + location.getLatitude() + "\t" + location.getLongitude());
        }
        gson = new Gson();
        listTime = gson.fromJson(listTimeString, typeTime);
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
        for (Location location : listLocation) {
            listLatLng.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }

    }

    private void bikeRunning() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    if (isTracking) {
                        Location location = listLocation.get(count);
                        long firstTime = listTime.get(count);
                        long secondTime = listTime.get(++count);
                        long timedelaya = 0;
                        if (isTracking) {
                            timedelaya = secondTime - listTime.get(0) - player.getCurrentPosition();
                        } else {
                            timedelaya = (secondTime - firstTime);
                        }
                        if (timedelaya > 0) {
                            SystemClock.sleep(timedelaya);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drawLocationAccuracyCircle(location);
                                drawUserPositionMarker(location);
                                animateCameraMap(location);
                            }
                        });
                    }

                }
                while (count < listTime.size() - 1);
            }
        }).start();

    }

    private void drawUserPositionMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (this.userPositionMarkerBitmapDescriptor == null) {
            userPositionMarkerBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_circular_shape);
        }

        if (userPositionMarker == null) {
            userPositionMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .flat(true)
                    .anchor(0.5f, 0.5f)
                    .icon(this.userPositionMarkerBitmapDescriptor));
        } else {
            userPositionMarker.setPosition(latLng);
        }
    }

    private void drawLocationAccuracyCircle(Location location) {
        Log.d(TAG, "drawLocationAccuracyCircle: " + location.getAccuracy());
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (this.locationAccuracyCircle == null) {
            this.locationAccuracyCircle = mMap.addCircle(new CircleOptions()
                    .center(latLng)
                    .fillColor(Color.argb(64, 0, 0, 0))
                    .strokeColor(Color.argb(64, 0, 0, 0))
                    .strokeWidth(0.0f)
                    .radius(location.getAccuracy())); //set readius to horizonal accuracy in meter.
        } else {
            this.locationAccuracyCircle.setCenter(latLng);
        }
    }


    private void updateVideoBar() {

        addPolylineGPS2(listLatLng);
        drawUserPositionMarker(listLocation.get(0));
        drawLocationAccuracyCircle(listLocation.get(0));
        bikeRunning();


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




    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (listLocation.size()>0) {
            Location location = listLocation.get(0);
            animateCameraMap(location);
            updateVideoBar();
        }

    }

    private void addPolylineGPS2(List<LatLng> list) {
        //In case of more than 5 alternative routes
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(getResources().getColor(COLORS[0]));
        polyOptions.width(20);
        polyOptions.addAll(list);
        mMap.addPolyline(polyOptions);
    }


    private void animateCameraMap(Location mLocation) {
        LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        mMap.animateCamera(cameraUpdate);
    }
}
