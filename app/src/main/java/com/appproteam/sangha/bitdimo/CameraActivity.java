package com.appproteam.sangha.bitdimo;

import android.Manifest;
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
import android.support.v7.app.AppCompatActivity;
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


import com.appproteam.sangha.bitdimo.Utils.MyLocation;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mukesh.tinydb.TinyDB;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CameraActivity extends AppCompatActivity  {
    public static final String LOGTAG = "VIDEOCAPTURE";
    private static final String TAG = "sangha123";
    private CamcorderProfile camcorderProfile;
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
    TinyDB tinyDB;
    int minute = 0;
    int second = 0;
    ImageView imvRecord;
    int countCallback=0;
    ProgressDialog progress;
    private boolean started = false;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(countCallback<list.size()) {
                progress.dismiss();
                moveActivity();
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
    View.OnClickListener recordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!isRecording) {
                isRecording = true;
                cameraKitView.startRecordingVideo();
                runThreadTracking();
                imvRecord.setVisibility(View.VISIBLE);
                btnRecording.setBackgroundResource(R.drawable.ic_stop_recording);
            }
            else {
                isRecording = false;
                activityStopped = true;
                imvRecord.setVisibility(View.INVISIBLE);
                btnRecording.setBackgroundResource(R.drawable.ic_record);
                cameraKitView.stopRecordingVideo();
                tv_duration.setText("00:00");
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusCheck();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera);
        tinyDB = new TinyDB(this);
        String test = tinyDB.getString("ListLocation");
        Type type = new TypeToken<List<Location>>(){}.getType();
        Gson gson = new Gson();
        List<Location> listss = gson.fromJson(test, type);
        Toast.makeText(this, "" + listss.size(), Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_camera);
        btnRecording = (ImageButton) findViewById(R.id.btn_record);
        imvRecord = findViewById(R.id.dotRecord);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        btnRecording.setOnClickListener(recordListener);
        cameraKitView = findViewById(R.id.camera);
        tv_duration = findViewById(R.id.tv_duration);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        cameraKitView.setCameraListener(new CameraListener() {
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
        });
        btnRecording = (ImageButton) findViewById(R.id.btn_record);
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
        myLocation = new MyLocation();
        myLocation.getLocation(this, this, locationResult);

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
        return getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

    }
    private void runThreadTracking() {
        threadControl = new Thread(new Runnable() {
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
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (isRecording);
            }
        });
        threadControl.start();
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
    protected void onStart() {
        super.onStart();
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
        cameraKitView.stop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
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


}
