package com.example.Control_WIFI_Electronice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WifiChangeBroadcastReceiver extends BroadcastReceiver {
    private WifiChangeBroadcastListener listener;
    public WifiChangeBroadcastReceiver(WifiChangeBroadcastListener listener){
        this.listener=listener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        listener.onWifiChangeBroadcastReceived(context, intent);
    }
    public interface WifiChangeBroadcastListener {
        void onWifiChangeBroadcastReceived(Context context, Intent intent);
    }
}
