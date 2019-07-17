package com.example.joanna.lab10;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static android.net.wifi.WifiManager.*;

public class MainActivity extends AppCompatActivity {

    private TextView textresult, bitrate, textView3;
    Integer linkSpeed;
    Button btn;
    int frequency;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textresult = (TextView) findViewById(R.id.avalia);
        bitrate = (TextView) findViewById(R.id.bit);
        textView3 = (TextView)findViewById(R.id.con);
        btn = (Button) findViewById(R.id.button);


        WifiManager wifiManager= (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);

        //check device support 5G or not

        Class cls = null;
        try {
            cls = Class.forName("android.net.wifi.WifiManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method method = null;
        try {
            method = cls.getMethod("isDualBandSupported");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Object invoke = null;
        try {
            invoke = method.invoke(wifiManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        boolean is5GhzSupported=(boolean)invoke;
        String str = String.valueOf(is5GhzSupported);

        textresult.setText("Connect 5Ghz wifi: "+ str);

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        //WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        //4.4 getfrequency
        String tempSsidString = wifiInfo.getSSID();
        if (tempSsidString != null && tempSsidString.length() > 2) {
            String wifiSsid = tempSsidString.substring(1, tempSsidString.length() - 1);
            List<ScanResult> scanResults=wifiManager.getScanResults();
            for(ScanResult scanResult:scanResults){
                if(scanResult.SSID.equals(wifiSsid)){
                    frequency = scanResult.frequency;
                    //bitrate.setText(" fre: "+frequency);
                    break;
                }
            }
        }


        if (wifiInfo != null) {
            linkSpeed = wifiInfo.getLinkSpeed();
            //wifiInfo.getFrequency();//measured using WifiInfo.LINK_SPEED_UNITS
            //frequency = wifiInfo.getFrequency();
            //int frequency = wifiInfo.getFrequency();
        }
        String rate = Integer.toString(linkSpeed);
//        frequency = wifiInfo.getFrequency();
//        int frequency = wifiInfo.getFrequency();
//
        bitrate.setText("frequency:"+frequency+" bit rate: "+rate);

        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                double fre = (double) frequency / 1000;

                if (fre >= 4.9 && fre <= 5.1 && linkSpeed < 54) {
                    textView3.setText("Current WiFi protocol is 802.11a\n" +
                            "The modulation is OFDM waveform.\n");
                } else if (fre >= 2.3 && fre <= 2.5 && linkSpeed < 11) {
                    textView3.setText("Current WiFi protocol is 802.11b\n" +
                            "The modulation is DSSS waveform.\n");
                } else if (fre >= 2.3 && fre <= 2.5 && linkSpeed < 54) {
                    textView3.setText("Current WiFi protocol is 802.11g\n" +
                            "The modulation is OFDM waveform.\n");
                } else if ((fre >= 2.3 && fre <= 2.5 && linkSpeed < 600) ||
                        (fre >= 4.9 && fre <= 5.1 && linkSpeed < 600)) {
                    textView3.setText("Current WiFi protocol is 802.11n\n" +
                            "The modulation is MIMO-OFDM waveform.\n");
                } else if (fre >= 4.9 && fre <= 5.1 && linkSpeed < 3466.8 ||
                        (fre >= 0.053 && fre <= 0.8 && linkSpeed < 568.9)) {
                    textView3.setText("Current WiFi protocol is 802.11ac\n" +
                            "The modulation is MIMO-OFDM waveform.\n");
                } else if (fre >= 59 && fre <= 61 && linkSpeed < 6757) {
                    textView3.setText("Current WiFi protocol is 802.11ad\n" +
                            "The modulation is OFDM(single carrier, low-power single carrier) waveform.\n");
                }
            }
        });
    }




}
