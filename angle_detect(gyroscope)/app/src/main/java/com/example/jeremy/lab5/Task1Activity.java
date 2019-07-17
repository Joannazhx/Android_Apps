package com.example.jeremy.lab5;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Task1Activity extends AppCompatActivity implements SensorEventListener {

    private TextView txtRotation;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1);

        txtRotation = findViewById(R.id.txtRotation);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i("SENSOR", String.valueOf(event.values[0] + '-' +event.values[1]+ '-' +event.values[2]));
//        String text = String.format("(x,y,z): %0f, %0f, %0f", event.values[0], event.values[1],event.values[2]);
        txtRotation.setText("(x,y,z): "+event.values[0]+" "+ event.values[1] +" "+ event.values[2]);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}