package com.canyinghao.canaccess.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothHeadset;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.bean.AppBean;
import com.canyinghao.canaccess.bean.EventBean;
import com.canyinghao.canaccess.bean.IgnoreBean;
import com.canyinghao.canaccess.receiver.PhoneChangedReceiver;
import com.canyinghao.canhelper.DateHelper;
import com.canyinghao.canhelper.LogHelper;
import com.canyinghao.canhelper.SPHepler;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class CanAccessibilityService extends AccessibilityService {

    public static int INVOKE_TYPE = 0;
    public static final int TYPE_CLEAN_APP = 1;
    public static final int TYPE_INSTALL_APP = 2;
    public static final int TYPE_UNINSTALL_APP = 3;
    public static final int TYPE_OPEN_ACCESS = 4;

    public static void reset() {
        INVOKE_TYPE = 0;
    }

    public static   boolean isWifiUse=false;

    private TextToSpeech mTts;
    PhoneChangedReceiver receiver;



    @Override
    public void onCreate() {
        addHeadSetReceiver();
        mTts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.ERROR) {

                    return;
                }


            }
        });



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isWifiUse=true;
            }
        },10000);
        super.onCreate();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogHelper.logi("test", event.toString());

        StringBuilder notifyMsg = new StringBuilder();
        if (!event.getText().isEmpty()) {
            for (CharSequence subText : event.getText()) {
                notifyMsg.append(subText);

            }
        }
        if (TextUtils.isEmpty(notifyMsg.toString())) {
            return;
        }

        if (event.getEventType() != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED && event.getPackageName().equals(getPackageName())) {
            return;
        }


