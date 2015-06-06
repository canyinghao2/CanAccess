package com.canyinghao.canaccess.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.canyinghao.canhelper.PhoneHelper;
import com.canyinghao.canhelper.SPHepler;

/**
 * Created by yangjian on 15/6/7.
 */
public class PhoneChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int wifi=  SPHepler.getInstance().getInt("set_notify7_a");
        int gprs=  SPHepler.getInstance().getInt("set_notify7_b");


        if (wifi==0&&WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 这个监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);

            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:

                    PhoneHelper.getInstance().show("WIFI_STATE_DISABLED");

                    break;
                case WifiManager.WIFI_STATE_DISABLING:
//                    PhoneHelper.getInstance().show("WIFI_STATE_DISABLING");


                    break;
                case WifiManager.WIFI_STATE_ENABLED:

                    PhoneHelper.getInstance().show("WIFI_STATE_ENABLED");

                    break;
                case WifiManager.WIFI_STATE_ENABLING:
//                    PhoneHelper.getInstance().show("WIFI_STATE_ENABLING");



                    break;

            }
        }

        if (gprs==0&&intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){

            ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {// unconnect network

                PhoneHelper.getInstance().show("mobNetInfo_STATE_ENABLED");
            } else {// connect network

                PhoneHelper.getInstance().show("mobNetInfo_STATE_ENABLED");
            }

        }






    }
}
