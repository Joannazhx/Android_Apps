package com.example.joanna.lab5_2;

import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private Button start;
    private Button stop;
    private TextView text;
    private TextView mag_text;
    private SensorManager SM;
    private Sensor gyroscope;
    private Sensor magno;
    private SensorEventListener magnoListener;
    private SensorEventListener gyroscopeListener;
    private float start_value = 0;
    private float stop_value = 0;
    private float value;
    private float x;
    private float y;
    private float z;
    private float m_x;
    private float m_y;
    private float m_z;
    private float rotation;
    private  float timestamp = 0f;
    private float dT;
    private TextView angle;
    private float NS2S = 1.0f / 1000000000.0f;
    private float magn_val;
    private float grand_val = 0;
    private TextView mag_textview;

    LocationManager locationManager;
    LocationListener locationListener;
    Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button)findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        text = (TextView) findViewById(R.id.gydata);
        angle= (TextView) findViewById(R.id.angel);
        mag_text = (TextView) findViewById(R.id.magdata);
        mag_textview = (TextView) findViewById(R.id.head);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
