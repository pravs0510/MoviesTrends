package com.example.android.moviestrends;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by praveena on 11/22/2015.
 */
public class NetUtils {
    public static boolean hasConnectivity(Context context, boolean roamingOK) {
        boolean hasConnectivity = true;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        hasConnectivity = info != null && (info.isConnected() || (roamingOK && info.isRoaming()));
        return hasConnectivity;
    }
}
