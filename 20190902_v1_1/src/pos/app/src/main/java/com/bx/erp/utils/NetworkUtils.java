package com.bx.erp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.log4j.Logger;

public class NetworkUtils {
    private static Logger log = Logger.getLogger(NetworkUtils.class);
    public static final int TYPE_NOT_CONNECTED = 0;
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;

    //判断网络是否连接
    public static boolean isNetworkAvalible(Context context) {
        // 获得网络状态管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            // 建立网络数组
            NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();
            if (net_info != null) {
                for (int i = 0; i < net_info.length; i++) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    } else {
                        log.info("网络不可用！");
                    }
                }
            }
        }
        return false;
    }

    public NetworkUtils() {

    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(ConnectivityManager.TYPE_WIFI == activeNetwork.getType()) {
                return TYPE_WIFI;
            } else if(ConnectivityManager.TYPE_MOBILE == activeNetwork.getType()) {
                return TYPE_MOBILE;
            }
        }

        return TYPE_NOT_CONNECTED;
    }

//    public static String getConnectivityStatusString(Context context) {
//        int conn = NetworkUtils.getConnectivityStatus(context);
//        String status = null;
//        if (conn == NetworkUtils.TYPE_WIFI) {
//            status = "Wifi enabled";
//        } else if (conn == NetworkUtils.TYPE_MOBILE) {
//            status = "Mobile data enabled";
//        } else if (conn == NetworkUtils.TYPE_NOT_CONNECTED) {
//            status = "Not connected to Internet";
//        }
//        return status;
//    }

}
