package com.example.jeremy.lab8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task3Activity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button btnPeer, btnCheck;
    private TextView tvStatus;
    private WifiP2pManager WD_Manager;
    private ArrayAdapter<String> peerAdapter;

    private ListView lsView;
    private Channel mChannel;
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;

    private String[] deviceNameArray;
    private WifiP2pDevice[] deviceArray;
    private Boolean isStart = false;

    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    WifiP2pConfig config = new WifiP2pConfig();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_task3 );

        btnPeer = findViewById( R.id.btnPeerDiscover );
        btnPeer.setOnClickListener( this );
        btnCheck = findViewById( R.id.btnCheck );
        btnCheck.setOnClickListener( this );
        tvStatus = findViewById( R.id.tvStatus );
        tvStatus.setText( "" );

        peerAdapter = new ArrayAdapter<>( this,android.R.layout.simple_list_item_1 );
        lsView = findViewById( R.id.peerListView );
        lsView.setAdapter( peerAdapter );
        lsView.setOnItemClickListener( this );

        initializeWifiDirect();
    }

    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }
    /* unregister the broadcast receiver */

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
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
                if (isStart) {
                    stopFindPeers();
                } else {
                    peerDiscovery();
                }

                isStart = ! isStart;
                break;
            default:
                break;
        }
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void onSuccess() {
            tvStatus.setText( "Connecting Success" );
        }

        @Override
        public void onFailure(int i) {

        }
    };

    WifiP2pManager.PeerListListener myPeerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

            if(!peerList.getDeviceList().equals(peers)){
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                deviceNameArray = new String[peerList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];
                int index = 0;
                // maybe we can get more information here
                for(WifiP2pDevice device : peerList.getDeviceList()){
                    deviceNameArray[index] = device.deviceName+ "\n"+device.deviceAddress;
                    deviceArray[index] = device;
                    index++;
                }
                Log.d("this is my array", "arr: " + Arrays.toString(deviceNameArray));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Task3Activity.this, android.R.layout.simple_list_item_1, deviceNameArray);
                lsView.setAdapter(adapter);
            }


            if(peers.size() == 0){
                Log.d("WIFIDIRECT", "No devices found");
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String line[] = deviceNameArray[i].split( "\\r?\\n" );
        config.deviceAddress = line[1];


        WD_Manager.connect(mChannel, config, actionListener);

    }


    public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

        private WifiP2pManager mManager;
        private WifiP2pManager.Channel mChannel;
        private Task3Activity mActivity;

        public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, Task3Activity activity) {
            super();
            this.mManager = manager;
            this.mChannel = channel;
            this.mActivity = activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();


            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {

                } else {

                }
            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                // Call WifiP2pManager.requestPeers() to get a list of current peers
                mManager.requestPeers( mChannel, myPeerListListener );
            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                // Respond to new connection or disconnections
            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                // Respond to this device's wifi state changing
            }
        }
    }


    private void peerDiscovery() {
        WD_Manager.discoverPeers( mChannel,  new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                tvStatus.setText( "Discovery Start" );

            }

            @Override
            public void onFailure(int i) {
                tvStatus.setText( "Failed :"+i );
            }
        });
    }

    public void stopFindPeers(){
        if(WD_Manager != null){
            WD_Manager.stopPeerDiscovery(mChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    tvStatus.setText( "Discovery Stop" );
                }

                @Override
                public void onFailure(int reason) {
                    tvStatus.setText( "Failed :"+reason );
                }
            });
        }
    }

    public void initializeWifiDirect(){
        //Wifi Direct
        // This clall provides the API for managing Wifi peer-to-peer connectivity.
        WD_Manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        // A channel that connects the application to the Wifi p2p framework.
        // Most p2p operations require a channel as an argument.
        if (WD_Manager != null) {
            mChannel = WD_Manager.initialize(this, getMainLooper(), null);
        }

        receiver = new Task3Activity.WiFiDirectBroadcastReceiver(WD_Manager, mChannel, this);

        intentFilter = new IntentFilter();

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

    }
}
