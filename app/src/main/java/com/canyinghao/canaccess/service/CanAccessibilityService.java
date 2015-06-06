package com.canyinghao.canaccess.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.bean.AppBean;
import com.canyinghao.canaccess.bean.EventBean;
import com.canyinghao.canhelper.DateHelper;
import com.canyinghao.canhelper.LogHelper;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class CanAccessibilityService extends AccessibilityService {

    public static int INVOKE_TYPE = 0;
    public static final int TYPE_CLEAN_APP = 1;
    public static final int TYPE_INSTALL_APP = 2;
    public static final int TYPE_UNINSTALL_APP = 3;

    public static void reset() {
        INVOKE_TYPE = 0;
    }

    private TextToSpeech mTts;

    @SuppressLint("NewApi")
    private static class AudioFocus {
        private static void abandonFocus(AudioManager audioMan) {
            audioMan.abandonAudioFocus(null);
        }

        private static void requestFocus(AudioManager audioMan) {
            audioMan.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        }
    }

    @Override
    public void onCreate() {
        mTts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.ERROR) {

                    Toast.makeText(getApplicationContext(), "TTS init fail", Toast.LENGTH_LONG).show();
                    return;
                }
                mTts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                    @Override
                    public void onUtteranceCompleted(String utteranceId) {

                    }
                });

            }
        });
        super.onCreate();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("test", event.eventTypeToString(event.getEventType()));
//        存储数据
        DbUtils utils = App.getInstance().getDbUtils();


        EventBean bean = new EventBean();
        bean.setAction(event.getAction());
        bean.setPackageName(getString(event.getPackageName()));
        bean.setBeforeText(getString(event.getBeforeText()));
        bean.setClassName(getString(event.getClassName()));
        bean.setEventTime(bean.getEventTime());
        bean.setEventType(bean.getEventType());
        bean.setEventTimeStr(DateHelper.getInstance().getDataString_1(new Date()));


        bean.setDatail(event.eventTypeToString(event.getEventType()));
        StringBuilder notifyMsg = new StringBuilder();
        if (!event.getText().isEmpty()) {
            for (CharSequence subText : event.getText()) {
                notifyMsg.append(subText);
                System.out.println(subText);
            }
        }
        bean.setText(notifyMsg.toString());


        bean.setLabel(findAppLabel(bean.getPackageName()));
        try {
            utils.saveBindingId(bean);
        } catch (DbException e) {
            e.printStackTrace();
        }


        processAccessibilityEnvent(event,bean);


    }

    private void processAccessibilityEnvent(AccessibilityEvent event,EventBean bean) {


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
                default:
                    break;
            }

        }


        switch (event.getEventType()) {

            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                spreak(event,bean);

                break;


        }


    }


    /**
     * 语音提醒
     * @param event
     */
    private void spreak(AccessibilityEvent event,EventBean bean) {


        StringBuilder notifyMsg = new StringBuilder();
        if (!event.getText().isEmpty()) {
            for (CharSequence subText : event.getText()) {
                notifyMsg.append(subText);
                System.out.println(subText);
            }
        }





        notifyMsg.append(bean.getLabel());


        final HashMap<String, String> ttsParams = new HashMap<String, String>();


        ttsParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, Long.toString(System.currentTimeMillis()));


        LogHelper.loge(notifyMsg.toString());
        mTts.speak(notifyMsg.toString(), TextToSpeech.QUEUE_ADD, ttsParams);
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



    private String findAppLabel(String pkg) {
        String lable="";

        try {
            ApplicationInfo info=   getPackageManager().getApplicationInfo(pkg, 0);
           lable=String.valueOf(info.loadLabel(getPackageManager()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        getPackageManager().getApplicationLabel()
//
//        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
//
//        for (PackageInfo info : packages) {
//
//            if (pkg.equals(info.packageName)) {
//                return info;
//            }
//
//        }

        return lable;
    }

    /** 是否不提醒
     * @param pkg
     * @return
     */
    private boolean filterAppInfo(String pkg) {
        try {


            List<AppBean> list = App.getInstance().getDbUtils().findAll(AppBean.class);

            if (list != null && !list.isEmpty()) {
                for (AppBean bean : list) {

                    if (pkg.equals(bean.getPackage())) {
                        return true;
                    }
                }

            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
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
     * @param event
     */
    private void processUninstallApplication(AccessibilityEvent event) {

        if (event.getSource() != null) {

            if (event.getPackageName().equals("com.android.packageinstaller")) {
                resetApplication();
                List<AccessibilityNodeInfo> ok_nodes = event.getSource().findAccessibilityNodeInfosByText("确定");
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
     * @param event
     */
    private void processinstallApplication(AccessibilityEvent event) {

        if (event.getSource() != null) {

            if (event.getPackageName().equals("com.android.packageinstaller")) {
                resetApplication();
                List<AccessibilityNodeInfo> unintall_nodes = event.getSource().findAccessibilityNodeInfosByText("安装");

                if (unintall_nodes != null && !unintall_nodes.isEmpty()) {
                    AccessibilityNodeInfo node;
                    for (int i = 0; i < unintall_nodes.size(); i++) {
                        node = unintall_nodes.get(i);
                        if (node.getClassName().equals("android.widget.Button") && node.isEnabled()) {
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }

                List<AccessibilityNodeInfo> next_nodes = event.getSource().findAccessibilityNodeInfosByText("下一步");
                if (next_nodes != null && !next_nodes.isEmpty()) {
                    AccessibilityNodeInfo node;
                    for (int i = 0; i < next_nodes.size(); i++) {
                        node = next_nodes.get(i);
                        if (node.getClassName().equals("android.widget.Button") && node.isEnabled()) {
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }

                List<AccessibilityNodeInfo> ok_nodes = event.getSource().findAccessibilityNodeInfosByText("打开");
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
     * @param event
     */
    private void processCleanApplication(AccessibilityEvent event) {

        if (event.getSource() != null) {
            resetApplication();
            if (event.getPackageName().equals("com.android.settings")) {
                resetApplication();
                List<AccessibilityNodeInfo> stop_nodes = event.getSource().findAccessibilityNodeInfosByText("清除数据");
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

                List<AccessibilityNodeInfo> ok_nodes = event.getSource().findAccessibilityNodeInfosByText("清除缓存");
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


    /**
     * 卸载后重置状态
     */
  private void   resetApplication(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                INVOKE_TYPE=0;

            }
        },10000);


    }


}
