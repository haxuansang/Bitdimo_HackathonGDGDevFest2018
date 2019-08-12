package com.appproteam.sangha.bitdimo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.appproteam.sangha.bitdimo.Utils.MyLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;
import com.wonderkiln.camerakit.CameraKitEventListenerAdapter;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class CameraActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{
    private KalmanLatLong kalmanFilter;
    private long runStartTimeInMillis;
    private static final int TIMEREQUESTLOCATION = 1 ;
    int countmain = 0;
    boolean isFirstTime = true;
    private Uri uri;
    ImageButton imvMoveJourney;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: "+location.getAccuracy());
            if (location.getAccuracy() <= MINIUM_ACCURACY && location.getAccuracy()>0) {
                if (lastKnownLocation == null || lastKnownLocation.distanceTo(location) > MIN_DISPLACEMENT) {
                    if (isFirstTime) {
                        progress.dismiss();
                        isFirstTime = false;
                        runThreadTracking();
                        imvRecord.setVisibility(View.VISIBLE);
                        btnRecording.setBackgroundResource(R.drawable.ic_stop_record_24dp);
                        Log.d(TAG, "First Time: " + location.getLatitude() + "\t" + location.getLongitude() + "\t" + time);
                        countmain++;
                    }
                    else {
                        Log.d(TAG, "onLocationChanged: " + location.getLatitude() + "\t" + location.getLongitude() + "\t" + time);
                        Toast.makeText(CameraActivity.this, "got " + ++countmain + " location at "+time, Toast.LENGTH_SHORT).show();
                    }
                    list.add(location);
                    listTime.add(location.getTime());
                    lastKnownLocation = location;
                }
            }
        }
    };
    private LocationRequest locationRequest;
    private static final float MIN_DISPLACEMENT = 10;
    private static final float MINIUM_ACCURACY = 14;
    private Location lastKnownLocation;
    private GoogleApiClient googleApiClient;
    private boolean isConnected;
    public static final String LOGTAG = "VIDEOCAPTURE";
    private static final String TAG = "sangha123";
    TextView tv_duration;
    ImageButton btnRecording;
    boolean isRecording = false;
    ArrayList<Location> list = new ArrayList<>();
    ArrayList<Long> listTime = new ArrayList<>();
    int count = 0;
    Random rand = new Random();
    private CameraView cameraKitView;
    Thread threadTimeCounter;
    boolean activityStopped = false;
    Location firstLocation;
    MyLocation myLocation;
    long time = 0;
    public static TinyDB tinyDB;
    int minute = 0;
    int second = 0;
    ImageView imvRecord;
    ProgressDialog progress;
    View.OnClickListener recordListener = new View.OnClickListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onClick(View view) {
            if (!isRecording) {
                isRecording = true;
                if(isConnected) {
                    progress.show();
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
                }
                else {
                    Toast.makeText(CameraActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                }

            }
            else {
                isRecording = false;
                activityStopped = true;
                imvRecord.setVisibility(View.INVISIBLE);
                btnRecording.setBackgroundResource(R.drawable.ic_record);
                cameraKitView.stopVideo();
                tv_duration.setText("00:00");
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
            }
        }
    };

    private void moveActivity() {
        Toast.makeText(this, "You have journey", Toast.LENGTH_SHORT).show();
        Gson gson = new Gson();
        String sda,sdb;
        sda = gson.toJson(list);
        gson = new Gson();
        sdb = gson.toJson(listTime);
        tinyDB.putString("ListLocation", sda);
        tinyDB.putString("ListTime", sdb);
        moveJourney();
    }
    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusCheck();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera);
        kalmanFilter = new KalmanLatLong(3);
        tinyDB = new TinyDB(this);
        setContentView(R.layout.activity_camera);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setInterval(0);
        btnRecording = (ImageButton) findViewById(R.id.btn_record);
        btnRecording = (ImageButton) findViewById(R.id.btn_record);
        imvRecord = findViewById(R.id.dotRecord);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
        btnRecording.setOnClickListener(recordListener);
        cameraKitView = findViewById(R.id.camera);
        tv_duration = findViewById(R.id.tv_duration);
        imvMoveJourney  = findViewById(R.id.imv_move_journey);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while get location...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        cameraKitView.addCameraKitListener(new CameraKitEventListenerAdapter() {
            @Override
            public void onVideo(CameraKitVideo video) {
                super.onVideo(video);
                addVideo(video.getVideoFile());
                String s = video.getVideoFile().getAbsolutePath();
                tinyDB.putString("UriVideo",s);
                moveActivity();
            }
        });
        imvMoveJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveJourney();
            }
        });
    }

    private void moveJourney() {
        Intent intent = new Intent(CameraActivity.this,JourneyMap.class);
        startActivity(intent);
        finish();
    }

    public void addVideo(File videoFile) {
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Video.Media.TITLE, "video");
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, videoFile.getAbsolutePath());
        getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }
    private void runThreadTracking() {
        cameraKitView.captureVideo();
        threadTimeCounter = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    time += 100;
                    if (time % 1000 == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                second++;
                                if (second == 60) {
                                    second = 0;
                                    minute++;
                                }
                                if (minute == 60) {
                                    minute = 0;
                                }
                                tv_duration.setText(""
                                        + (minute > 9 ? minute : ("0" + minute))
                                        + ":"
                                        + (second > 9 ? second : "0" + second));
                            }
                        });
                    }
                }
                while (!activityStopped);
            }
        });
        threadTimeCounter.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.start();
    }

    @Override
    protected void onPause() {
        cameraKitView.stop();
        activityStopped = true;
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isConnected = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        isConnected = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        isConnected = false;
    }
}
