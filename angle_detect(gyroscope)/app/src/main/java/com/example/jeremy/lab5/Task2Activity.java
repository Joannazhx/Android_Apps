package com.example.jeremy.lab5;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class   Task2Activity extends AppCompatActivity implements SensorEventListener {

    private TextView txtRotation;
    private Button btnStart;
    private Sensor mSensor, accelerometerReading, magnetometerReading;
    private SensorManager mSensorManager;
    private Double direction, offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2);
        offset = 0.0;

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        txtRotation = findViewById(R.id.txtRotation);
        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startSensor();
                offset = direction;
            }
        });
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        double x,y , clean;
        x = event.values[0];
        y = event.values[1];

//        direction =0;
        if (x>0) {
            direction = 270 +Math.atan(y/x) * 180/Math.PI;
        } else if (x < 0) {
            direction = 90 +Math.atan(y/x) * 180/Math.PI;
        } else if (x == 0 && y > 0) {
            direction = 0.0;
        } else {
            direction = 180.0;
        }

        clean =  direction - offset;
        if (clean < 0) {
            clean = clean + 360.01;
        }


//        direction = Math.atan(y/x) * 180/Math.PI;
        txtRotation.setText(""+ clean);
        Log.i("DIR", String.valueOf(direction));
//        String text = String.format("(x,y,z): %0f, %0f, %0f", event.values[0], event.values[1],event.values[2]);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {



    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    protected void startSensor() {
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this,sensor, SensorManager.SENSOR_DELAY_NORMAL);


    }
}
