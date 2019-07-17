package com.example.joanna.myapplication;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Button battery_status;
    TextView battery_text;
    TextView current_battery;
    TextView init_battery;
    TextView check_type;
    private Timer timer;
    float lastp ;


    int flag = 0;

    int test_period = 1000 * 60 * 10;

    private Intent batteryStatus ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        battery_status = (Button) findViewById(R.id.status);
        battery_text = (TextView) findViewById(R.id.bat);
        current_battery = (TextView) findViewById(R.id.current);
        init_battery = (TextView) findViewById(R.id.init);
        check_type = (TextView) findViewById(R.id.ty);

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            flag = 1;
        }
        if ( manager.isProviderEnabled( LocationManager.NETWORK_PROVIDER ) ) {
            flag += 2;
        }



        final IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = this.registerReceiver(null, ifilter);

        timer = new Timer();

        battery_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                // How are we charging?
                int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
                boolean wifiCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS;

                float batteryPct = level / (float)scale * 100;

                String mess = "initial level of battery: " + batteryPct +"%";
                lastp = batteryPct;

                init_battery.setText(mess);

                if (usbCharge) {
                    battery_text.setText( "Current level of battery is: "+batteryPct+"%\n Mobile is charging via usb" );

                }else if(acCharge) {
                    battery_text.setText( "Current level of battery is: "+batteryPct+"%\n Mobile is charging via AC" );
                }else if(wifiCharge){
                    battery_text.setText( "Current level of battery is: "+batteryPct+"%\n Mobile is charging via Wireless" );
                } else {
                    battery_text.setText( "Current level of battery is: "+batteryPct+"%" );

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("gg  ");
                Message message = new Message();
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                float batteryPct = level / (float)scale * 100;
                System.out.println("final level of battery: " + batteryPct +"%");
                //String mess = "final level of battery: " + batteryPct +"%";
                float diff = lastp - batteryPct;
                System.out.println(diff);
                message.what = 1;
                message.arg1 = (int)batteryPct;
                message.arg2 = (int)diff;
                handler.sendMessage(message);


            }
            },3*60*1000,3*60*1000);
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message){
            String inf = "";
            int g = message.arg1;
            int g2 = message.arg2;
            switch (message.what) {
                case 1:
                    inf = "final level of battery: " +g + "%";
                    current_battery.setText(inf);
                    inf = "Consumed battary : " +g2 + "%";
                    check_type.setText(inf);
                    if(flag == 0){
                        inf = "Normal usage of mobile phone for 10 mins";
                    }else if(flag == 1){
                        inf = "Using GPS for 10 mins";
                    }else if(flag == 2){
                        inf = "Using Wi-Fi for 10 mins";
                    }else{
                        inf = "Using Wi-Fi and GPS for 10 mins";
                    }
                    battery_text.setText(inf);
                    break;

            }
        }
    };

}
