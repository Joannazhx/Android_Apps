package com.example.jeremy.lab9;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;

public class Task3Activity extends AppCompatActivity {
    private static final int MY_WIFI = 10;
    private static final int MY_LOC= 5;

    private Button btnScan;
    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> listItems=new ArrayList<String>();
    private List<MyWifi> wifiList = new ArrayList<>();
    private List<ScanResult> results = new ArrayList<>();
    private WifiManager wifiManager;

    private double longitude;
    private double latitude;
    private String LOG_TAG = "Save";
    private static final String TAG = "MEDIA";

    private String filename = "SampleFile.txt";
    private String filepath = "MyFileStorage";
    File myExternalFile;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private String mLastUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_task3 );
        btnScan = findViewById( R.id.btnScan );
        btnScan.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWifi();
            }
        } );

        listView = findViewById( R.id.lsView );
        listAdapter = new ArrayAdapter<>( this,android.R.layout.simple_list_item_1,listItems);
        listView.setAdapter( listAdapter );

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        myExternalFile = new File(getExternalFilesDir(filepath), filename);


        // location

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = java.text.DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
//                longitude = mCurrentLocation.getLongitude();
//                latitude = mCurrentLocation.getLatitude();
            }
        };
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

        scanWifi();
        readFile();

    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            longitude = mCurrentLocation.getLongitude();
            latitude = mCurrentLocation.getLatitude();
        }


    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(Task3Activity.this, 100);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(Task3Activity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
//                        toggleButtons();
                    }
                });
    }


    private void scanWifi() {
        if ((ContextCompat.checkSelfPermission(this, ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||

                (ContextCompat.checkSelfPermission(this, CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
                ) {
            ActivityCompat.requestPermissions(this,new String[] {
                    ACCESS_WIFI_STATE,
                    ACCESS_COARSE_LOCATION,
                    CHANGE_WIFI_STATE
            },MY_WIFI);
        } else {
            startLocationUpdates();

            _scanWifi();


        }

    }

    private void _scanWifi() {
        registerReceiver(wifiReceiver, new IntentFilter(wifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifiManager.startScan();

        Toast.makeText(this,"Scanning WiFi...",Toast.LENGTH_LONG).show();

    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults(); // get the list of Wifi Access Point
            try {
                FileOutputStream fos = new FileOutputStream(myExternalFile);
                String entry = "";
                // Sorting ref: https://stackoverflow.com/questions/17285337/how-can-i-sort-the-a-list-of-getscanresults-based-on-signal-strength-in-ascend
                Comparator<ScanResult> compare_signal_strength = new Comparator<ScanResult>() {
                    @Override
                    public int compare(ScanResult scanResult, ScanResult t1) {
                        return (scanResult.level > t1.level ? -1 : (scanResult.level == t1.level ? 0 : 1));
                    }
                };
                Collections.sort(results,compare_signal_strength); // sorting the result

                wifiList.clear();
                MyWifi wifi;
                int counter = 0;
                for (ScanResult scanResult: results) {
                    wifi = new MyWifi(DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString(), scanResult.SSID, scanResult.level,latitude,longitude);


                    entry = DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString() + " " + latitude + " " +longitude + " " + scanResult.SSID + " " + scanResult.level  + "\n";
                    fos.write(entry.getBytes());

                    wifiList.add(wifi);
                    if (counter == 3) {
                        break;
                    }
                    counter += 1;
                }

                fos.close();



                readFile();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    };

    private void readFile(){
        FileInputStream fis = null;
        listItems.clear();
        try {
            fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));


            String strLine;
            while ((strLine = br.readLine()) != null) {
                Log.e("qw", String.valueOf( strLine ) );
                listItems.add(strLine);
            }
            listAdapter.notifyDataSetChanged();
            in.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}