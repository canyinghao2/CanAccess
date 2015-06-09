package com.canyinghao.canaccess.receiver;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.canyinghao.canhelper.SPHepler;

/**
 * Created by yangjian on 15/6/10.
 */
public class NoisyAudioStreamReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if (action.equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    //拔出耳机

                    SPHepler.getInstance().setInt("headset",0);

                    break;
                case 1:
                    //插耳机自动播放
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


        //只监听拔出耳机
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
            SPHepler.getInstance().setInt("headset",0);
        }
    }

}
