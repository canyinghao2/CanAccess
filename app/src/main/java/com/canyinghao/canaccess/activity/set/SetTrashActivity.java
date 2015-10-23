package com.canyinghao.canaccess.activity.set;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.bean.AppBean;
import com.canyinghao.canaccess.bean.EventBean;
import com.canyinghao.canaccess.bean.IgnoreBean;
import com.canyinghao.canaccess.utils.Utils;
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
import com.lidroid.xutils.exception.DbException;

public class SetTrashActivity extends MaterialSettingsActivity {

    private static final int SDK_VERSION = Build.VERSION.SDK_INT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setToolbarLayout(getText(R.string.set5).toString());
        setToolbar(R.mipmap.ic_arrow_back_white,"","",new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             IntentHelper.getInstance().finish(context);


            }
        },null);


        addItem(new HeaderItem(getFragment()).setTitle(getText(R.string.header_notify1).toString()));
        addTrashSwitcher();
        addType();

        addTime();
        addClear();

        for (int i = 0; i < 9; i++) {


            TextItem textItem = new TextItem(getFragment(), "");
            addItem(textItem);


        }

    }

    private void addClear() {
        addItem(new DividerItem(getFragment()));
        final TextItem trash4=  new TextItem(getFragment(), "set_trash4").setTitle(getText(R.string.set_trash4).toString()).setSubtitle(getText(R.string.set_trash4a).toString());
        addItem(trash4);
        trash4.setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem item) {

                try {
                    App.getInstance().getDbUtils().deleteAll(AppBean.class);
                    App.getInstance().getDbUtils().deleteAll(EventBean.class);
                    App.getInstance().getDbUtils().deleteAll(IgnoreBean.class);

                    Utils.showSnackbar(toolbar,getString(R.string.trash_success),"",null);

                } catch (DbException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void addTrashSwitcher() {
        final CheckboxItem notify1=   new SwitcherItem(getFragment(), "set_trash1").setTitle(getText(R.string.set_trash1).toString()).setSubtitle(getText(R.string.set_trash1a).toString());
        addItem(notify1);
        notify1.setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem item, boolean isChecked) {
                if (isChecked){
                    SPHepler.getInstance().setInt("set_trash1",0);
                }else{
                    SPHepler.getInstance().setInt("set_trash1",1);
                }
            }
        });
        int set_notify1= SPHepler.getInstance().getInt("set_trash1");
        if (set_notify1==0){
            notify1.updateChecked(true);

        }else{
            notify1.updateChecked(false);
        }



    }



    private void addType() {
        addItem(new DividerItem(getFragment()));
        final TextItem notify4=  new TextItem(getFragment(), "set_trash2").setTitle(getText(R.string.set_trash2).toString()).setSubtitle(getText(R.string.set_trash2a).toString());
        addItem(notify4);
        int set_notify4= SPHepler.getInstance().getInt("set_trash2");
        if (set_notify4==0){
            notify4.updateSubTitle(getText(R.string.set_trash2a).toString());

        }else{
            notify4.updateSubTitle(getText(R.string.set_trash2a1).toString());
        }
        notify4.setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(context);

                builder.setTitle(R.string.set_trash2);
                int set_notify4= SPHepler.getInstance().getInt("set_trash2");

                builder.setSingleChoiceItems(new String[]{getString(R.string.set_trash2a),getString(R.string.set_trash2a1)},set_notify4,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i==0){
                            SPHepler.getInstance().setInt("set_trash2",0);


                            notify4.updateSubTitle(getText(R.string.set_trash2a).toString());
                        }else{
                            SPHepler.getInstance().setInt("set_trash2",1);

                            notify4.updateSubTitle(getText(R.string.set_trash2a1).toString());
                        }


                    }
                });


                builder.create().show();


            }
        });



    }


    private void addTime() {
        addItem(new DividerItem(getFragment()));
        final TextItem trash3=  new TextItem(getFragment(), "set_trash3").setTitle(getText(R.string.set_trash3).toString()).setSubtitle(getText(R.string.set_trash2a).toString());
        addItem(trash3);
        int set_trash3= SPHepler.getInstance().getInt("set_trash3");

          if (set_trash3==0){
              set_trash3=7;
          }
        trash3.updateSubTitle(set_trash3+getString(R.string.day));



        trash3.setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(context);

                builder.setTitle(R.string.set_trash3);
                int set_notify4= SPHepler.getInstance().getInt("set_trash3");
                int postion=0;
                if (set_notify4==0||set_notify4==7){
                    postion=0;

                }else if(set_notify4==14){
                    postion=1;

                }else{
                    postion=2;

                }

                builder.setSingleChoiceItems(getResources().getStringArray(R.array.trash_time),postion,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        switch (i){

                            case 0:
                                SPHepler.getInstance().setInt("set_trash3",7);
                                trash3.updateSubTitle(7+getString(R.string.day));
                                break;
                            case 1:
                                SPHepler.getInstance().setInt("set_trash3",14);
                                trash3.updateSubTitle(14+getString(R.string.day));
                                break;
                            case 2:
                                SPHepler.getInstance().setInt("set_trash3",30);
                                trash3.updateSubTitle(30+getString(R.string.day));
                                break;
                        }
                    }
                });


                builder.create().show();


            }
        });



    }




    @Override
    public StorageInterface initStorageInterface() {
        return new PreferencesStorageInterface(this);
    }


    @Override
    public void onBackPressed() {
        IntentHelper.getInstance().finish(this);
    }


}
