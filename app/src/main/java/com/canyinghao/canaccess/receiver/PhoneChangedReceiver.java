package com.canyinghao.canaccess.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.service.CanAccessibilityService;
import com.canyinghao.canhelper.PhoneHelper;
import com.canyinghao.canhelper.SPHepler;

/**
 * Created by yangjian on 15/6/7.
 */
public class PhoneChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!CanAccessibilityService.isWifiUse){
            return;
        }
        int wifi=  SPHepler.getInstance().getInt("set_notify7_a");
        int gprs=  SPHepler.getInstance().getInt("set_notify7_b");


        if (wifi==0&&WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// �������wifi�Ĵ���رգ���wifi�������޹�
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);


            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:

                    PhoneHelper.getInstance().show(context.getString(R.string.wifi_unconnect));

                    break;
                case WifiManager.WIFI_STATE_DISABLING:
//                    PhoneHelper.getInstance().show("WIFI_STATE_DISABLING");


                    break;
                case WifiManager.WIFI_STATE_ENABLED:

                    PhoneHelper.getInstance().show(context.getString(R.string.wifi_success));

                    break;
                case WifiManager.WIFI_STATE_ENABLING:
//                    PhoneHelper.getInstance().show("WIFI_STATE_ENABLING");



                    break;

            }
        }

        if (gprs==0&&intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){

            ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);


            NetworkInfo  netInfo= connectMgr.getActiveNetworkInfo();

            if(netInfo != null && netInfo.isAvailable()) {


                String name = netInfo.getTypeName();

                if(netInfo.getType()==ConnectivityManager.TYPE_WIFI){

                    PhoneHelper.getInstance().show(context.getString(R.string.wifi_success));
                }else if(netInfo.getType()==ConnectivityManager.TYPE_ETHERNET){


                }else if(netInfo.getType()==ConnectivityManager.TYPE_MOBILE){

                    PhoneHelper.getInstance().show(context.getString(R.string.mob_success));
                }
            } else {

                PhoneHelper.getInstance().show(context.getString(R.string.net_unconnect));
            }

        }




        String action=intent.getAction();
        if (action.equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:


                    SPHepler.getInstance().setInt("headset",0);

                    break;
                case 1:

                    SPHepler.getInstance().setInt("headset",1);
                    break;
                default:

                    break;
            }

        }

        if (BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)){


            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if(BluetoothProfile.STATE_DISCONNECTED == adapter.getProfileConnectionState(BluetoothProfile.HEADSET)) {
                //Bluetooth headset is now disconnected

                SPHepler.getInstance().setInt("headset",0);

            }
        }


        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
            SPHepler.getInstance().setInt("headset",0);
        }



    }
}
