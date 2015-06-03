package com.canyinghao.canaccess.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.canyinghao.canhelper.LogHelper;

import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class MyAccessibilityService extends AccessibilityService {

    public static int INVOKE_TYPE = 0;
    public static final int TYPE_KILL_APP = 1;
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
//                        messageStatuses.poll();
//                        if (messageStatuses.isEmpty()) {
//                            if (Build.VERSION.SDK_INT >= 8 && shouldRequestFocus) {
//                                AudioFocus.abandonFocus(audioMan);
//                            }
//                            shake.disable();
//                        }
                    }
                });
                /* added in API 15 (4.0.3).  Once enough users are on that version to justify it, uncomment this block, as onUtteranceCompletedListener is now deprecated
                mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        messageStatuses.poll();
                        if(messageStatuses.isEmpty()){
                            if (shouldRequestFocus) {
                                audioMan.abandonAudioFocus(null);
                            }
                            shake.disable();
                        }
                    }

                    @Override
                    public void onError(String utteranceId) {
                        Log.w(TAG, getString(R.string.error_tts_init));
                        Toast.makeText(getApplicationContext(), R.string.error_tts_init, Toast.LENGTH_LONG).show();
                    }
                });
                */
            }
        });
        super.onCreate();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // TODO Auto-generated method stub
        this.processAccessibilityEnvent(event);

        Log.e("onaccessibility", event.getText().toString());
    }

    private void processAccessibilityEnvent(AccessibilityEvent event) {

        Log.d("test", event.eventTypeToString(event.getEventType()));
        if (event.getSource() == null) {
            Log.d("test", "the source = null");
        } else {
            Log.d("test", "event = " + event.toString());
            switch (INVOKE_TYPE) {
                case TYPE_KILL_APP:
                    processKillApplication(event);
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
                spreak(event);

                break;


        }


    }


    private void spreak(AccessibilityEvent event) {


        StringBuilder notifyMsg = new StringBuilder();
        if (!event.getText().isEmpty()) {
            for (CharSequence subText : event.getText()) {
                notifyMsg.append(subText);
                System.out.println(subText);
            }
        }
        PackageInfo info = findOrAddApp(event.getPackageName().toString(), this);
        LogHelper.loge(event.getPackageName()+"");
        if (info != null) {
            String label = info.applicationInfo.loadLabel(getPackageManager()).toString();
            LogHelper.loge(label);
            notifyMsg.append(label);
        }

        final HashMap<String, String> ttsParams = new HashMap<String, String>();
        //needed because we want to apply stream changes immediately
//        ttsParams.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
//                Integer.toString(Common.getSelectedAudioStream(getApplicationContext())));
        //not used anywhere, but has to be set to get the UtteranceProgressListener to trigger its submethods
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

    private void processUninstallApplication(AccessibilityEvent event) {

        if (event.getSource() != null) {
            if (event.getPackageName().equals("com.android.packageinstaller")) {
                List<AccessibilityNodeInfo> ok_nodes = event.getSource().findAccessibilityNodeInfosByText("确定");
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

    private void processinstallApplication(AccessibilityEvent event) {

        if (event.getSource() != null) {
            if (event.getPackageName().equals("com.android.packageinstaller")) {
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

    private void processKillApplication(AccessibilityEvent event) {

        if (event.getSource() != null) {
            if (event.getPackageName().equals("com.android.settings")) {
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


    private PackageInfo findOrAddApp(String pkg, Context ctx) {

        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);

//        packageInfo.applicationInfo.loadLabel(getPackageManager()).toString()


        for (PackageInfo info : packages) {

            if (pkg.equals(info.packageName)) {
                return info;
            }

        }

        return null;
    }

}
