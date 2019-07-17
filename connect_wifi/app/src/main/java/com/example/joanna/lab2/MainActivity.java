package com.example.joanna.lab2;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.Build;
//import android.app.AlertDialog;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifimanager;
    private ConnectivityManager connect;
    private Button allwifi;
    private Button topfour;
    private ListView listView;
    private ArrayList<ScanResult> scanResults;
    private ArrayList<String> printResult;
    private ArrayList<String> result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set UI component
        allwifi = (Button)findViewById(R.id.allwifi);
        topfour = (Button)findViewById(R.id.top4);
        listView = (ListView)findViewById(R.id.listview);

        //init wifi service
        wifimanager = (WifiManager)getSystemService(WIFI_SERVICE);
        wifimanager.setWifiEnabled(true);
        
        setclicklisten();

    }

    private void setclicklisten() {
        allwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task1();
            }
        });
        
        topfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task2();
            }
        });
    }
    
    private void task1() {
        scanResults = (ArrayList<ScanResult>) wifimanager.getScanResults();
        printResult = new ArrayList<>();
        final ArrayList<String> result = new ArrayList<>();
        String pass = "";
        for(int i = 0;i < scanResults.size();i++){
            if(scanResults.get(i).capabilities.equals("[ESS]")){
                pass = "OPEN";

            }else{
                pass = "Private";
            }
            printResult.add(scanResults.get(i).SSID + " " + scanResults.get(i).BSSID + " " + scanResults.get(i).level + " "+ pass);
            result.add(scanResults.get(i).SSID);
        }
        listView.setAdapter(new ArrayAdapter<>(this,R.layout.sowwifilist,printResult));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                connectwifi(position);
            }
        });

    }

    private void connectwifi(final int position) {
        final View connectwindow = getLayoutInflater().inflate(R.layout.pass,null);

        //dialog window setting
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(printResult.get(position));
        dialog.setView(connectwindow);//UI -- showwifi.xml
        dialog.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //get text set type String
                EditText user = (EditText) connectwindow.findViewById(R.id.username);
                EditText pass = (EditText) connectwindow.findViewById(R.id.password);
                String username = user.getText().toString();
                String password = pass.getText().toString();


                WifiConfiguration config = new WifiConfiguration();
                WifiEnterpriseConfig enterpriseConfig = new WifiEnterpriseConfig();
                config.SSID = "\"" + scanResults.get(position).SSID + "\"";
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
                enterpriseConfig.setIdentity(username);
                enterpriseConfig.setPassword(password);
                config.enterpriseConfig = enterpriseConfig;

                //
                int netId = wifimanager.addNetwork(config);
                wifimanager.saveConfiguration();
                wifimanager.disconnect();
                wifimanager.enableNetwork(netId, true);
                wifimanager.reconnect();
                connect = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);


                while(true) {
                    NetworkInfo message = connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (message.isConnected()) {
                        new android.app.AlertDialog.Builder(MainActivity.this)
                                .setTitle("Connected Success")
                                .setMessage(scanResults.get(position).SSID + "\n")
                                .setPositiveButton("Confirm", null)
                                .create().show();
                        break;
                    }
                }

            }

        });
        dialog. setNegativeButton("Cancel",null);
        dialog.show();


    }

    private void task2() {
        scanResults = (ArrayList<ScanResult>) wifimanager.getScanResults();
        printResult = new ArrayList<>();
        final ArrayList<String> result = new ArrayList<>();
        //sort base on signal(.level)
        for (int j = scanResults.size()-1; j >=0;--j){
            for (int k = scanResults.size()-1 ;k>=0;--k){
                if (scanResults.get(j).level>scanResults.get(k).level){
                    ScanResult temp = null;
                    temp = scanResults.get(j);
                    scanResults.set(j,scanResults.get(k));
                    scanResults.set(k,temp);
                }
            }
        }
        int k = 4;
        if(k >= scanResults.size()){
            k = scanResults.size();
        }
        String pass = "";
        for(int i = 0;i < k;i++){
            if(scanResults.get(i).capabilities.equals("[ESS]")){
                pass = "OPEN";

            }else{
                pass = "Private";
            }
            printResult.add(scanResults.get(i).SSID + " " + scanResults.get(i).BSSID + " " + scanResults.get(i).level + " "+ pass);
            result.add(scanResults.get(i).SSID);
        }

        listView.setAdapter(new ArrayAdapter<>(this,R.layout.sowwifilist,printResult));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                connectwifi(position);
            }
        });
    }

}
