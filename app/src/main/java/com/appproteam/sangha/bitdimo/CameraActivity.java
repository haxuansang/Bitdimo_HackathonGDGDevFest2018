package com.appproteam.sangha.bitdimo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.appproteam.sangha.bitdimo.Utils.MyLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.VideoResult;


import java.io.File;
import java.util.ArrayList;
import java.util.Random;


public class CameraActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{
    boolean isFirstTime = true;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(isFirstTime) {
                progress.dismiss();
                isFirstTime=false;
                runThreadTracking();
                imvRecord.setVisibility(View.VISIBLE);
                btnRecording.setBackgroundResource(R.drawable.ic_stop_recording);
                Log.d(TAG, "First Time: "+ location.getLatitude() +"\t" + location.getLongitude() +"\t" +time);
                list.add(location);
                listTime.add(time);
            }
            else {
                Log.d(TAG, "onLocationChanged: " + location.getLatitude() + "\t" + location.getLongitude() + "\t" + time);
            }

        }
    };
    MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            firstLocation = location;
            long beginTime = 0;
            listTime.add(beginTime);
            list.add(location);
            Log.e(TAG, "gotLocation: " + location.getLatitude() + "\t" + location.getLongitude());
            Toast.makeText(CameraActivity.this, "You got first location. You can record video right now!", Toast.LENGTH_SHORT).show();
            btnRecording.setOnClickListener(recordListener);
            progress.dismiss();
        }
    };
    private LocationRequest locationRequest;
    private static final float MIN_DISPLACEMENT = 1;
    private static final int LOCATION_REQUEST_CODE = 101;
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
    Thread threadControl;
    Thread threadTimeCounter;
    boolean activityStopped = false;
    Location firstLocation;
    MyLocation myLocation;
    long time = 0;
    public static TinyDB tinyDB;
    int minute = 0;
    int second = 0;
    ImageView imvRecord;
    int countCallback=0;
    ProgressDialog progress;
    public static Uri uri;
    private boolean started = false;


    View.OnClickListener recordListener = new View.OnClickListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onClick(View view) {
            if (!isRecording) {
                isRecording = true;
                runThreadTracking();
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
                tv_duration.setText("00:00");
                imvRecord.setVisibility(View.INVISIBLE);
                btnRecording.setBackgroundResource(R.drawable.ic_record);
                cameraKitView.stopVideo();
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
        Intent intent = new Intent(CameraActivity.this,JourneyMap.class);
        startActivity(intent);

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
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE);

        tinyDB = new TinyDB(this);
        setContentView(R.layout.activity_camera);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setSmallestDisplacement(MIN_DISPLACEMENT);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        btnRecording = (ImageButton) findViewById(R.id.btn_record);
        imvRecord = findViewById(R.id.dotRecord);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        btnRecording.setOnClickListener(recordListener);
        cameraKitView = findViewById(R.id.camera);
        tv_duration = (TextView) findViewById(R.id.tv_duration);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        cameraKitView.addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(@NonNull VideoResult result) {
                super.onVideoTaken(result);
                tinyDB.putObject("uriVideo",addVideo(result.getFile()));
                moveActivity();
            }
        });


       /* cameraKitView.setCameraListener(new CameraListener() {
            @Override
            public void onCameraOpened() {
                super.onCameraOpened();

            }

            @Override
            public void onVideoTaken(File video) {
                super.onVideoTaken(video);
                tinyDB.putObject("uriVideo",addVideo(video));
                progress.show();
                startChecking();
            }
        });*/
        btnRecording = (ImageButton) findViewById(R.id.btn_record);



    }



    /*  private void loadVideoFromInternalStorage(String filePath){

          Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+filePath);
          myVideoView.setVideoURI(uri);

      }
       */
    public Uri addVideo(File videoFile) {
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Video.Media.TITLE, "video");
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, videoFile.getAbsolutePath());
        uri = Uri.fromFile(videoFile);
        return getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

    }
    private void runThreadTracking() {
        cameraKitView.takeVideo(new File(getFilesDir(), "video.mp4"));


      /*  threadControl = new Thread(new Runnable() {
            public void run() {
                do {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            countCallback++;
                            myLocation.getLocation(CameraActivity.this, CameraActivity.this, new MyLocation.LocationResult() {
                                @Override
                                public void gotLocation(Location location) {
                                    list.add(location);
                                    listTime.add(time);
                                    Log.e(TAG, "gotLocation: " + list.size() + "\t" + countCallback);
                                }
                            });
                        }
                    });
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (isRecording);
            }
        });
         threadControl.start();*/

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

    }

    @Override
    protected void onPause() {

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
        cameraKitView.open();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        for (int grantResult : grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (valid && !cameraKitView.isOpened()) {
            cameraKitView.open();
        }
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

    }
}
