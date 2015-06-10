package com.canyinghao.canaccess.activity.set;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.canyinghao.canaccess.R;
import com.canyinghao.canhelper.IntentHelper;
import com.canyinghao.canhelper.SPHepler;
import com.kenumir.materialsettings.MaterialSettingsActivity;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.SwitcherItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.PreferencesStorageInterface;
import com.kenumir.materialsettings.storage.StorageInterface;

public class SetAllActivity extends MaterialSettingsActivity {

    private static final int SDK_VERSION = Build.VERSION.SDK_INT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setToolbarLayout(getText(R.string.set4).toString());
        setToolbar(R.mipmap.ic_arrow_back_white,"","",new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             IntentHelper.getInstance().finish(context);


            }
        },null);
        addItem(new HeaderItem(getFragment()).setTitle(getText(R.string.header_notify1).toString()));
        addAllSwitcher();

        addAPPList();

        for (int i = 0; i < 9; i++) {


            TextItem textItem = new TextItem(getFragment(), "");
            addItem(textItem);


        }

    }

    /**
     * ×Ü¿ª¹Ø
     */
    private void addAllSwitcher() {
        final CheckboxItem notify1=   new SwitcherItem(getFragment(), "set_all1").setTitle(getText(R.string.set_all1).toString()).setSubtitle(getText(R.string.set_all1a).toString());
        addItem(notify1);
        notify1.setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem item, boolean isChecked) {
                if (isChecked){
                    SPHepler.getInstance().setInt("set_all1",0);
                }else{
                    SPHepler.getInstance().setInt("set_all1",1);
                }
            }
        });
        int set_notify1= SPHepler.getInstance().getInt("set_all1");
        if (set_notify1==0){
            notify1.updateChecked(true);

        }else{
            notify1.updateChecked(false);
        }



    }


    private void addAPPList() {
        addItem(new DividerItem(getFragment()));


        addItem(new TextItem(getFragment(), "set_all2").setTitle(getText(R.string.set_all2).toString()).setSubtitle(getText(R.string.set_all2a).toString()).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {

                IntentHelper.getInstance().showIntent(context,AppListActivity.class,new String[]{"all"},new String[]{"all"},true);
            }
        }));
    }

    @Override
    public StorageInterface initStorageInterface() {
        return new PreferencesStorageInterface(this);
    }





}
