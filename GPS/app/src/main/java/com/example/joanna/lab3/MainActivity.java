package com.example.joanna.lab3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button getstatus;
    private Button getlocation;
    private TextView status;
    private TextView location;
    private TextView track;

    private Location locations;
    private LocationManager locationManager;
    private String provide = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getstatus = (Button) findViewById(R.id.getstatus);
        getlocation = (Button) findViewById(R.id.getlocation);
        status = (TextView) findViewById(R.id.status);
        location = (TextView) findViewById(R.id.location);
        track = (TextView) findViewById(R.id.track);
        //set click listener
        setclicklistener();
    }

    private void setclicklistener() {
        getstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task1();
                getlocation.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        task2();
                    }
                });
            }
        });

    }

    private void task1() {
        //get location service
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //check GPS status
        if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
            status.setText("GPS is active");
        } else {
            status.setText("GPS is not active");

            //use dialog ,set GPS status
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Enable GPS");
            dialog.setMessage("GPS is not active.\nDo you want to open GPS?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0);
                }
            });
            dialog.setNegativeButton("No", null);
            dialog.show();
        }

    }

    private void task2() {
        String loc = GetLocation();
        location.setText(loc);

    }

    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String loc = GetLocation();
            Toast.makeText(getApplicationContext(), loc, Toast.LENGTH_SHORT).show();
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
    };

    @SuppressLint("MissingPermission")
    public String GetLocation() {
//        Toast.makeText(getApplicationContext(), "Fetching location details, please wait a minute.", Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Double altitude = 0.0;
        Double longitute = 0.0;
        Double latitude = 0.0;
//        long time = 0;
        String provider = "";
        float speed = 0f;
        float accuracy = 0f;

        if (!isGPSEnabled && !isNetworkEnabled) {
            return "can not get your location.";
        } else {
            if (isNetworkEnabled) {
                if (locationManager != null) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener);
                    locations = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                    System.out.println("network " + location);
                    if(locations != null) {
                        altitude = locations.getAltitude();
                        latitude = locations.getLatitude();
                        longitute = locations.getLongitude();
//                        time = location.getTime();
                        provider = locations.getProvider();
                        speed = locations.getSpeed();
                        accuracy = locations.getAccuracy();
                    } else {
                        return "Network locating...\nPlease wait a moment.";
                    }
                } else {
                    return "Network locationManager error";
                }
            }else{
                //GPSEnabled == true
                if(locationManager != null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
                    locations = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    System.out.println("gps " + location);
                    if(location!=null){
                        altitude = locations.getAltitude();
                        latitude = locations.getLatitude();
                        longitute = locations.getLongitude();
//                        time = location.getTime();
                        provider = locations.getProvider();
                        speed = locations.getSpeed();
                        accuracy = locations.getAccuracy();
                    } else {
                        return "GPS locating...\nPlease wait a moment.";
                    }
                } else {
                    return "GPS locationmanager error";
                }
            }
        }
        long time = locations.getTime();
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String textofDate = sdf.format(date);
        return "Date/Time: " + textofDate +
                "\nProvider: " + provider +
                "\nAccuracy: " + accuracy +
                "\nLatitude: " + latitude +
                "\nLongtitude: " + longitute +
                "\nAltitude: " + altitude +
                "\nspeed: " + speed;

    }


}

























