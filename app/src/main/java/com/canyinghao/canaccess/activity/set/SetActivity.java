package com.canyinghao.canaccess.activity.set;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.service.CanAccessibilityService;
import com.canyinghao.canaccess.utils.PatternLockUtils;
import com.canyinghao.canhelper.IntentHelper;
import com.canyinghao.canhelper.PhoneHelper;
import com.kenumir.materialsettings.MaterialSettingsActivity;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.PreferencesStorageInterface;
import com.kenumir.materialsettings.storage.StorageInterface;

import java.io.Serializable;

public class SetActivity extends MaterialSettingsActivity {

    private static final int SDK_VERSION = Build.VERSION.SDK_INT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setToolbarLayout(getText(R.string.setting).toString());
        setToolbar(R.mipmap.ic_arrow_back_white,"","",new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             onBackPressed();

            }
        },null);
        addAccess();

        addNotify();

        addAction();

        addAll();
        addTrash();


        addAPPList();
        addLock();
        addGoodUse();

        addBadUse();



    }

    private void addAPPList() {
        addItem(new DividerItem(getFragment()));


        addItem(new TextItem(getFragment(), "set_notify2").setTitle(getText(R.string.set_notify2).toString()).setSubtitle(getText(R.string.set_notify2a).toString()).setIcon(R.mipmap.ic_apps_grey600).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {

                startActivity(new Intent(context,AppListActivity.class));
            }
        }));
    }

    private void addBadUse() {
        addItem(new DividerItem(getFragment()));

        addItem(new TextItem(getFragment(), "set7").setTitle(getText(R.string.set7).toString()).setSubtitle(getText(R.string.set7a).toString()).setIcon(R.mipmap.ic_thumb_down_grey600).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {


                CanAccessibilityService.INVOKE_TYPE = CanAccessibilityService.TYPE_UNINSTALL_APP;
                Uri packageURI = Uri.parse("package:"+getPackageName());
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                startActivity(uninstallIntent);


            }
        }));
    }

    private void addGoodUse() {
        addItem(new DividerItem(getFragment()));



        addItem(new TextItem(getFragment(), "set6").setTitle(getText(R.string.set6).toString()).setSubtitle(getText(R.string.set6a).toString()).setIcon(R.mipmap.ic_thumb_up_grey600).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    PhoneHelper.getInstance().show(getText(R.string.instance_market).toString());
                    e.printStackTrace();
                }
            }
        }));
    }



    private void addLock() {
        addItem(new DividerItem(getFragment()));
        addItem(new HeaderItem(getFragment()).setTitle(getText(R.string.headeritem3).toString()));


        addItem(new TextItem(getFragment(), "set_lock").setTitle(getText(R.string.set_lock).toString()).setSubtitle(getText(R.string.set_locka).toString()).setIcon(R.mipmap.ic_thumb_up_grey600).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {

                if (PatternLockUtils.hasPattern(context)){
                    IntentHelper.getInstance().showIntent(context,PwdConfirmActivity.class,new String[]{"class"},new Serializable[]{SetPwdActivity.class});
                }else{

                    IntentHelper.getInstance().showIntent(context,SetPwdActivity.class);
                }






            }
        }));
    }

    private void addTrash() {
        addItem(new DividerItem(getFragment()));

        addItem(new TextItem(getFragment(), "set5").setTitle(getText(R.string.set5).toString()).setSubtitle(getText(R.string.set5a).toString()).setIcon(R.mipmap.ic_delete_grey600).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {
                Toast.makeText(SetActivity.this, "Clicked", Toast.LENGTH_SHORT).show();


            }
        }));
    }

    private void addAll() {
        addItem(new DividerItem(getFragment()));

        addItem(new TextItem(getFragment(), "set4").setTitle(getText(R.string.set4).toString()).setSubtitle(getText(R.string.set4a).toString()).setIcon(R.mipmap.ic_dashboard_grey600).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {
                Toast.makeText(SetActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void addAction() {
        addItem(new DividerItem(getFragment()));

        addItem(new TextItem(getFragment(), "set3").setTitle(getText(R.string.set3).toString()).setSubtitle(getText(R.string.set3a).toString()).setIcon(R.mipmap.ic_videocam_grey600).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {
                Toast.makeText(SetActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void addNotify() {
        addItem(new DividerItem(getFragment()));

        addItem(new HeaderItem(getFragment()).setTitle(getText(R.string.headeritem2).toString()));

        addItem(new TextItem(getFragment(), "set2").setTitle(getText(R.string.set2).toString()).setSubtitle(getText(R.string.set2a).toString()).setIcon(R.mipmap.ic_mic_grey600).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {


                IntentHelper.getInstance().showIntent(SetActivity.this,SetNotifyActivity.class);

            }
        }));
    }

    private void addAccess() {
        addItem(new HeaderItem(getFragment()).setTitle(getText(R.string.headeritem1).toString()));

        addItem(new TextItem(getFragment(), "set1").setTitle(getText(R.string.set1).toString()).setSubtitle(getText(R.string.set1a).toString()).setIcon(R.mipmap.ic_settings_grey600).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {

              startActivity(getAccessibilityIntent());

            }
        }));
    }

    private Intent getAccessibilityIntent() {
        Intent intent = new Intent();
        if (SDK_VERSION > 4) {
            intent.setAction(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        } else if (SDK_VERSION== 4) {
            intent.setAction(Intent.ACTION_MAIN);
            intent.setClassName("com.android.settings", "com.android.settings.AccessibilitySettings");
        }
        return intent;
    }


    @Override
    public StorageInterface initStorageInterface() {
        return new PreferencesStorageInterface(this);
    }





}
