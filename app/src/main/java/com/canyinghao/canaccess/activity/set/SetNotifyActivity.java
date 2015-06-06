package com.canyinghao.canaccess.activity.set;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.canyinghao.canaccess.R;
import com.canyinghao.canhelper.SPHepler;
import com.kenumir.materialsettings.MaterialSettingsActivity;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.SwitcherItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.PreferencesStorageInterface;
import com.kenumir.materialsettings.storage.StorageInterface;

public class SetNotifyActivity extends MaterialSettingsActivity {
    private static final int SDK_VERSION = Build.VERSION.SDK_INT;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbarLayout(getText(R.string.set2).toString());
        setToolbar(R.mipmap.ic_arrow_back_white,"","",new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        },null);

        addItem(new HeaderItem(getFragment()).setTitle(getText(R.string.header_notify1).toString()));

        addNotifySwitcher();


        addTTSSet();


        addStream();

        addToast();

        addIgnoreText();

        addPhoneStaut();

        addTest();


    }

    private void addTest() {
        addItem(new DividerItem(getFragment()));

        addItem(new HeaderItem(getFragment()).setTitle(getText(R.string.header_notify2).toString()));

        addItem(new TextItem(getFragment(), "set_notify8").setTitle(getText(R.string.set_notify8).toString()).setSubtitle(getText(R.string.set_notify8a).toString()).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {

               String[] strs=   getResources().getStringArray(R.array.notify_test);
//              NotificationCompat.Builder  builder= PhoneHelper.getInstance().getNotifyBuilder(strs[0],strs[1],strs[2],null,R.mipmap.ic_launcher,null,null, Notification.DEFAULT_ALL);
//
//               PhoneHelper.getInstance().showNotifyBigText(strs[0],strs[1],strs[3],builder,0);


                NotificationManager manager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new Notification();

                notification.icon = R.mipmap.ic_launcher;



                notification.tickerText = strs[3];
                notification.when = System.currentTimeMillis();


              notification.setLatestEventInfo(context, strs[0], strs[3], null);

               notification.flags = Notification.FLAG_AUTO_CANCEL; // 点击通知后自动消失

                manager.notify(1001,notification);

            }
        }));
    }

    private void addPhoneStaut() {
        addItem(new DividerItem(getFragment()));
       final TextItem notify_7= new TextItem(getFragment(), "set_notify7").setTitle(getText(R.string.set_notify7).toString()).setSubtitle(getText(R.string.set_notify7a).toString());

        addItem(notify_7);

        notify_7.setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {


              int wifi=  SPHepler.getInstance().getInt("set_notify7_a");
              int gprs=  SPHepler.getInstance().getInt("set_notify7_b");
               boolean[] bs= new boolean[2];


                bs[0]=wifi==0?true:false;
                bs[1]=gprs==0?true:false;



                AlertDialog.Builder builder= new AlertDialog.Builder(context);

                builder.setTitle(R.string.phone_stauts);
                int set_notify4= SPHepler.getInstance().getInt("set_notify7");

                builder.setMultiChoiceItems(R.array.phone_stauts_array,bs,new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (i==0){

                            SPHepler.getInstance().setInt("set_notify7_a",b?0:1);
                        }else{
                            SPHepler.getInstance().setInt("set_notify7_b",b?0:1);
                        }




                    }
                });


                builder.create().show();


            }
        });

    }

    private void addIgnoreText() {
        addItem(new DividerItem(getFragment()));


        addItem(new TextItem(getFragment(), "set_notify6").setTitle(getText(R.string.set_notify6).toString()).setSubtitle(getText(R.string.set_notify6a).toString()).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {


            }
        }));
    }

    private void addStream() {
        addItem(new DividerItem(getFragment()));
        final TextItem notify4=  new TextItem(getFragment(), "set_notify4").setTitle(getText(R.string.set_notify4).toString()).setSubtitle(getText(R.string.set_notify4a).toString());
        addItem(notify4);
        int set_notify4= SPHepler.getInstance().getInt("set_notify4");
        if (set_notify4==0){
            notify4.updateTitle(getText(R.string.set_notify4).toString());

        }else{
            notify4.updateTitle(getText(R.string.set_notify4_).toString());
        }
        notify4.setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(context);

                builder.setTitle(R.string.play_mode);
                 int set_notify4= SPHepler.getInstance().getInt("set_notify4");

                builder.setSingleChoiceItems(getResources().getStringArray(R.array.play_mode_array),set_notify4,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i==0){
                            SPHepler.getInstance().setInt("set_notify4",0);
                            context.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

                            notify4.updateTitle(getText(R.string.set_notify4).toString());
                        }else{
                            SPHepler.getInstance().setInt("set_notify4",1);
                            context.setVolumeControlStream(AudioManager.STREAM_MUSIC);
                            notify4.updateTitle(getText(R.string.set_notify4_).toString());
                        }
                    }
                });


                builder.create().show();


            }
        });



    }

    private void addTTSSet() {
        addItem(new DividerItem(getFragment()));


        addItem(new TextItem(getFragment(), "set_notify3").setTitle(getText(R.string.set_notify3).toString()).setSubtitle(getText(R.string.set_notify3a).toString()).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {
                 startActivity(getTtsIntent());

            }
        }));

    }



    /**
     * notify总开关
     */
    private void addNotifySwitcher() {
        final CheckboxItem notify1=   new SwitcherItem(getFragment(), "set_notify1").setTitle(getText(R.string.set_notify1).toString()).setSubtitle(getText(R.string.set_notify1a).toString());
        addItem(notify1);
        notify1.setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem item, boolean isChecked) {
                  if (isChecked){
                      SPHepler.getInstance().setInt("set_notify1",0);
                  }else{
                      SPHepler.getInstance().setInt("set_notify1",1);
                  }
            }
        });
        int set_notify1= SPHepler.getInstance().getInt("set_notify1");
        if (set_notify1==0){
            notify1.updateChecked(true);

        }else{
            notify1.updateChecked(false);
        }



    }

    /**
     * Toast开关
     */
    private void addToast() {
        addItem(new DividerItem(getFragment()));


       final  CheckboxItem notify5=  new SwitcherItem(getFragment(), "set_notify5").setTitle(getText(R.string.set_notify5).toString()).setSubtitle(getText(R.string.set_notify5a).toString());
        addItem(notify5);
        int set_notify5= SPHepler.getInstance().getInt("set_notify5");
        if (set_notify5==0){
            notify5.updateChecked(true);

        }else{
            notify5.updateChecked(false);
        }

        notify5.setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem item, boolean isChecked) {
                if (isChecked){
                    SPHepler.getInstance().setInt("set_notify5",0);
                }else{
                    SPHepler.getInstance().setInt("set_notify5",1);
                }
            }
        });









    }

    @Override
    public StorageInterface initStorageInterface() {
        return new PreferencesStorageInterface(this);
    }



    private Intent getTtsIntent() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        if (isClassExist("com.android.settings.TextToSpeechSettings")) {
            if (SDK_VERSION >= 11 && SDK_VERSION <= 13) {
                intent.setAction(android.provider.Settings.ACTION_SETTINGS);
                intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, "com.android.settings.TextToSpeechSettings");
                intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT_ARGUMENTS, intent.getExtras());
            } else intent.setClassName("com.android.settings", "com.android.settings.TextToSpeechSettings");
        } else if (isClassExist("com.android.settings.Settings$TextToSpeechSettingsActivity")) {
            if (SDK_VERSION == 14) {
                intent.setAction(android.provider.Settings.ACTION_SETTINGS);
                intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, "com.android.settings.tts.TextToSpeechSettings");
                intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT_ARGUMENTS, intent.getExtras());
            } else intent.setClassName("com.android.settings", "com.android.settings.Settings$TextToSpeechSettingsActivity");
        } else if (isClassExist("com.google.tv.settings.TextToSpeechSettingsTop")) {
            intent.setClassName("com.google.tv.settings", "com.google.tv.settings.TextToSpeechSettingsTop");
        } else return null;
        return intent;
    }

    private boolean isClassExist(String name) {
        try {
            PackageInfo pkgInfo = getPackageManager().getPackageInfo(
                    name.substring(0, name.lastIndexOf(".")), PackageManager.GET_ACTIVITIES);
            if (pkgInfo.activities != null) {
                for (int n = 0; n < pkgInfo.activities.length; n++) {
                    if (pkgInfo.activities[n].name.equals(name)) return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {}
        return false;
    }



}
