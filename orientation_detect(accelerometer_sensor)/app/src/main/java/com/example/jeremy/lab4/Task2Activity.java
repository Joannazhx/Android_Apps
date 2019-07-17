package com.example.jeremy.lab4;

import android.content.Context;
import android.hardware.Sensor;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static java.lang.Math.round;

public class Task2Activity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView txt;
    private TextView without;
    private double[] gravity = {0,0,0} ;
    private double[] linear_acceleration = {0,0,0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        setContentView(R.layout.activity_task2);

        txt = findViewById(R.id.txt_x);
        without = findViewById(R.id.without);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {


            double x_raw = event.values[0];
            double y_raw = event.values[1];
            double z_raw = event.values[2];

            txt.setText(
                    "with g\n"+
                    "X:"+x_raw+"\n"+ "Y:"+y_raw + "\n" + "Z:"+z_raw + "\n");

            final float alpha = (float) 0.8;

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            without.setText( "without g\n"+
                    "X:"+linear_acceleration[0]+"\n"+
                    "Y:"+linear_acceleration[1] + "\n" +
                    "Z:"+linear_acceleration[2] + "\n"
            );


    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), mSensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }




}
