package com.example.jeremy.lab4;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class Task3Activity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private TextView txt_orient ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task3);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this,accelerometer,10000);


        txt_orient = findViewById(R.id.txt_orient);
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        int x = (int)sensorEvent.values[0];
        int y = (int)sensorEvent.values[1];
        int z = (int)sensorEvent.values[2];
        String flag = null;
        if(x>8 && flag!="Left"){
            flag = "Left";
            txt_orient.setText("Left");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if(x<-8 && flag!="Right"){
            flag = "Right";
            txt_orient.setText("Right");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
        if(y<-8 && flag!="Up Side Down"){
            flag = "Up Side Down";
            txt_orient.setText("Up Side Down");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        }
        if(y>8 && flag!="Default"){
            flag = "Default";
            txt_orient.setText("Default");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if(z>8 && flag!="On the table"){
            flag = "On the table";
            txt_orient.setText("On the table");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}

