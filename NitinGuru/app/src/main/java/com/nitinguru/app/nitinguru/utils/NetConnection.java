package com.nitinguru.app.nitinguru.utils;

/**
 * Created by Rahul on 07-03-2016.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashMap;
import java.util.Map;

public class NetConnection {
    public Map<String, String> getConnectionDetails(Context ctn) {
        Map<String, String> networkDetails = new HashMap<String, String>();
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)ctn.getSystemService(ctn.CONNECTIVITY_SERVICE);
            NetworkInfo wifiNetwork = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetwork != null && wifiNetwork.isConnected()) {

                networkDetails.put("Type", wifiNetwork.getTypeName());
                networkDetails.put("Sub type", wifiNetwork.getSubtypeName());
                networkDetails.put("State", wifiNetwork.getState().name());
            }

            NetworkInfo mobileNetwork = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileNetwork != null && mobileNetwork.isConnected()) {
                networkDetails.put("Type", mobileNetwork.getTypeName());
                networkDetails.put("Sub type", mobileNetwork.getSubtypeName());
                networkDetails.put("State", mobileNetwork.getState().name());
                if (mobileNetwork.isRoaming()) {
                    networkDetails.put("Roming", "YES");
                } else {
                    networkDetails.put("Roming", "NO");
                }
            }
        } catch (Exception e) {
            networkDetails.put("Status", e.getMessage());
        }
        return networkDetails;
    }
}
