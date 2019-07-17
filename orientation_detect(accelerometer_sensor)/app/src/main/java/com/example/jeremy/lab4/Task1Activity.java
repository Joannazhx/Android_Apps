package com.example.jeremy.lab4;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Task1Activity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private List <Sensor> sensorList;
    private ArrayList<String> myList = new ArrayList<>();
    private ArrayAdapter adapter;

    private ListView ls;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1);
        ls = findViewById(R.id.ls);
        btn = findViewById(R.id.button);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,myList);
        ls.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSensors();
            }
        });
    }

    private void showSensors() {
        for (Sensor itemSensor: sensorList) {

            myList.add(
                    "Name: " + itemSensor.getName().toString() + "\n" +
                            "Vendor: " + itemSensor.getVendor().toString() + "\n" +
                            "Version: " + itemSensor.getVersion() + "\n" +
                            "Max Range: " + itemSensor.getMaximumRange() + "\n" +
                            "Min Delay: " + itemSensor.getMinDelay() + "\n"
            );
        }

        adapter.notifyDataSetChanged();
    }





}
