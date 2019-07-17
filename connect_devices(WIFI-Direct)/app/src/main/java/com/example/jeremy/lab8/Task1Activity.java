package com.example.jeremy.lab8;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Task1Activity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPeer, btnCheck;
    private TextView tvStatus;
    private WifiP2pManager WD_Manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_task1 );

        btnPeer = findViewById( R.id.btnPeerDiscover );
        btnPeer.setOnClickListener( this );
        btnCheck = findViewById( R.id.btnCheck );
        btnCheck.setOnClickListener( this );
        tvStatus = findViewById( R.id.tvStatus );
        tvStatus.setText( "" );

        WD_Manager = (WifiP2pManager) getSystemService( Context.WIFI_P2P_SERVICE );

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCheck:
                if (WD_Manager.WIFI_P2P_STATE_ENABLED!=2) {
                    tvStatus.setText( "Wifi Direct not available" );

                } else {
                    tvStatus.setText( "Wifi Direct available" );
                }

                break;
            case R.id.btnPeerDiscover:

                break;
            default:
                break;
        }

    }
}