//        存储数据
        DbUtils utils = App.getInstance().getDbUtils();


        try {
            long count = utils.count(Selector.from(AppBean.class).where("packageName", "=", event.getPackageName()));
            if (count > 0) {
                return;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }


        EventBean bean = new EventBean();

        bean.packageName = getString(event.getPackageName());
        bean.beforeText = getString(event.getBeforeText());
        bean.className = getString(event.getClassName());
        bean.eventTime = new Date().getTime();
        bean.eventType = event.getEventType();
        bean.eventTimeStr = DateHelper.getInstance().getDataString_1(new Date());
        bean.eventTypeStr = AccessibilityEvent.eventTypeToString(event.getEventType());

        bean.datail = event.toString();

        bean.text = notifyMsg.toString();
        findAppLabelIcon(bean);

        try {
            utils.saveBindingId(bean);
        } catch (DbException e) {
            e.printStackTrace();
        }


        processAccessibilityEnvent(event, bean);


    }

    private void processAccessibilityEnvent(AccessibilityEvent event, EventBean bean) {


        if (event.getSource() != null) {
            switch (INVOKE_TYPE) {
                case TYPE_CLEAN_APP:
                    processCleanApplication(event);
                    break;
                case TYPE_INSTALL_APP:
                    processinstallApplication(event);
                    break;
                case TYPE_UNINSTALL_APP:
                    processUninstallApplication(event);
                    break;
                case TYPE_OPEN_ACCESS:

                    openAccess(event);

                    break;
                default:
                    break;
            }

        }


        switch (event.getEventType()) {

            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:


                int set_notify1 = SPHepler.getInstance().getInt("set_notify1");
                if (set_notify1 == 0) {
                    spreak(event, bean);
                }


                break;


        }


    }


    /**
     * 语音提醒
     *
     * @param event
     */
    private void spreak(AccessibilityEvent event, EventBean bean) {


        int notify_9 = SPHepler.getInstance().getInt("set_notify9");
        int headset = SPHepler.getInstance().getInt("headset");
        if (notify_9 == 1 && headset == 0) {
            return;
        }

        int notify_10 = SPHepler.getInstance().getInt("set_notify10");

        int start_time = SPHepler.getInstance().getInt("start_time");
        int end_time = SPHepler.getInstance().getInt("end_time");
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        boolean isOpen = true;
        if (notify_10 == 1) {
            if (start_time == end_time) {
                isOpen = false;

            } else if (start_time < end_time) {

                if (hour >= start_time && hour < end_time) {
                    isOpen = false;
                } else {
                    isOpen = true;
                }

            } else {

                if (hour >= start_time || hour < end_time) {
                    isOpen = false;
                } else {
                    isOpen = true;
                }

            }
        }

        if (!isOpen) {
            return;
        }


        try {
//            忽略的字段

            List<IgnoreBean> igList = App.getInstance().getDbUtils().findAll(IgnoreBean.class);

            if (igList != null && !igList.isEmpty()) {
                for (IgnoreBean bean1 : igList) {

                    if (bean.text.toLowerCase().contains(bean1.text.toLowerCase())) {
                        return;
                    }
                }
            }


            //        不播放选择列表中的
            List<AppBean> list = App.getInstance().getDbUtils().findAll(AppBean.class);

            if (list != null && !list.isEmpty()) {
                for (AppBean bean1 : list) {

                    if (bean1.packageName.equals(bean.packageName)) {
                        return;
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
//判断是否播放Toast

        if (SPHepler.getInstance().getInt("set_notify5") == 1) {
            if (event.getClassName().toString().contains("android.widget.Toast")) {

                return;
            }
        }


        final HashMap<String, String> ttsParams = new HashMap<String, String>();


        ttsParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, Long.toString(System.currentTimeMillis()));
// 听筒播放还是扬声器播放
        ttsParams.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                Integer.toString(SPHepler.getInstance().getInt("set_notify4")));

         String speakStr=bean.label + bean.text;
       if(SPHepler.getInstance().getInt("set_notify11")!=0){

           speakStr=bean.text;
       }



        mTts.speak(speakStr, TextToSpeech.QUEUE_ADD, ttsParams);
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        return true;

    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub

    }


    private void findAppLabelIcon(EventBean bean) {


        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(bean.packageName, 0);
            String lable = String.valueOf(info.loadLabel(getPackageManager()));
            bean.label = lable;


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }


    private String getString(Object ob) {

        if (ob == null) {
            return "";
        } else {
            return ob.toString();
        }

    }


    /**
     * 卸载软件
     *
     * @param event
     */
    private void processUninstallApplication(AccessibilityEvent event) {

        if (event.getSource() != null) {

            if (event.getPackageName().equals("com.android.packageinstaller")) {
                resetApplication();
                List<AccessibilityNodeInfo> ok_nodes = event.getSource().findAccessibilityNodeInfosByText(getString(R.string.sure));
                if (ok_nodes != null && !ok_nodes.isEmpty()) {
                    AccessibilityNodeInfo node;
                    for (int i = 0; i < ok_nodes.size(); i++) {
                        node = ok_nodes.get(i);
//                        node.getClassName().equals("android.widget.Button") &&
                        if (node.isEnabled()) {
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }

                }
            }
        }

    }

    /**
     * 安装软件
     *
     * @param event
     */
    private void processinstallApplication(AccessibilityEvent event) {

        if (event.getSource() != null) {

            if (event.getPackageName().equals("com.android.packageinstaller")) {
                resetApplication();
                List<AccessibilityNodeInfo> unintall_nodes = event.getSource().findAccessibilityNodeInfosByText(getString(R.string.installer));

                if (unintall_nodes != null && !unintall_nodes.isEmpty()) {
                    AccessibilityNodeInfo node;
                    for (int i = 0; i < unintall_nodes.size(); i++) {
                        node = unintall_nodes.get(i);
                        if (node.getClassName().equals("android.widget.Button") && node.isEnabled()) {
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }

                List<AccessibilityNodeInfo> next_nodes = event.getSource().findAccessibilityNodeInfosByText(getString(R.string.next));
                if (next_nodes != null && !next_nodes.isEmpty()) {
                    AccessibilityNodeInfo node;
                    for (int i = 0; i < next_nodes.size(); i++) {
                        node = next_nodes.get(i);
                        if (node.getClassName().equals("android.widget.Button") && node.isEnabled()) {
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }

                List<AccessibilityNodeInfo> ok_nodes = event.getSource().findAccessibilityNodeInfosByText(getString(R.string.open));
                if (ok_nodes != null && !ok_nodes.isEmpty()) {
                    AccessibilityNodeInfo node;
                    for (int i = 0; i < ok_nodes.size(); i++) {
                        node = ok_nodes.get(i);
                        if (node.getClassName().equals("android.widget.Button") && node.isEnabled()) {
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }


            }
        }

    }

    /**
     * 清除数据
     *
     * @param event
     */
    private void processCleanApplication(AccessibilityEvent event) {

        if (event.getSource() != null) {

            if (event.getPackageName().equals("com.android.settings")) {
                resetApplication();
                List<AccessibilityNodeInfo> stop_nodes = event.getSource().findAccessibilityNodeInfosByText(getString(R.string.clear_data));
                if (stop_nodes != null && !stop_nodes.isEmpty()) {
                    AccessibilityNodeInfo node;
                    for (int i = 0; i < stop_nodes.size(); i++) {
                        node = stop_nodes.get(i);
                        if (node.getClassName().equals("android.widget.Button")) {
                            if (node.isEnabled()) {
                                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        }
                    }
                }

                List<AccessibilityNodeInfo> ok_nodes = event.getSource().findAccessibilityNodeInfosByText(getString(R.string.clear_cache));
                if (ok_nodes != null && !ok_nodes.isEmpty()) {
                    AccessibilityNodeInfo node;
                    for (int i = 0; i < ok_nodes.size(); i++) {
                        node = ok_nodes.get(i);
                        if (node.getClassName().equals("android.widget.Button")) {
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            Log.d("action", "click ok");
                        }
                    }

                }
            }
        }
    }







    private void clickSure(AccessibilityEvent event,String packName,String btnStr,int action) {
        if (event.getSource() != null) {
            if (event.getPackageName().equals(packName)) {


                List<AccessibilityNodeInfo> ok_nodes = event.getSource().findAccessibilityNodeInfosByText(btnStr);



                if (ok_nodes != null && !ok_nodes.isEmpty()) {
                    AccessibilityNodeInfo node;
                    for (int i = 0; i < ok_nodes.size(); i++) {
                        node = ok_nodes.get(i);


                        node.performAction(action);



                    }

                }


            }


        }


    }

    private void openAccess(final AccessibilityEvent event) {
        clickSure(event,"com.android.settings","canaccess",AccessibilityNodeInfo.ACTION_CLICK);


    }


    /**
     * 卸载后重置状态
     */
    private void resetApplication() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                reset();

            }
        }, 10000);


    }

    private void addHeadSetReceiver() {
        receiver = new PhoneChangedReceiver();
        IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (receiver != null) {

            unregisterReceiver(receiver);
        }
    }
}
