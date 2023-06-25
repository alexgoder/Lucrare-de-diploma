package com.example.Control_WIFI_Electronice;

import android.annotation.SuppressLint;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import com.example.trial1_1.R;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class WifiAddrItem extends AppCompatActivity  {
    private String name;
    private String password="123456789";
    private TextView textView;
    private CheckBox checkBox;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_addr_item);

        textView=findViewById(R.id.name);
        checkBox=findViewById(R.id.checkBox);

    }


    public boolean checkIfSelected(){
        if(checkBox!=null)
        return checkBox.isChecked();
        else
            return false;
    }

    public WifiAddrItem(String name) {

        this.name = name;
    }

    public WifiAddrItem(String name, String password) {
        this.name = name;
        this.password = password;
    }


    public void Connect()
    {
        WifiConfiguration wifiConfiguration=new WifiConfiguration();
        wifiConfiguration.SSID="\""+ this.name+"\"";
        wifiConfiguration.preSharedKey="\""+ this.password+"\"";
        WifiManager wifiManager = (WifiManager)(this.getParent().getApplicationContext().getSystemService(this.getParent().getApplicationContext().WIFI_SERVICE));
        wifiManager.addNetwork(wifiConfiguration);
        @SuppressLint("MissingPermission") List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + this.name + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}


