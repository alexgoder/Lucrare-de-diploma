package com.example.Control_WIFI_Electronice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.trial1_1.R;

import java.util.ArrayList;
import java.util.List;

public class WifiSearch extends AppCompatActivity {

    private static ArrayList<WifiAddrItem> wifiAddrItems;

    private WifiManager wifiManager;
    RecyclerView textView;

    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.wifi_search_rgb);
        textView = findViewById(R.id.recyclerView);

        wifiAddrItems = new ArrayList<>();
        Button btn2 = findViewById(R.id.BackBtn);
        Button btn3 = findViewById(R.id.ScanWIFIBtn);
        Button next = findViewById(R.id.ConnectBtn);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi not enabled", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);

        }
        setUserInfo();
        scanWifi();
        setAdapter();
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanWifi();
                setAdapter();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();

            }

        });
    }

    private void stateMachine(View view) {
        Intent intent;
        switch (MainActivity.router) {
            case RGB:
                intent = new Intent(view.getContext(), SelectColor.class);
                startActivity(intent);
                break;
            case Centrala:
                intent = new Intent(view.getContext(), CentralaControl.class);
                startActivity(intent);
                break;
            case Semafor:
                intent = new Intent(view.getContext(), SemaforExemplu.class);
                startActivity(intent);
                break;
            case Default:

            default:
                break;
        }
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        Button btnNxt, btnBack;
        dialog.setContentView(R.layout.popup_window);
        btnNxt = dialog.findViewById(R.id.buttonCnt);
        btnBack = dialog.findViewById(R.id.buttonBack);
        String networkSSID = "ESP8266-Access-Point";
        String networkPass = "123456789";

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
        conf.wepKeys[0] = "\"" + networkPass + "\"";
        conf.wepTxKeyIndex = 0;
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        conf.preSharedKey = "\"" + networkPass + "\"";
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        @SuppressLint("MissingPermission") List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }
        btnNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateMachine(v);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(v.getContext(), WifiSearch.class);
                startActivity(intent);

            }
        });

        dialog.show();
    }

    private void scanWifi() {
        wifiAddrItems.clear();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);
        boolean status=((WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE)).startScan();
        if(status)
            scanSuccess();
        else
            scanFailure();
    }

    IntentFilter intentFilter = new IntentFilter();

    BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            boolean success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (!success) {
                @SuppressLint("MissingPermission") List<ScanResult> results = ((WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE)).getScanResults();
                if(!results.isEmpty())
                    for(ScanResult res:results)
                    {
                        if(res.SSID.length()>0)
                        {
                            wifiAddrItems.add(new WifiAddrItem(res.SSID));
                        }

                    }

            } else {
                // scan failure handling
                @SuppressLint("MissingPermission") List<ScanResult> results = ((WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE)).getScanResults();
                wifiAddrItems.clear();
            }
        }
    };

    private void scanFailure() {
        @SuppressLint("MissingPermission") List<ScanResult> results = wifiManager.getScanResults();

       wifiAddrItems.add(new WifiAddrItem("Nu s-a gasit nicio retea WiFi"));
        if(MainActivity.router==Router.Semafor)
        {
            wifiAddrItems.clear();
            wifiAddrItems.add(new WifiAddrItem("Semafor WiFi"));
        }
        if(MainActivity.router==Router.RGB)
        {
            wifiAddrItems.clear();
            wifiAddrItems.add(new WifiAddrItem("Leduri RGB WiFi"));
        }
        if(MainActivity.router==Router.Centrala)
        {
            wifiAddrItems.clear();
            wifiAddrItems.add(new WifiAddrItem("Centrala WiFi"));
        }
    }

    private void scanSuccess() {
        @SuppressLint("MissingPermission") List<ScanResult> results = wifiManager.getScanResults();
        for(ScanResult res:results)
        {
            if(res.SSID.length()>0)
            {
                wifiAddrItems.add(new WifiAddrItem(res.SSID));
            }
        }
        if(MainActivity.router==Router.Semafor)
        {
            wifiAddrItems.add(new WifiAddrItem("Semafor WiFi"));
        }
    }

    private void setAdapter(){
        CustomAdapter customAdapter = new CustomAdapter(wifiAddrItems);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        textView.setLayoutManager(layoutManager);
        textView.setItemAnimator(new DefaultItemAnimator());
        textView.setAdapter(customAdapter);

    }

    private void setUserInfo(){
        wifiAddrItems.add(new WifiAddrItem("ESP8266-Access-Point"));
        wifiAddrItems.add(new WifiAddrItem("TP-LINK286779"));
    }

    public void getScanResults(@NonNull final List<ScanResult> results)
    {
        if(results.isEmpty())
        {
            setUserInfo();
        }
        else{
            for(int i=0;i<results.size();i++)
                wifiAddrItems.add(new WifiAddrItem(results.get(i).SSID));
        }
    }


}


